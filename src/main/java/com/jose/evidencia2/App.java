package com.jose.evidencia2;


import java.util.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    int SCREEN_HEIGHT = 800;
    int SCREEN_WIDTH = 1200;
    double BUTTON_WIDTH = 120;
    double CANVAS_WIDTH = SCREEN_WIDTH / 4;
    double canvasSpacing = (SCREEN_WIDTH - CANVAS_WIDTH * 4) / 5;
    double spacing = (SCREEN_WIDTH - BUTTON_WIDTH * 4) / 5;
    int[][] adjacencyMatrix;
    int[][] capacityMatrix;


    @Override
    public void start(Stage stage) {
        this.stage = stage;
        
        this.stage.setTitle("Evidencia 2");
        
        Label label1 = new Label("Seleccione una opción para comenzar");
        label1.setTranslateX(SCREEN_WIDTH/2 - 100);
        
                
        Button loadGraphButton = new Button("Cargar grafo");
        loadGraphButton.setMaxWidth(BUTTON_WIDTH);
        loadGraphButton.setMinWidth(BUTTON_WIDTH);

        Button loadVoronoiDiagram = new Button("Diagrama Voronoi");
        loadVoronoiDiagram.setMaxWidth(BUTTON_WIDTH);
        loadVoronoiDiagram.setMinWidth(BUTTON_WIDTH);

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
        loadVoronoiDiagram.setTranslateX(pos);
        loadVoronoiDiagram.setTranslateY(50);
        loadVoronoiDiagram.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                drawVoronoi();
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
        
        mainGroup.getChildren().addAll(loadGraphButton,loadVoronoiDiagram,  addColony, deleteColony, saveGraph, label1);

        Scene scene = new Scene(mainGroup, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        stage.setScene(scene);
        
        stage.show();
    }
    /**
     * Loads a graph from a file.
     * Has complexity O(n).
     * @param None  No parameters are required.
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
                i++;
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
            this.adjacencyMatrix = new int[this.colonies.size()+1][this.colonies.size()+1];
            this.capacityMatrix = new int[this.colonies.size()+1][this.colonies.size()+1];
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
            int index1 = map.get(begin);
            int index2 = map.get(end);

            this.adjacencyMatrix[index1][index2] = link.getDistance();
            this.capacityMatrix[index1][index2] = link.getCapacity();

        }
        printMatrix(this.adjacencyMatrix);
        System.out.println();
        printMatrix(this.capacityMatrix);


    }

    private void drawVoronoi() {
        Voronoi voronoi = new Voronoi(SCREEN_HEIGHT-10, SCREEN_WIDTH -20, colonies,   centrals);
        Canvas canvas =  voronoi.getVoronoi();
        canvas.setTranslateY(100);
        canvas.setTranslateX(10);
        mainGroup.getChildren().add(canvas);
    }
    private void printMatrix(int[][] matrix) {
        for( int[] row : matrix) {
            for( int element : row) {
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

