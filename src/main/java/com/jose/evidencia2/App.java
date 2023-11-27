package com.jose.evidencia2;


import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * JavaFX App
 */
public class App extends Application {
    ArrayList<Colony> colonies = new ArrayList<Colony>();
    ArrayList<Central> centrals = new ArrayList<Central>();
    ArrayList<Link> links = new ArrayList<Link>();
    Stage stage;
    double SCREEN_HEIGHT = 500;
    double SCREEN_WIDTH = 800;
    double BUTTON_WIDTH = 120;
    
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        
        this.stage.setTitle("Evidencia 2");
        
        Label label1 = new Label("Seleccione una opción para comenzar");
        label1.setTranslateX(SCREEN_WIDTH/2 - 100);
        
                
        Button loadGraphButton = new Button("Cargar grafo");
        loadGraphButton.setMaxWidth(BUTTON_WIDTH);
        loadGraphButton.setMinWidth(BUTTON_WIDTH);
        
        Button addColony = new Button("Añadir colonia");        
        addColony.setMaxWidth(BUTTON_WIDTH);
        addColony.setMinWidth(BUTTON_WIDTH);
        
        Button deleteColony = new Button("Eliminar colonia");
        deleteColony.setMaxWidth(BUTTON_WIDTH);
        deleteColony.setMinWidth(BUTTON_WIDTH);
        
        Button saveGraph = new Button("Guardar grafo");
        saveGraph.setMaxWidth(BUTTON_WIDTH);
        saveGraph.setMinWidth(BUTTON_WIDTH);
        
        double spacing = (SCREEN_WIDTH - BUTTON_WIDTH * 4) / 5; 
        
        double pos = 0;
        
        pos = pos + spacing;
        loadGraphButton.setTranslateX(pos);
        loadGraphButton.setTranslateY(50);
        loadGraphButton.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadGraph();
            }
        });
        
        pos = pos + BUTTON_WIDTH + spacing;
        addColony.setTranslateX(pos);
        addColony.setTranslateY(50);
        
        
        pos = pos + BUTTON_WIDTH + spacing;
        deleteColony.setTranslateX(pos);
        deleteColony.setTranslateY(50);
        
        
        pos = pos + BUTTON_WIDTH + spacing;
        saveGraph.setTranslateX(pos);
        saveGraph.setTranslateY(50);
     
        Group group = new Group();
        
        group.getChildren().addAll(loadGraphButton, addColony, deleteColony, saveGraph, label1);

        Scene scene = new Scene(group, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        stage.setScene(scene);
        
        stage.show();
    }
    /**
     * Loads a graph from a file.
     * Has complexity O(n).
     * @param  None    No parameters are required.
     * @return None    No return value.
     */
    private void loadGraph() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir archivo");
        File file = fileChooser.showOpenDialog(stage);
        System.out.println(file.getAbsolutePath());
        try {
            Scanner reader = new Scanner(file); // Preparamos el lector de archivos
            JsonParser parser = new JsonParser();

            String data = "";
            while (reader.hasNextLine()) {
                data += reader.nextLine().toString(); // Guardamos todo el contenido en un string

            }
            JsonObject gsonObj = parser.parse(data).getAsJsonObject(); // Lo convertimos en un objeto
            JsonElement cities = gsonObj.get("ciudad");
            JsonElement colonies = cities.getAsJsonObject().get("colonias");
            JsonElement links = cities.getAsJsonObject().get("enlaces");
            JsonElement centrals = cities.getAsJsonObject().get("centrales");

            for( JsonElement colony : colonies.getAsJsonArray()) {
                String name = colony.getAsJsonObject().get("nombre").getAsString();
                int x = colony.getAsJsonObject().get("coordenadaX").getAsInt();
                int y = colony.getAsJsonObject().get("coordenadaY").getAsInt();
                this.colonies.add( new Colony(name, x, y));

            }

            for( JsonElement link : links.getAsJsonArray()) {
                String colonyBegin = link.getAsJsonObject().get("coloniaInicial").getAsString();
                String colonyEnd = link.getAsJsonObject().get("coloniaFinal").getAsString();
                double distance = link.getAsJsonObject().get("distancia").getAsDouble();
                double capacity = link.getAsJsonObject().get("capacidad").getAsDouble();
                this.links.add( new Link(colonyBegin,colonyEnd,distance,capacity) );
            }

            for( JsonElement central : centrals.getAsJsonArray()) {

                int x = central.getAsJsonObject().get("x").getAsInt();
                int y = central.getAsJsonObject().get("y").getAsInt();
                this.centrals.add( new Central( x, y ));
            }

            System.out.println(this.colonies.size());
            System.out.println(this.links.size());
            System.out.println(this.centrals.size());
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public static void main(String[] args) {
        launch();
    }

}


;

