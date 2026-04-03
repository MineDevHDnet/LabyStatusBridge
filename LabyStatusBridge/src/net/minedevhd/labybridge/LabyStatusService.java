package net.minedevhd.labybridge;

import java.util.Locale;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.labymod.serverapi.core.model.feature.DiscordRPC;
import net.labymod.serverapi.server.bukkit.LabyModPlayer;
import net.labymod.serverapi.server.bukkit.LabyModProtocolService;
import net.minedevhd.labybridge.api.StatusUpdate;

public final class LabyStatusService {

    private final LabyStatus plugin;

    public LabyStatusService(LabyStatus plugin) {
        this.plugin = plugin;
    }

    public void updatePlayer(Player player) {
        LabyModPlayer labyPlayer = getLabyPlayer(player.getUniqueId());
        if (labyPlayer == null) return;

        StatusUpdate status = plugin.getApi().resolveStatus(player);
        if (status == null) return;

        applyStatus(labyPlayer, status);
    }

    public StatusUpdate resolveConfigStatus(Player player) {
        String worldName = player.getWorld().getName();
        FileConfiguration config = plugin.getConfig();

        ConfigurationSection defaults = config.getConfigurationSection("defaults");
        ConfigurationSection worldSection = config.getConfigurationSection("worlds." + worldName);

        String gameMode = color(getString(worldSection, defaults, "gamemode", "Lobby"));
        boolean discordEnabled = getBoolean(worldSection, defaults, "discord-rpc.enabled", true);
        String discordMode = getString(worldSection, defaults, "discord-rpc.mode", "simple").toLowerCase(Locale.ROOT);
        String discordText = color(getString(worldSection, defaults, "discord-rpc.text", "Lobby"));
        long offsetSeconds = getLong(worldSection, defaults, "discord-rpc.offset-seconds", 0L);

        return new StatusUpdate(gameMode, discordEnabled, discordMode, discordText, offsetSeconds);
    }

    public void applyStatus(LabyModPlayer labyPlayer, StatusUpdate status) {
        String gameMode = color(status.getGameMode());

        if (gameMode.isBlank()) {
            labyPlayer.sendPlayingGameMode(null);
        } else {
            labyPlayer.sendPlayingGameMode(gameMode);
        }

        if (!status.isDiscordEnabled()) {
            labyPlayer.sendDiscordRPC(DiscordRPC.createReset());
            return;
        }

        String mode = status.getDiscordMode() == null ? "simple" : status.getDiscordMode().toLowerCase(Locale.ROOT);
        String text = color(status.getDiscordText());
        long time = System.currentTimeMillis() + (status.getDiscordOffsetSeconds() * 1000L);

        DiscordRPC rpc;

        switch (mode) {
            case "with_start":
                rpc = DiscordRPC.createWithStart(text, time);
                break;
            case "with_end":
                rpc = DiscordRPC.createWithEnd(text, time);
                break;
            case "reset":
                rpc = DiscordRPC.createReset();
                break;
            case "simple":
            default:
                rpc = DiscordRPC.create(text);
                break;
        }

        labyPlayer.sendDiscordRPC(rpc);
    }

    public LabyModPlayer getLabyPlayer(UUID uniqueId) {
        try {
            return LabyModProtocolService.get().getPlayer(uniqueId);
        } catch (Exception exception) {
            return null;
        }
    }

    private String getString(ConfigurationSection worldSection, ConfigurationSection defaults, String path, String def) {
        if (worldSection != null && worldSection.contains(path)) return worldSection.getString(path, def);
        if (defaults != null && defaults.contains(path)) return defaults.getString(path, def);
        return def;
    }

    private boolean getBoolean(ConfigurationSection worldSection, ConfigurationSection defaults, String path, boolean def) {
        if (worldSection != null && worldSection.contains(path)) return worldSection.getBoolean(path, def);
        if (defaults != null && defaults.contains(path)) return defaults.getBoolean(path, def);
        return def;
    }

    private long getLong(ConfigurationSection worldSection, ConfigurationSection defaults, String path, long def) {
        if (worldSection != null && worldSection.contains(path)) return worldSection.getLong(path, def);
        if (defaults != null && defaults.contains(path)) return defaults.getLong(path, def);
        return def;
    }

    private String color(String text) {
        if (text == null) return "";
        return text.replace('&', '§');
    }
}