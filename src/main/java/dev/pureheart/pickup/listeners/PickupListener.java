package dev.pureheart.pickup.listeners;

import dev.pureheart.pickup.Loader;
import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.Map;

public record PickupListener(Loader plugin) implements Listener {

    @EventHandler
    public void onDrop(BlockDropItemEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE) return;
        if (!plugin.getDatabase().isEnabled(player.getUniqueId())) return;
        if (plugin.getWhitelistBlocks().contains(event.getBlockState().getType())) return;

        Iterator<Item> iter = event.getItems().iterator();

        while (iter.hasNext()) {
            Item itemEnt = iter.next();
            ItemStack item = itemEnt.getItemStack();
            Map<Integer, ItemStack> leftovers = player.getInventory().addItem(item);

            if (!leftovers.isEmpty()) {
                leftovers.values().forEach(left -> player.getWorld().dropItemNaturally(
                                player.getLocation(), left
                        )
                );
            }

            iter.remove();
        }
    }
}
