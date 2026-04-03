package net.minedevhd.labybridge.impl;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.minedevhd.labybridge.LabyStatusService;
import net.minedevhd.labybridge.api.LabyBridgeAPI;
import net.minedevhd.labybridge.api.StatusUpdate;

public final class LabymodBridgeAPIImpl implements LabyBridgeAPI {

    private final LabyStatusService service;
    private final Map<UUID, StatusUpdate> temporaryStatuses = new ConcurrentHashMap<>();

    public LabymodBridgeAPIImpl(LabyStatusService service) {
        this.service = service;
    }

    @Override
    public void updatePlayer(Player player) {
        service.updatePlayer(player);
    }

    @Override
    public void updateAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            service.updatePlayer(player);
        }
    }

    @Override
    public void updateMinigameJoin(Player player, String gameModeName, String discordText) {
        setTemporaryStatus(player, StatusUpdate.withStart(gameModeName, discordText));
        service.updatePlayer(player);
    }

    @Override
    public void setTemporaryStatus(Player player, StatusUpdate update) {
        if (player == null || update == null) {
            return;
        }
        temporaryStatuses.put(player.getUniqueId(), update);
    }

    @Override
    public void clearTemporaryStatus(Player player) {
        if (player == null) {
            return;
        }
        temporaryStatuses.remove(player.getUniqueId());
    }

    @Override
    public StatusUpdate resolveStatus(Player player) {
        if (player == null) {
            return null;
        }
        StatusUpdate temporary = temporaryStatuses.get(player.getUniqueId());
        if (temporary != null) {
            return temporary;
        }
        return service.resolveConfigStatus(player);
    }

    @Override
    public boolean isLabyModPlayer(Player player) {
        return service.getLabyPlayer(player.getUniqueId()) != null;
    }
}