package com.jose.evidencia2;


import java.util.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

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
    Map<String, Integer> map = new HashMap<String, Integer>();
    Group mainGroup;
    Stage stage;
    double SCREEN_HEIGHT = 800;
    double SCREEN_WIDTH = 1200;
    double BUTTON_WIDTH = 120;
    double CANVAS_WIDTH = SCREEN_WIDTH / 4;
    double canvasSpacing = (SCREEN_WIDTH - CANVAS_WIDTH * 4) / 5;
    double spacing = (SCREEN_WIDTH - BUTTON_WIDTH * 4) / 5;
    double[][] adjacencyMatrix;
    double[][] capacityMatrix;


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
     
        mainGroup = new Group();
        
        mainGroup.getChildren().addAll(loadGraphButton, addColony, deleteColony, saveGraph, label1);

        Scene scene = new Scene(mainGroup, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        stage.setScene(scene);
        
        stage.show();
    }
    /**
     * Loads a graph from a file.
     * Has complexity O(n)..
     * @return None    No return value.
     */
    private void loadGraph() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir archivo");
        File file = fileChooser.showOpenDialog(stage);
        System.out.println(file.getAbsolutePath());
        float divider = 1.25f;
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
            int i = 0;
            for( JsonElement colony : colonies.getAsJsonArray()) {
                String name = colony.getAsJsonObject().get("nombre").getAsString();
                int x = (int) (colony.getAsJsonObject().get("coordenadaX").getAsInt()/divider);
                int y = (int) (colony.getAsJsonObject().get("coordenadaY").getAsInt()/divider);
                map.put(name, i);
                this.colonies.add( new Colony(name, x, y));

            }

            for( JsonElement link : links.getAsJsonArray()) {
                String colonyBegin = link.getAsJsonObject().get("coloniaInicial").getAsString();
                String colonyEnd = link.getAsJsonObject().get("coloniaFinal").getAsString();
                int distance = (int) (link.getAsJsonObject().get("distancia").getAsInt()/divider);
                int capacity = (int) (link.getAsJsonObject().get("capacidad").getAsInt()/divider);
                this.links.add( new Link(colonyBegin,colonyEnd,distance,capacity) );

            }

            for( JsonElement central : centrals.getAsJsonArray()) {

                int x = (int) (central.getAsJsonObject().get("x").getAsInt()/divider);
                int y = (int) (central.getAsJsonObject().get("y").getAsInt()/divider);
                this.centrals.add( new Central( x, y ));
            }
            this.adjacencyMatrix = new double[this.colonies.size()][this.colonies.size()];
            TreeSpanning tsp = new TreeSpanning();
            tsp.solveTsp(this.colonies);

            drawGraph();

        }
        catch (IOException ex) {
            System.out.println(ex);
        }
    }

    private void drawGraph() {
        int radius = 6;
        int translateX = 100;
        int translateY = 150;

        for (Colony colony : colonies) {
            Circle circle = new Circle();
            circle.setCenterX(colony.getX() + translateX);
            circle.setCenterY(colony.getY() + translateY);
            circle.setRadius(radius);
            circle.setFill(Color.BLUE);
            Text text = new Text();

            text.setX(colony.getX() + translateX);
            text.setY(colony.getY() + translateY - 10);

            text.setText(String.valueOf(colony.getName()));

            mainGroup.getChildren().addAll(text, circle);
        }

        for (Central central : centrals) {
            Circle circle = new Circle();
            circle.setCenterX(central.getX() + translateX);
            circle.setCenterY(central.getY() + translateY);
            circle.setRadius(radius);
            circle.setFill(Color.RED);
            mainGroup.getChildren().add(circle);
        }
        int i = 0;
        int j = 0;
        for (Link link : links) {
            String begin = link.getColonyBegin();
            String end = link.getColonyEnd();
            int x, y, xf, yf;
            for (Colony colony : colonies) {
                if (begin.equals(colony.getName())) {
                    x = colony.getX() + translateX;
                    y = colony.getY() + translateY;
                    this.adjacencyMatrix[i][j] = link.getDistance();
                    this.capacityMatrix[i][j] = link.getCapacity();
                }
                if (end.equals(colony.getName())) {
                    xf = colony.getX() + translateX;
                    yf = colony.getY() + translateY;
                    this.adjacencyMatrix[i][j] = link.getDistance();
                    this.capacityMatrix[i][j] = link.getCapacity();
                }
                    j++;

            }
            i++;

        }
        printMatrix(this.adjacencyMatrix);
        printMatrix(this.capacityMatrix);


    }

    private void printMatrix(double[][] matrix) {
        for( double[] row : matrix) {
            for( double element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}


;

