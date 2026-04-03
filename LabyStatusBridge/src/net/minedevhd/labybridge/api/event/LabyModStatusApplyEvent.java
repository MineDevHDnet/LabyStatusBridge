package net.minedevhd.labybridge.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.minedevhd.labybridge.api.StatusUpdate;

public final class LabyModStatusApplyEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final StatusUpdate statusUpdate;

    public LabyModStatusApplyEvent(Player player, StatusUpdate statusUpdate) {
        this.player = player;
        this.statusUpdate = statusUpdate;
    }

    public Player getPlayer() {
        return player;
    }

    public StatusUpdate getStatusUpdate() {
        return statusUpdate;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}