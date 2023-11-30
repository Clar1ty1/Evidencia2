package com.jose.evidencia2;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class Voronoi {
    private WritableImage image;
    private int[] px, py, color;

    ArrayList<Colony> colonies;
    ArrayList<Central> centrals;

    public Voronoi(int height, int width, ArrayList<Colony> colonies, ArrayList<Central> centrals) {
        this.centrals = centrals;
        this.colonies = colonies;

        int zonas = centrals.size();

        image = new WritableImage(width, height);

        px = new int[zonas];
        py = new int[zonas];
        color = new int[zonas];

        Random rand = new Random();

        int j = 0;
        for (Central central : centrals) {
            px[j] = central.getX();
            py[j] = central.getY();
            color[j] = rand.nextInt(16777215);
            j++;
        }

        int n;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                n = 0;
                for (int i = 0; i < zonas; i++) {
                    if (distance(px[i], x, py[i], y) < distance(px[n], x, py[n], y)) {
                        n = i;
                    }
                }
                image.getPixelWriter().setColor(x, y, Color.rgb((color[n] >> 16) & 0xFF, (color[n] >> 8) & 0xFF, color[n] & 0xFF));
            }
        }
    }

    public Group getVoronoi(Stage stage) {
        Canvas canvas = new Canvas(image.getWidth(), image.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(image, 0, 0);
        gc.setFill(Color.BLUE);
        for (Colony colony : colonies ) {
            gc.fillOval(colony.getX(), colony.getY(), 8, 8);
        }
        gc.setFill(Color.RED);
        for( Central central: centrals ) {
            gc.fillOval(central.getX(), central.getY(), 8, 8);
        }

        Group voronoiGroup = new Group(canvas);

        stage.setScene(new Scene(voronoiGroup));
        stage.show();

        return voronoiGroup;
    }


    private double distance(int x1, int x2, int y1, int y2) {
        double d;
        d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        return d;
    }
}
