package com.larskrs.plugins.mapmaker3000;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mapmaker3000 extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("download").setExecutor(new downloadCommand(this));
        Bukkit.getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    @EventHandler
    public void onMapLoad (MapInitializeEvent e) {
        MapView view = e.getMap();

        view.getRenderers().clear();


    }
}
