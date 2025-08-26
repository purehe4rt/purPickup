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

import java.util.EnumSet;

public class PickupListener implements Listener {

    private final EnumSet<Material> whitelistBlocks;

    public PickupListener(Loader plugin) {
        this.whitelistBlocks = EnumSet.noneOf(Material.class);

        for (String name : plugin.getConfig().getStringList("whitelistBlocks")) {
            Material material = Material.valueOf(name.toUpperCase());
            whitelistBlocks.add(material);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (!event.isDropItems() || player.getGameMode() == GameMode.CREATIVE) return;

        Block block = event.getBlock();
        Material type = block.getType();

        if (whitelistBlocks.contains(type)) return;

        event.setDropItems(false);

        PlayerInventory inventory = player.getInventory();
        for (ItemStack drop : block.getDrops(inventory.getItemInMainHand())) {
            inventory.addItem(drop);
        }
    }
}
