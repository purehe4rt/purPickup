package dev.pureheart.pickup.listeners;

import dev.pureheart.pickup.Loader;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public record PickupListener(Loader plugin) implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        boolean autoActivated = plugin.getConfig().getBoolean("autoPickupActivated", false);

        if (!event.isDropItems() || player.getGameMode() == GameMode.CREATIVE) return;
        if (!plugin.getDatabase().existPlayer(uuid)) plugin.getDatabase().addPlayer(uuid, autoActivated);
        if (!plugin.getDatabase().getEnabled(uuid)) return;

        Block block = event.getBlock();
        Material type = block.getType();

        if (plugin.getWhitelistBlocks().contains(type)) return;

        event.setDropItems(false);

        PlayerInventory inventory = player.getInventory();
        for (ItemStack drop : block.getDrops(inventory.getItemInMainHand())) {
            inventory.addItem(drop);
        }
    }
}
