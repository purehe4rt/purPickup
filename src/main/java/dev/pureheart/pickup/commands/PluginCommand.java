package dev.pureheart.pickup.commands;

import dev.pureheart.pickup.Loader;
import dev.pureheart.pickup.utils.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public record PluginCommand(Loader plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission("purpickup.admin")) {
            String noPerm = plugin.getConfig().getString("messages.noPerm", "<red>Сообщение не найдено :c");
            commandSender.sendMessage(ColorUtil.colorize(noPerm));
            return true;
        }

        if (strings.length == 0) {
            String help = plugin.getConfig().getString("messages.help", "<red>Сообщение не найдено :c");
            commandSender.sendMessage(ColorUtil.colorize(help));
            return true;
        }

        if (strings[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            plugin.createListBlocks();

            String reloaded = plugin.getConfig().getString("messages.reloaded", "<red>Сообщение не найдено :c");
            commandSender.sendMessage(ColorUtil.colorize(reloaded));
        } else {
            String noArg = plugin.getConfig().getString("messages.noArg", "<red>Сообщение не найдено :c");
            commandSender.sendMessage(ColorUtil.colorize(noArg));
        }

        return true;
    }
}
