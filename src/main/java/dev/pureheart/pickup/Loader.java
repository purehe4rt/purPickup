package dev.pureheart.pickup;

import dev.pureheart.pickup.listeners.PickupListener;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumSet;

public final class Loader extends JavaPlugin {

    @Getter
    private EnumSet<Material> whitelistBlocks;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        createListBlocks();

        getCommand("purpickup").setExecutor(new Commands(this));
        getServer().getPluginManager().registerEvents(new PickupListener(this), this);
    }

    public void createListBlocks() {
        this.whitelistBlocks = EnumSet.noneOf(Material.class);

        for (String name : this.getConfig().getStringList("whitelistBlocks")) {
            Material material = Material.valueOf(name.toUpperCase());
            whitelistBlocks.add(material);
        }
    }
}
