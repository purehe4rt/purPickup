package dev.pureheart.pickup.listeners;

import dev.pureheart.pickup.Loader;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PickupListener implements Listener {

    private final List<String> whitelistBlocks;

    public PickupListener(Loader plugin) {
        this.whitelistBlocks = plugin.getConfig().getStringList("whitelistBlocks");
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (!event.isDropItems() || player.getGameMode() == GameMode.CREATIVE) return;

        Block block = event.getBlock();
        String type = block.getType().name();

        if (whitelistBlocks.contains(type)) return;

        event.setDropItems(false);

        List<ItemStack> drops = new ArrayList<>(block.getDrops(player.getInventory().getItemInMainHand()));
        drops.forEach(drop -> player.getInventory().addItem(drop));
    }
}
