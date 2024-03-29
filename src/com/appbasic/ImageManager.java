/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appbasic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import org.balx.ColorDg;
import static org.dz.Imagenes.partirCadena;
import sun.font.FontLineMetrics;

/**
 *
 * @author ballestax
 */
public class ImageManager {

    private HashMap<String, Image> images;

    private ImageManager() {
        images = new HashMap<>();
    }

    public static ImageManager getInstance() {
        return ImageManagerHolder.INSTANCE;
    }

    private static class ImageManagerHolder {

        private static final ImageManager INSTANCE = new ImageManager();
    }

    public Image getImagen(String key) {
        if (images.containsKey(key)) {
            return images.get(key);
        } else {
            Image img = org.balx.Resources.getImagen(key, this.getClass());
            images.put(key, img);
            return img;
        }
    }

    public Image getImagen(String key, int w, int h) {
        if (images.containsKey(key)) {
            Image img = images.get(key);
            return img.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING);
        } else {
//            Image img = Toolkit.getDefaultToolkit().getImage(clas.getResource(key));
            Image img;
            try {
                img = org.balx.Resources.getImagen(key, this.getClass());
            } catch (Exception e) {
                img = org.balx.Imagenes.centrarTexto(w, h, "img", new Font("Arial", 1, 12), Color.red, Color.gray);
            }

            images.put(key, img);
            return img.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING);
        }
    }

    public BufferedImage getBufImagen(String key) {
        if (images.containsKey(key)) {
            return org.bx.Imagenes.toBuffereredImage(images.get(key));
        } else {
            BufferedImage img = org.dzur.Resources.cargarImagen(key);
            images.put(key, img);
            return org.bx.Imagenes.toBuffereredImage(img);
        }
    }

    public BufferedImage getBufImagen(String key, int w, int h) {
        if (images.containsKey(key)) {
            Image img = images.get(key);
            return org.bx.Imagenes.toBuffereredImage(img.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING));
        } else {
            BufferedImage img = org.dzur.Resources.cargarImagen(key);
            images.put(key, img);
            return org.bx.Imagenes.toBuffereredImage(img.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING));
        }
    }

    public static Image pintarCirculos(int d, int t) {
        int offset = t;
        Image imagen = new BufferedImage(d + offset, d + offset, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) imagen.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int thickness = t;
        int innerSize = d - (2 * thickness);

        Shape outer = new Ellipse2D.Double(0, 0, d, d);
        Shape inner = new Ellipse2D.Double(thickness, thickness, innerSize, innerSize);

        Area circle = new Area(outer);
        circle.subtract(new Area(inner));

        int x = offset / 2;
        int y = offset / 2;
        g.translate(x, y);

        g.setColor(ColorDg.colorAleatorio().getColor1());
        g.fill(circle);
//        g.setColor(Color.BLACK);
//        g.draw(circle);

        g.dispose();
        return imagen;
    }

    public static Image centrarTextoLim(int w, int h, String s, Font f, Color fondo, Color letra, int espacio, int maximo, boolean shading) {
        Image imagen = new BufferedImage(w, h, 2);
        Graphics2D g = (Graphics2D) imagen.getGraphics();
        String linea[] = null;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(f);
        java.awt.font.FontRenderContext frc = g.getFontRenderContext();
        g.setColor(fondo);
        g.fillRect(0, 0, w, h);
        linea = partirCadena(s, f, maximo, 12);
        int largo = w * 1;
        int alto = h * 1;
        int mx = w - largo;
        int my = h - alto;
        for (int i = 0; i < linea.length; i++) {
            linea[i] = org.balx.Utiles.eliminarAcentos(linea[i]);
            FontLineMetrics lm = (FontLineMetrics) g.getFont().getLineMetrics(linea[i], frc);
            Rectangle2D bounds = g.getFont().getStringBounds(linea[i], frc);
            float largoCadena = (float) bounds.getWidth();
            float alturaCadena = (float) bounds.getHeight() - lm.getLeading();
            if (linea.length == 1) {
                espacio *= -1;
            }
            if (shading) {
                g.setColor(Color.black);
                g.drawString(linea[i], (float) mx + ((float) (largo / 2) - largoCadena / 2.0F), (int) ((float) (my + espacio * (i + 1)) + alturaCadena * (float) (i + 1)));
            }
            g.setColor(letra);
            g.drawString(linea[i], (float) mx + ((float) (largo / 2) - largoCadena / 2.0F) + 1.0F, (int) ((float) (my + espacio * (i + 1)) + alturaCadena * (float) (i + 1)) + 1);
        }
        return imagen;
    }

}
