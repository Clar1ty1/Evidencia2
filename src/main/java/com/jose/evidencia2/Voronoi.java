package com.jose.evidencia2;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Voronoi extends javax.swing.JFrame {
    BufferedImage imagen;
    int px[], py[], color[];

    public Voronoi(ArrayList<Colony> colonies ,ArrayList<Central> centrales) {
        super("Diagrama de Voronoi");
        int size = 100;
        setBounds(0, 0, size, size);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        imagen = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        px = new int[size];
        py = new int[size];
        color = new int[size];
        Random rand = new Random();

        int j = 0;
        for (Central i : centrales) {
            px[j] = i.getX();
            py[j] = i.getY();
            color[j] = rand.nextInt(16777215);
            j++;
        }
        int n;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                n = 0;
                for (int i = 0; i < size; i++) {
                    if (distance(px[i], x, py[i], y) < distance(px[n], x, py[n], y)) {
                        n = i;
                    }
                }
                imagen.setRGB(x, y, color[n]);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(imagen, 0, 0, this);
    }

    double distance(int x1, int x2, int y1, int y2) {
        double d;
        d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        return d;
    }




}


