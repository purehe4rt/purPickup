package dev.pureheart.pickup;

import dev.pureheart.pickup.commands.PickupCommand;
import dev.pureheart.pickup.commands.PluginCommand;
import dev.pureheart.pickup.listeners.PickupListener;
import dev.pureheart.pickup.listeners.PlayerListener;
import dev.pureheart.pickup.sql.Database;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumSet;

@Getter
public final class Loader extends JavaPlugin {

    private ConfigManager configManager;
    private Database database;

    private EnumSet<Material> whitelistBlocks;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configManager = new ConfigManager(this);

        createListBlocks();

        database = new Database(this);
        database.setup();

        for (Player player : Bukkit.getOnlinePlayers()) {
            database.loadPlayer(
                    player.getUniqueId(),
                    configManager.isAutoPickupActivated()
            );
        }

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new PickupListener(this), this);

        getCommand("purpickup").setExecutor(new PluginCommand(this));
        getCommand("autopickup").setExecutor(new PickupCommand(this));
    }

    @Override
    public void onDisable() {
        if (database != null) database.close();
    }

    public void createListBlocks() {
        this.whitelistBlocks = EnumSet.noneOf(Material.class);

        for (String name : configManager.getWhitelistBlocks()) {
            Material material = Material.valueOf(name.toUpperCase());
            whitelistBlocks.add(material);
        }
    }
}
