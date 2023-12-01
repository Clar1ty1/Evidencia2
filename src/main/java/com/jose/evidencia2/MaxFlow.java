package com.jose.evidencia2;

import javafx.stage.Stage;

import java.util.ArrayList;

public class MaxFlow {

    int [][] CapacityMatrix;

    public MaxFlow(){

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
