package dev.pureheart.pickup.listeners;

import dev.pureheart.pickup.Loader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public record PlayerListener(Loader plugin) implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        plugin.getDatabase().loadPlayer(uuid, plugin.getConfigManager().isAutoPickupActivated());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        plugin.getDatabase().unloadPlayer(uuid);
    }
}
