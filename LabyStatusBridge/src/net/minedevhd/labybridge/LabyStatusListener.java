package net.minedevhd.labybridge;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public final class LabyStatusListener implements Listener {

    private final LabyStatus plugin;
    private final LabyStatusService statusService;

    public LabyStatusListener(LabyStatus plugin, LabyStatusService statusService) {
        this.plugin = plugin;
        this.statusService = statusService;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        if (!plugin.getConfig().getBoolean("update.on-join", true)) {
            return;
        }
        statusService.updatePlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        if (!plugin.getConfig().getBoolean("update.on-world-change", true)) {
            return;
        }
        statusService.updatePlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        if (!plugin.getConfig().getBoolean("update.on-teleport", true)) {
            return;
        }

        // 1 Tick später, damit die Welt/Position sicher aktuell ist
        plugin.getServer().getScheduler().runTask(plugin, () -> statusService.updatePlayer(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawn(PlayerRespawnEvent event) {
        if (!plugin.getConfig().getBoolean("update.on-respawn", true)) {
            return;
        }

        plugin.getServer().getScheduler().runTask(plugin, () -> statusService.updatePlayer(event.getPlayer()));
    }
}