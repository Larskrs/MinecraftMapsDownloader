package com.larskrs.plugins.mapmaker3000;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class renderer extends MapRenderer {

    private BufferedImage image;
    private Mapmaker3000 mapMaker3000;
    private String read;
    private File file;

    private SoftReference<BufferedImage> cacheImage;
    private boolean hasRendered = false;


    public renderer(String read, File file) throws IOException {
        this.read = read;
        this.file = file;

    }
    @Override
    public void render(MapView view, MapCanvas canvas, Player player) {
        if (this.hasRendered) {


            return;
        }

        BufferedImage image = null;

        this.image = image;

        try{
            // scale image on disk
            BufferedImage originalImage = ImageIO.read(new URL(read));


            int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB
                    : originalImage.getType();

            BufferedImage resizeImageJpg = resizeImage(originalImage, type, 128, 128);
            ImageIO.write(resizeImageJpg, "jpg", file);

            canvas.drawImage(0, 0, resizeImageJpg);
            view.setUnlimitedTracking(false);
            this.hasRendered = true;


        } catch(IOException | NullPointerException e) {
            System.out.println(e.getMessage());
            this.hasRendered = true;
            player.sendMessage("§cThere was en error loading your image, please try a different one!");
        }


    }
    private static BufferedImage resizeImage(BufferedImage originalImage, int type,
                                             Integer img_width, Integer img_height)
    {
        BufferedImage resizedImage = new BufferedImage(img_width, img_height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, img_width, img_height, null);
        g.dispose();

        return resizedImage;
    }

}
