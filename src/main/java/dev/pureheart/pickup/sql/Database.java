package dev.pureheart.pickup.sql;

import dev.pureheart.pickup.Loader;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class Database {

    private final Loader plugin;
    private Connection connection;

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
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public void addPlayer(UUID uuid, boolean enabled) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT OR REPLACE INTO pickups (uuid, enabled) VALUES (?, ?);")) {
            preparedStatement.setString(1, String.valueOf(uuid));
            preparedStatement.setBoolean(2, enabled);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public boolean existPlayer(UUID uuid) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT 1 FROM pickups WHERE uuid = ?;")) {
            preparedStatement.setString(1, String.valueOf(uuid));
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException error) {
            error.printStackTrace();
        }

        return false;
    }

    public boolean getEnabled(UUID uuid) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT enabled FROM pickups WHERE uuid = ?;")) {
            preparedStatement.setString(1, String.valueOf(uuid));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean("enabled");
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }

        return false;
    }
}
