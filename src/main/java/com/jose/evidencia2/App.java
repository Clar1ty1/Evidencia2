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

    double spacing = (SCREEN_WIDTH - BUTTON_WIDTH * 6) / 7;
    int[][] adjacencyMatrix;
    int[][] capacityMatrix;

    Button loadGraphButton = new Button("Cargar grafo");
    Button loadVoronoiDiagram = new Button("Diagrama Voronoi");
    Button addColonyButton = new Button("Añadir colonia");
    Button deleteColonyButton = new Button("Eliminar colonia");
    Button saveGraphButton = new Button("Guardar grafo");
    Button shortestPathButton = new Button("Ruta mas corta");

    TreeSpanning tsp = new TreeSpanning();



    @Override
    public void start(Stage stage) {
        this.stage = stage;
        
        this.stage.setTitle("Evidencia 2");
        
        Label label1 = new Label("Seleccione una opción para comenzar");
        label1.setTranslateX(SCREEN_WIDTH/2 - 100);

        loadGraphButton.setMaxWidth(BUTTON_WIDTH);
        loadGraphButton.setMinWidth(BUTTON_WIDTH);

        loadVoronoiDiagram.setMaxWidth(BUTTON_WIDTH);
        loadVoronoiDiagram.setMinWidth(BUTTON_WIDTH);

        addColonyButton.setMaxWidth(BUTTON_WIDTH);
        addColonyButton.setMinWidth(BUTTON_WIDTH);

        deleteColonyButton.setMaxWidth(BUTTON_WIDTH);
        deleteColonyButton.setMinWidth(BUTTON_WIDTH);

        saveGraphButton.setMaxWidth(BUTTON_WIDTH);
        saveGraphButton.setMinWidth(BUTTON_WIDTH);

        shortestPathButton.setMaxWidth(BUTTON_WIDTH);
        shortestPathButton.setMinWidth(BUTTON_WIDTH);

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
        addColonyButton.setTranslateX(pos);
        addColonyButton.setTranslateY(50);
        
        
        pos = pos + BUTTON_WIDTH + spacing;
        deleteColonyButton.setTranslateX(pos);
        deleteColonyButton.setTranslateY(50);
        
        
        pos = pos + BUTTON_WIDTH + spacing;
        saveGraphButton.setTranslateX(pos);
        saveGraphButton.setTranslateY(50);

        pos = pos + BUTTON_WIDTH + spacing;
        shortestPathButton.setTranslateX(pos);
        shortestPathButton.setTranslateY(50);
        shortestPathButton.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tsp.solveTsp(colonies);
                drawShortestPath();

            }
        });
     
        mainGroup = new Group();
        
        mainGroup.getChildren().addAll(loadGraphButton,loadVoronoiDiagram, addColonyButton, deleteColonyButton, saveGraphButton, label1, shortestPathButton);

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


            drawGraph();

        }
        catch (IOException ex) {
            System.out.println(ex);
        }
    }

    private void drawGraph() {
        int radius = 10;
        int translateX = 100;
        int translateY = 150;
        Group newGroup = new Group();

        for (Central central : centrals) {
            Circle circle = new Circle();
            circle.setCenterX(central.getX() + translateX);
            circle.setCenterY(central.getY() + translateY);
            circle.setRadius(radius);
            circle.setFill(Color.RED);
            newGroup.getChildren().add(circle);
        }

        for (Link link : links) {
            String begin = link.getColonyBegin();
            String end = link.getColonyEnd();
            int x, y, xf, yf;
            int index1 = map.get(begin);
            int index2 = map.get(end);

            this.adjacencyMatrix[index1][index2] = link.getDistance();
            this.capacityMatrix[index1][index2] = link.getCapacity();

        }
        int[][] aux = this.adjacencyMatrix;
        for( int i = 0; i < this.adjacencyMatrix.length; i++) {
            for( int j = 0; j < this.adjacencyMatrix.length; j++) {
                if( this.adjacencyMatrix[i][j] != 0) {
                    if( this.adjacencyMatrix[i][j] == this.adjacencyMatrix[j][i] && aux[i][j] != -1 && aux[j][i] != -1) {
                        Line line = new Line();
                        line.setStartX(colonies.get(i).getX() + translateX - 6 );
                        line.setStartY(colonies.get(i).getY() + translateY - 6 );
                        line.setEndX(colonies.get(j).getX() + translateX - 6);
                        line.setEndY(colonies.get(j).getY() + translateY - 6);
                        line.setStroke(Color.BLUE);
                        line.setStrokeWidth(3);

                        Line line2 = new Line();
                        line2.setStartX(colonies.get(i).getX() + translateX + 6 );
                        line2.setStartY(colonies.get(i).getY() + translateY + 6);
                        line2.setEndX(colonies.get(j).getX() + translateX + 6);
                        line2.setEndY(colonies.get(j).getY() + translateY + 6);
                        line2.setStroke(Color.RED);
                        line2.setStrokeWidth(3);
                        aux[i][j] = -1;
                        aux[j][i] = -1;
                        newGroup.getChildren().addAll(line, line2);
                    }
                    else if(aux[i][j] != -1){
                        Line line = new Line();
                        line.setStartX(colonies.get(i).getX() + translateX  );
                        line.setStartY(colonies.get(i).getY() + translateY  );
                        line.setEndX(colonies.get(j).getX() + translateX );
                        line.setEndY(colonies.get(j).getY() + translateY );
                        line.setStroke(Color.GREEN);
                        line.setStrokeWidth(3);
                        newGroup.getChildren().add(line);

                    }

                }
            }
        }


        for (Colony colony : colonies) {
            Circle circle = new Circle();
            circle.setCenterX(colony.getX() + translateX);
            circle.setCenterY(colony.getY() + translateY);
            circle.setRadius(radius);
            circle.setFill(Color.BLUE);
            Text text = new Text();

            text.setX(colony.getX() + translateX - 30);
            text.setY(colony.getY() + translateY - 10);

            text.setText(String.valueOf(colony.getName()));

            newGroup.getChildren().addAll(text, circle);
        }


        newGroup.getChildren().addAll(loadGraphButton, loadVoronoiDiagram, addColonyButton, deleteColonyButton, saveGraphButton, shortestPathButton);

        Scene newScene = new Scene(newGroup, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(newScene);


    }

    private void drawVoronoi() {
        Voronoi voronoi = new Voronoi(SCREEN_HEIGHT-10, SCREEN_WIDTH -20, colonies,   centrals);
        Canvas canvas =  voronoi.getVoronoi();
        canvas.setTranslateY(100);
        canvas.setTranslateX(10);
        Group newGroup = new Group();
        newGroup.getChildren().addAll(canvas, loadGraphButton, loadVoronoiDiagram, addColonyButton, deleteColonyButton, saveGraphButton, shortestPathButton);

        Scene newScene = new Scene(newGroup, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(newScene);
    }
    private void printMatrix(int[][] matrix) {
        for( int[] row : matrix) {
            for( int element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }

    private void drawShortestPath(){
        int radius = 8;
        int translateX = 100;
        int translateY = 150;
        Group newGroup = new Group();


        int[] x = new int[tsp.solution.size()];
        int[] y = new int[tsp.solution.size()];

        for( Colony colony : tsp.solution ){
            x[tsp.solution.indexOf(colony)] = colony.getX() ;
            y[tsp.solution.indexOf(colony)] = colony.getY() ;

        }
        for( int i = 0; i < x.length - 1 ; i++ ){
            Line line = new Line();
            line.setStartX(x[i] + translateX);

            line.setStartY(y[i] + translateY);

            line.setEndX(x[i+1] + translateX);

            line.setEndY(y[i+1] + translateY);

            Text text = new Text();

            text.setX(x[i] + translateX + 30);
            text.setY(y[i] + translateY + 10);

            text.setText(String.valueOf(i));

            line.setStroke(Color.PURPLE);
            line.setStrokeWidth(3);
            newGroup.getChildren().addAll(text, line);
        }
        Line line = new Line();
        line.setStartX(x[0] + translateX);
        line.setStartY(y[0] + translateY);
        line.setEndX(x[x.length - 1] + translateX);
        line.setEndY(y[y.length - 1] + translateY);


        line.setStroke(Color.PURPLE);
        line.setStrokeWidth(3);
        //newGroup.getChildren().add( line);
        for( Colony colony : tsp.solution ){
            Circle circle = new Circle();
            circle.setCenterX(colony.getX() + translateX);
            circle.setCenterY(colony.getY() + translateY);
            circle.setRadius(radius);
            circle.setFill(Color.BLUE);
            Text text = new Text();

            text.setX(colony.getX() + translateX - 30);
            text.setY(colony.getY() + translateY - 30);

            text.setText(String.valueOf(colony.getName()));

            newGroup.getChildren().addAll(text, circle);
        }
        newGroup.getChildren().addAll(loadGraphButton, loadVoronoiDiagram, addColonyButton, deleteColonyButton, saveGraphButton, shortestPathButton);
        Scene newScene = new Scene(newGroup, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(newScene);
    }

    public static void main(String[] args) {
        launch();
    }

}


;

