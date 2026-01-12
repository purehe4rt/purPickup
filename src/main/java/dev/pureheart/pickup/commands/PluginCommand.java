package dev.pureheart.pickup.commands;

import dev.pureheart.pickup.Loader;
import dev.pureheart.pickup.utils.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public record PluginCommand(Loader plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String l, @NotNull String[] a) {
        if (!sender.hasPermission("purpickup.admin")) {
            sender.sendMessage(ColorUtil.colorize(plugin.getConfigManager().getNoPerm()));
            return true;
        }

        plugin.getConfigManager().reload();
        plugin.createListBlocks();

        sender.sendMessage(ColorUtil.colorize(plugin.getConfigManager().getReloaded()));

        return true;
    }
}
