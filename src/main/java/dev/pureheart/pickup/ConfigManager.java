package dev.pureheart.pickup;

import lombok.Getter;

import java.util.List;

@Getter
public class ConfigManager {

    private final Loader plugin;

    public ConfigManager(Loader plugin) {
        this.plugin = plugin;
        load();
    }

    private String database, noPerm, onlyPlayers, reloaded, toggledOn, toggledOff;
    private boolean autoPickupActivated;
    private List<String> whitelistBlocks;

    private void load() {
        plugin.reloadConfig();

        database = plugin.getConfig().getString("database", "database.db");
        noPerm = plugin.getConfig().getString("messages.noPerm", "<red>Недостаточно прав.");
        onlyPlayers = plugin.getConfig().getString("messages.onlyPlayers", "<red>Доступно только игрокам.");
        reloaded = plugin.getConfig().getString("messages.reloaded", "<green>Конфиги перезагружены.");
        toggledOn = plugin.getConfig().getString("messages.toggledOn", "<yellow>Автоподбор <green>включен.");
        toggledOff = plugin.getConfig().getString("messages.toggledOff", "<yellow>Автоподбор <red>выключен.");

        autoPickupActivated = plugin.getConfig().getBoolean("autoPickupActivated", false);

        whitelistBlocks = plugin.getConfig().getStringList("whitelistBlocks");
    }

    public void reload() {
        load();
    }
}
