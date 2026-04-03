package net.minedevhd.labybridge.api;

import org.bukkit.entity.Player;

public interface LabyBridgeAPI {

    void updatePlayer(Player player);

    void updateAll();

    void updateMinigameJoin(Player player, String gameModeName, String discordText);

    void setTemporaryStatus(Player player, StatusUpdate update);

    void clearTemporaryStatus(Player player);

    StatusUpdate resolveStatus(Player player);

    boolean isLabyModPlayer(Player player);
}