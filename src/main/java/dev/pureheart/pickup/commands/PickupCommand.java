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
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            String onlyPlayers = plugin.getConfig().getString("messages.onlyPlayers", "<red>Сообщение не найдено :c");
            commandSender.sendMessage(ColorUtil.colorize(onlyPlayers));
            return true;
        }

        UUID uuid = ((Player) commandSender).getUniqueId();

        boolean current = plugin.getDatabase().getEnabled(uuid);
        boolean switchEnabled = !current;

        plugin.getDatabase().addPlayer(uuid, switchEnabled);

        if (switchEnabled) {
            String toggledOn = plugin.getConfig().getString("messages.toggledOn", "<red>Сообщение не найдено :c");
            commandSender.sendMessage(ColorUtil.colorize(toggledOn));
        } else {
            String toggledOff = plugin.getConfig().getString("messages.toggledOff", "<red>Сообщение не найдено :c");
            commandSender.sendMessage(ColorUtil.colorize(toggledOff));
        }

        return true;
    }
}
