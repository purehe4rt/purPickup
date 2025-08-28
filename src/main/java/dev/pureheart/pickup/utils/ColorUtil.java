package dev.pureheart.pickup.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ColorUtil {

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static Component colorize(String text) {
        return miniMessage.deserialize(text);
    }
}
