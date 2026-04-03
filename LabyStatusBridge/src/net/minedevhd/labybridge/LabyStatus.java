package net.minedevhd.labybridge;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import net.minedevhd.labybridge.api.LabyBridgeAPI;
import net.minedevhd.labybridge.impl.LabymodBridgeAPIImpl;

public final class LabyStatus extends JavaPlugin {
	
    private LabyStatusService statusService;
    private LabyBridgeAPI api;
    private static LabyStatus instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        this.statusService = new LabyStatusService(this);
        this.api = new LabymodBridgeAPIImpl(statusService);

        Bukkit.getPluginManager().registerEvents(new LabyStatusListener(this, statusService), this);

        Bukkit.getServicesManager().register(LabyBridgeAPI.class, api, this, ServicePriority.Normal);

        startPeriodicRefresh();
    }

    @Override
    public void onDisable() {
        Bukkit.getServicesManager().unregister(LabyBridgeAPI.class, api);
        instance = null;
    }

    public static LabyStatus getInstance() {
        return instance;
    }

    public LabyStatusService getStatusService() {
        return statusService;
    }

    public LabyBridgeAPI getApi() {
        return api;
    }

    private void startPeriodicRefresh() {
        boolean enabled = getConfig().getBoolean("update.periodic-refresh.enabled", true);
        if (!enabled) return;

        long ticks = getConfig().getLong("update.periodic-refresh.ticks", 100L);
        if (ticks < 20L) ticks = 20L;

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                api.updatePlayer(player);
            }
        }, ticks, ticks);
    }
}