package net.minedevhd.labybridge.api;

public final class WorldStatus {

    private final String worldName;
    private final StatusUpdate statusUpdate;

    public WorldStatus(String worldName, StatusUpdate statusUpdate) {
        this.worldName = worldName;
        this.statusUpdate = statusUpdate;
    }

    public String getWorldName() {
        return worldName;
    }

    public StatusUpdate getStatusUpdate() {
        return statusUpdate;
    }
}