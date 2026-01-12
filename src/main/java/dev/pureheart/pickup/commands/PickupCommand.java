package dev.pureheart.pickup.commands;

import dev.pureheart.pickup.Loader;
import dev.pureheart.pickup.utils.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record PickupCommand(Loader plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String l, @NotNull String[] a) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorUtil.colorize(plugin.getConfigManager().getOnlyPlayers()));
            return true;
        }

        UUID uuid = player.getUniqueId();

        boolean current = plugin.getDatabase().isEnabled(uuid);
        boolean state = !current;

        plugin.getDatabase().setEnabled(uuid, state);

        player.sendMessage(ColorUtil.colorize(state ?
                plugin.getConfigManager().getToggledOn() :
                plugin.getConfigManager().getToggledOff()
        ));

        return true;
    }
}
