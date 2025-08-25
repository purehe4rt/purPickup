package dev.pureheart.pickup;

import dev.pureheart.pickup.listeners.PickupListener;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class Loader extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new PickupListener(this), this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll();
    }
}
