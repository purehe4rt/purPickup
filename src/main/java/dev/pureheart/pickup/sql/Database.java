package dev.pureheart.pickup.sql;

import dev.pureheart.pickup.Loader;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Database {

    private final Loader plugin;
    private Connection connection;

    private final Map<UUID, Boolean> cache = new ConcurrentHashMap<>();

    public Database(Loader plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        try {
            String database = plugin.getConfig().getString("database", "database.db");
            File file = new File(plugin.getDataFolder(), database);

            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
            Statement statement = connection.createStatement();

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS pickups (" +
                    "uuid TEXT PRIMARY KEY," +
                    "enabled BOOLEAN" +
                    ");");
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public void close() {
        if (connection == null) return;

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT OR REPLACE INTO pickups (uuid, enabled) VALUES (?, ?)")) {
                for (var entry : cache.entrySet()) {
                    ps.setString(1, entry.getKey().toString());
                    ps.setBoolean(2, entry.getValue());
                    ps.addBatch();
                }

                ps.executeBatch();
            }

            connection.commit();

        } catch (SQLException error) {
            error.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException ignored) {}
        }
    }

    public void loadPlayer(UUID uuid, boolean defValue) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT enabled FROM pickups WHERE uuid = ?")) {

            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cache.put(uuid, rs.getBoolean("enabled"));
            } else {
                cache.put(uuid, defValue);
                insertPlayer(uuid, defValue);
            }

        } catch (SQLException error) {
            error.printStackTrace();
            cache.put(uuid, defValue);
        }
    }

    public void unloadPlayer(UUID uuid) {
        cache.remove(uuid);
    }

    private void insertPlayer(UUID uuid, boolean enabled) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO pickups (uuid, enabled) VALUES (?, ?)")) {
            ps.setString(1, uuid.toString());
            ps.setBoolean(2, enabled);
            ps.executeUpdate();
        }
    }

    public boolean isEnabled(UUID uuid) {
        return cache.getOrDefault(uuid, false);
    }

    public void setEnabled(UUID uuid, boolean enabled) {
        cache.put(uuid, enabled);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT OR REPLACE INTO pickups (uuid, enabled) VALUES (?, ?)")) {

                ps.setString(1, uuid.toString());
                ps.setBoolean(2, enabled);
                ps.executeUpdate();

            } catch (SQLException error) {
                error.printStackTrace();
            }
        });
    }
}
