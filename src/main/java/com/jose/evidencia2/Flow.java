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

public class Flow {

    int [][] CapacityMatrix;

    public Flow(){

    }

    public int getLinks(ArrayList<Link> linkList){
        for (Link currentlink : linkList ){
            currentlink.getCapacity();
        }

        return 0;
    }

    public void start(Stage stage){

    }

}
