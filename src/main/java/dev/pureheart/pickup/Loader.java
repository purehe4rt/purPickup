package dev.pureheart.pickup;

import dev.pureheart.pickup.commands.PickupCommand;
import dev.pureheart.pickup.commands.PluginCommand;
import dev.pureheart.pickup.listeners.PickupListener;
import dev.pureheart.pickup.sql.Database;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumSet;

@Getter
public final class Loader extends JavaPlugin {

    private Database database;
    private EnumSet<Material> whitelistBlocks;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        createListBlocks();

        database = new Database(this);
        database.setup();

        getCommand("purpickup").setExecutor(new PluginCommand(this));
        getCommand("autopickup").setExecutor(new PickupCommand(this));

        getServer().getPluginManager().registerEvents(new PickupListener(this), this);
    }

    @Override
    public void onDisable() {
        database.close();
    }

    public void createListBlocks() {
        this.whitelistBlocks = EnumSet.noneOf(Material.class);

        for (String name : this.getConfig().getStringList("whitelistBlocks")) {
            Material material = Material.valueOf(name.toUpperCase());
            whitelistBlocks.add(material);
        }
    }
}
