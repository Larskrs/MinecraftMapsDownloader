package com.larskrs.plugins.mapmaker3000;

import org.asynchttpclient.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import sun.tools.jstat.Scale;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class downloadCommand implements CommandExecutor {

    private Mapmaker3000 mapmaker3000;

    public downloadCommand (Mapmaker3000 mapmaker3000) {
        this.mapmaker3000 = mapmaker3000;

        mapmaker3000.getConfig().options().copyDefaults(true);
        mapmaker3000.saveDefaultConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
        Player p = (Player) sender;
        String url = args[0];
        String filename = "image.jpg";

            if(args.length >= 1) {
                File pluginsDirectory = mapmaker3000.getDataFolder();
                try {
                    download(url, new File(pluginsDirectory, filename));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int mapId = Bukkit.createMap(p.getWorld()).getId();
                ItemStack is = new ItemStack(Material.FILLED_MAP);
                MapMeta meta = (MapMeta) is.getItemMeta();
                meta.setMapId(mapId);
                is.setItemMeta(meta);



                MapView view = Bukkit.getMap(((MapMeta) is.getItemMeta()).getMapId());

                view.setUnlimitedTracking(true);
                view.setScale(MapView.Scale.FARTHEST);

                for (MapRenderer m : view.getRenderers()) {
                    view.removeRenderer(m);
                }

                view.getRenderers().clear();


                try {
                    view.addRenderer(new renderer(url, new File(pluginsDirectory, filename)));
                p   .getInventory().addItem(is);
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                }







            } else {
            p.sendMessage("Incorrect Usage! /download <link> <name>");
        }

        }


        return false;
    }


    public File download(String url, File file) throws IOException {
        InputStream ura;
        ura = new URL(url).openStream();
        java.nio.file.Files.copy(ura, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

        try {
            if( ura!=null ) {
                ura.close();
                System.out.println("closed input stream!");
            }
        } catch(IOException e) {
        }

        return file;
    }


}
