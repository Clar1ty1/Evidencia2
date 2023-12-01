package com.jose.evidencia2;


import java.io.FileWriter;
import java.util.*;

import com.google.gson.*;
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
import javafx.scene.text.Font;
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
    float divider = 1.25f;
    double spacing = (SCREEN_WIDTH - BUTTON_WIDTH * 5) / 6;
    int[][] adjacencyMatrix;
    int[][] adjacencyMatrixDijkstra;
    int[][] capacityMatrix;

    String filePath = "";

    Button loadGraphButton = new Button("Cargar grafo");
    Button loadVoronoiDiagram = new Button("Diagrama Voronoi");
    Button addColonyButton = new Button("Añadir colonia");
    Button deleteColonyButton = new Button("Eliminar colonia");
    Button saveGraphButton = new Button("Guardar grafo");
    Button shortestPathButton = new Button("Ruta más corta");
    Button addLinkButton = new Button("Añadir enlace");


    Button deleteLinkButton = new Button("Eliminar enlace");

    Button optimalNetworkButton = new Button("Cableado Óptimo");
    Button maxFlowButton = new Button("Flujo Máximo");

    Button seeGraphButton = new Button("Ver grafo");

    TreeSpanning tsp = new TreeSpanning();
    private int[] costs;
    Label label1 = new Label("Seleccione una opción para comenzar");



    @Override
    public void start(Stage stage) {
        this.stage = stage;
        
        this.stage.setTitle("Evidencia 2");
        
        stage.setWidth(SCREEN_WIDTH);

        label1.setTranslateX(SCREEN_WIDTH/2 - 100);



        loadGraphButton.setMaxWidth(BUTTON_WIDTH);
        loadGraphButton.setMinWidth(BUTTON_WIDTH);

        loadVoronoiDiagram.setMaxWidth(BUTTON_WIDTH);
        loadVoronoiDiagram.setMinWidth(BUTTON_WIDTH);

        addColonyButton.setMaxWidth(BUTTON_WIDTH);
        addColonyButton.setMinWidth(BUTTON_WIDTH);

        addLinkButton.setMaxWidth(BUTTON_WIDTH);
        addLinkButton.setMinWidth(BUTTON_WIDTH);

        maxFlowButton.setMaxWidth(BUTTON_WIDTH);
        maxFlowButton.setMinWidth(BUTTON_WIDTH);

        deleteLinkButton.setMaxWidth(BUTTON_WIDTH);
        deleteLinkButton.setMinWidth(BUTTON_WIDTH);

        deleteColonyButton.setMaxWidth(BUTTON_WIDTH);
        deleteColonyButton.setMinWidth(BUTTON_WIDTH);

        saveGraphButton.setMaxWidth(BUTTON_WIDTH);
        saveGraphButton.setMinWidth(BUTTON_WIDTH);

        optimalNetworkButton.setMaxWidth(BUTTON_WIDTH);
        optimalNetworkButton.setMinWidth(BUTTON_WIDTH);

        shortestPathButton.setMaxWidth(BUTTON_WIDTH);
        shortestPathButton.setMinWidth(BUTTON_WIDTH);

        seeGraphButton.setMaxWidth(BUTTON_WIDTH);
        seeGraphButton.setMinWidth(BUTTON_WIDTH);

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
        addColonyButton.setTranslateX(pos);
        addColonyButton.setTranslateY(50);
        addColonyButton.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addColony();
            }
        });

        pos = pos + BUTTON_WIDTH + spacing;
        deleteColonyButton.setTranslateX(pos);
        deleteColonyButton.setTranslateY(50);
        deleteColonyButton.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteColony();
            }
        });

        pos = pos + BUTTON_WIDTH + spacing;
        addLinkButton.setTranslateX(pos);
        addLinkButton.setTranslateY(50);
        addLinkButton.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addLink();
            }
        });

        pos = pos + BUTTON_WIDTH + spacing;
        deleteLinkButton.setTranslateX(pos);
        deleteLinkButton.setTranslateY(50);
        deleteLinkButton.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteLink();
            }
        });

        pos = 0;
        pos = pos + spacing;
        loadVoronoiDiagram.setTranslateX(pos);
        loadVoronoiDiagram.setTranslateY(80);
        loadVoronoiDiagram.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                drawVoronoi();
            }
        });

        pos = pos + BUTTON_WIDTH + spacing;
        saveGraphButton.setTranslateX(pos);
        saveGraphButton.setTranslateY(80);
        saveGraphButton.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveGraph();
            }
        });


        pos = pos + BUTTON_WIDTH + spacing;
        shortestPathButton.setTranslateX(pos);
        shortestPathButton.setTranslateY(80);
        shortestPathButton.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tsp.solveTsp(colonies);
                drawShortestPath();
            }
        });


        pos = pos + BUTTON_WIDTH + spacing;
        maxFlowButton.setTranslateX(pos);
        maxFlowButton.setTranslateY(80);
        maxFlowButton.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getMaxFlow();
            }
        });

        pos = pos + BUTTON_WIDTH + spacing;
        optimalNetworkButton.setTranslateX(pos);
        optimalNetworkButton.setTranslateY(80);
        optimalNetworkButton.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startDijkstra();
            }
        });

        pos = 0;
        pos = pos + spacing + (BUTTON_WIDTH + spacing) * 2;
        seeGraphButton.setTranslateX(pos);
        seeGraphButton.setTranslateY(110);
        seeGraphButton.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                drawGraph();
            }
        });
     
        mainGroup = new Group();
        
        mainGroup.getChildren().addAll(loadGraphButton,loadVoronoiDiagram, addColonyButton, addLinkButton, deleteLinkButton, deleteColonyButton, saveGraphButton, label1, shortestPathButton, maxFlowButton, optimalNetworkButton, seeGraphButton);

        Scene scene = new Scene(mainGroup, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        stage.setScene(scene);
        
        stage.show();
    }
    /**
     * Loads a graph from a file.
     * Has complexity O(n).
     * @return None    No return value.
     */
    private void loadGraph() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir archivo");
        File file = fileChooser.showOpenDialog(stage);
        colonies = new ArrayList<>();
        links = new ArrayList<>();
        centrals = new ArrayList<>();
        this.filePath = file.getAbsolutePath();

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
                int x = (int) (colony.getAsJsonObject().get("coordenadaX").getAsInt()/divider);
                int y = (int) (colony.getAsJsonObject().get("coordenadaY").getAsInt()/divider);

                this.colonies.add( new Colony(name, x, y));

            }

            for( JsonElement link : links.getAsJsonArray()) {
                String colonyBegin = link.getAsJsonObject().get("coloniaInicial").getAsString();
                String colonyEnd = link.getAsJsonObject().get("coloniaFinal").getAsString();
                int distance = (int) (link.getAsJsonObject().get("distancia").getAsInt());
                int capacity = (int) (link.getAsJsonObject().get("capacidad").getAsInt());
                this.links.add( new Link(colonyBegin,colonyEnd,distance,capacity) );

            }

            for( JsonElement central : centrals.getAsJsonArray()) {

                int x = (int) (central.getAsJsonObject().get("x").getAsInt()/divider);
                int y = (int) (central.getAsJsonObject().get("y").getAsInt()/divider);
                this.centrals.add( new Central( x, y ));
            }


            drawGraph();

        }
        catch (IOException ex) {
            System.out.println(ex);
        }
    }

    private void drawGraph() {

        int radius = 10;
        int translateX = 200;
        int translateY = 150;
        this.adjacencyMatrix = new int[this.colonies.size()+1][this.colonies.size()+1];
        this.adjacencyMatrixDijkstra = new int[this.colonies.size()+1][this.colonies.size()+1];
        this.capacityMatrix = new int[this.colonies.size()+1][this.colonies.size()+1];

        Group newGroup = new Group();

        for( Colony colony : colonies) {
            map.put(colony.getName(), colonies.indexOf(colony));
        }

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

            this.adjacencyMatrix[index1][index2] = (int) link.getDistance();
            this.adjacencyMatrixDijkstra[index1][index2] = (int) link.getDistance();
            this.capacityMatrix[index1][index2] = (int) link.getCapacity();

        }
        int[][] aux = this.adjacencyMatrix;
        for( int i = 0; i < this.adjacencyMatrix.length; i++) {
            for( int j = 0; j < this.adjacencyMatrix.length; j++) {
                //System.out.print(adjacencyMatrix[i][j] + " ");
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
            //System.out.println();
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


        newGroup.getChildren().addAll(loadGraphButton,loadVoronoiDiagram, addColonyButton, addLinkButton, deleteLinkButton, deleteColonyButton, saveGraphButton, label1, shortestPathButton, maxFlowButton, optimalNetworkButton, seeGraphButton);

        Scene newScene = new Scene(newGroup, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(newScene);


    }

    private void drawVoronoi() {
        Voronoi voronoi = new Voronoi(SCREEN_HEIGHT - 10, SCREEN_WIDTH - 20, colonies, centrals);
        Canvas canvas = voronoi.getVoronoi();
        canvas.setTranslateY(150);
        canvas.setTranslateX(10);
        Group newGroup = new Group();
        newGroup.getChildren().addAll(canvas, loadGraphButton,loadVoronoiDiagram, addColonyButton, addLinkButton, deleteLinkButton, deleteColonyButton, saveGraphButton, label1, shortestPathButton, maxFlowButton, optimalNetworkButton, seeGraphButton );
        Scene newScene = new Scene(newGroup, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(newScene);

    }

    private void getMaxFlow() {

        MaxFlow maxFlow = new MaxFlow(SCREEN_HEIGHT - 10, SCREEN_WIDTH - 20,colonies, capacityMatrix);
        Canvas canvas = maxFlow.showMaxFlow();
        canvas.setTranslateX(50);
        canvas.setTranslateY(150);
        Group newGroup = new Group();
        newGroup.getChildren().addAll(canvas, loadGraphButton,loadVoronoiDiagram, addColonyButton, addLinkButton, deleteLinkButton, deleteColonyButton, saveGraphButton, label1, shortestPathButton, maxFlowButton, optimalNetworkButton, seeGraphButton );
        Scene newScene = new Scene(newGroup, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(newScene);


    }

    private void drawShortestPath(){
        int radius = 8;
        int translateX = 100;
        int translateY = 220;
        Group newGroup = new Group();


        int[] x = new int[tsp.solution.size()];
        int[] y = new int[tsp.solution.size()];

        for( Colony colony : tsp.solution ){
            x[tsp.solution.indexOf(colony)] = colony.getX() ;
            y[tsp.solution.indexOf(colony)] = colony.getY() ;

        }
        for( int i = 0; i < x.length - 2 ; i++ ){
            Line line = new Line();
            line.setStartX(x[i] + translateX);

            line.setStartY(y[i] + translateY);

            line.setEndX(x[i+1] + translateX);

            line.setEndY(y[i+1] + translateY);

            //System.out.println(x[i] + " " + y[i]);
            //System.out.println(x[i+1] + " " + y[i+1]);
            Text text = new Text();

            text.setX(x[i] + translateX + 20);
            text.setY(y[i] + translateY + 10);
            text.setFont( new Font("Arial", 18));
            text.setText(String.valueOf(i));

            line.setStroke(Color.PURPLE);
            line.setStrokeWidth(3);
            newGroup.getChildren().addAll(text, line);
        }
        Line line = new Line();
        line.setStartX(x[0] + translateX);
        line.setStartY(y[0] + translateY);
        line.setEndX(x[x.length - 2] + translateX);
        line.setEndY(y[y.length - 2] + translateY);
        Text lastText = new Text();
        lastText.setX(x[x.length - 2] + translateX + 20);
        lastText.setY(y[y.length - 2] + translateY + 10);
        lastText.setFont( new Font("Arial", 18));
        lastText.setText(String.valueOf(x.length - 2));

        line.setStroke(Color.PURPLE);
        line.setStrokeWidth(3);
        newGroup.getChildren().add( line);
        newGroup.getChildren().add(lastText);
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
        newGroup.getChildren().addAll(loadGraphButton,loadVoronoiDiagram, addColonyButton, addLinkButton, deleteLinkButton, deleteColonyButton, saveGraphButton, label1, shortestPathButton, maxFlowButton, optimalNetworkButton, seeGraphButton );
        Scene newScene = new Scene(newGroup, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(newScene);
    }

    private void addColony(){
        Stage newStage = new Stage();
        Group newGroup = new Group();
        newGroup.setTranslateX(10);
        Scene newScene = new Scene(newGroup, 500, 500);
        Label nameLabel = new Label("Nombre");
        nameLabel.setTranslateY(50);
        Label xLabel = new Label("Coordenada X ");
        xLabel.setTranslateY(150);
        Label yLabel = new Label("Coordenada Y");
        yLabel.setTranslateY(250);
        TextField name = new TextField();
        name.setPromptText("Name");
        name.setTranslateY(100);
        TextField x = new TextField();
        x.setPromptText("X");
        x.setTranslateY(200);
        TextField y = new TextField();
        y.setPromptText("Y");
        y.setTranslateY(300);
        Button addButton = new Button("Add");
        addButton.setTranslateY(400);
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                colonies.add(new Colony(name.getText(), Integer.parseInt(x.getText()), Integer.parseInt(y.getText())));
                newStage.close();
                drawGraph();
            }
        });
        newGroup.getChildren().addAll(name, x, y, addButton, nameLabel, xLabel, yLabel);
        newStage.setScene(newScene);
        newStage.show();
    }

    private void addLink(){
        Stage newStage = new Stage();
        Group newGroup = new Group();
        newGroup.setTranslateX(10);
        Scene newScene = new Scene(newGroup, 500, 500);

        Label city1Label = new Label("Nombre ciudad 1");
        city1Label.setTranslateY(0);
        Label city2Label = new Label("Nombre ciudad 2");
        city2Label.setTranslateY(40);
        Label distanceLabel = new Label("Distancia");
        distanceLabel.setTranslateY(80);
        Label capacityLabel = new Label("Capacidad");
        capacityLabel.setTranslateY(120);
        TextField city1 = new TextField();
        city1.setPromptText("Nombre ciudad 1");
        city1.setTranslateY(20);
        TextField city2 = new TextField();
        city2.setPromptText("Nombre ciudad 2");
        city2.setTranslateY(60);
        TextField distance = new TextField();
        distance.setPromptText("Distancia");
        distance.setTranslateY(100);
        TextField capacity = new TextField();
        capacity.setPromptText("Capacidad");
        capacity.setTranslateY(140);


        Button addButton = new Button("Add");
        addButton.setTranslateY(400);
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if( !map.containsKey(city1.getText())  ){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText( city1.getText() + " no existe");

                    alert.showAndWait();
                    return;
                }
                if( !map.containsKey(city2.getText()) ){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(city2.getText() + " no existe");

                    alert.showAndWait();
                    return;
                }

                links.add(new Link(city1.getText(), city2.getText(), Integer.parseInt(distance.getText()), Integer.parseInt(capacity.getText())));
                newStage.close();
                drawGraph();
            }
        });
            newGroup.getChildren().addAll(city1, city2, distance, capacity, addButton, city1Label, city2Label, distanceLabel, capacityLabel);
        newStage.setScene(newScene);
        newStage.show();
    }

    private void deleteColony(){
        Stage newStage = new Stage();
        Group newGroup = new Group();
        newGroup.setTranslateX(10);
        Scene newScene = new Scene(newGroup, 300, 300);

        Label city1Label = new Label("Nombre colonia a eliminar");
        city1Label.setTranslateY(0);
        TextField city1 = new TextField();
        city1.setPromptText("Colonia");
        city1.setTranslateY(30);

        Button deleteButton = new Button("Delete");
        deleteButton.setTranslateY(80);
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if( !map.containsKey(city1.getText())  ){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText( city1.getText() + " no existe");

                    alert.showAndWait();
                    return;
                }

                int index = map.get(city1.getText());
                colonies.remove(index);
                List<Link> linksToRemove = new ArrayList<>();
                for( Link link : links ){
                    if( link.getColonyBegin().equals(city1.getText()) || link.getColonyEnd().equals(city1.getText()) ){
                        linksToRemove.add(link);
                    }
                }
                for( Link link : linksToRemove ){
                    links.remove(link);
                }

                newStage.close();
                drawGraph();
            }
        });
        newGroup.getChildren().addAll(city1, deleteButton, city1Label);
        newStage.setScene(newScene);
        newStage.show();
    }

    private void deleteLink(){
        Stage newStage = new Stage();
        Group newGroup = new Group();
        newGroup.setTranslateX(10);
        Scene newScene = new Scene(newGroup, 300, 300);

        Label city1Label = new Label("Especifique el enlace a eliminar");
        city1Label.setTranslateY(0);
        TextField  startCity = new TextField();
        startCity.setPromptText("Colonia Origen");
        startCity.setTranslateY(30);

        TextField endCity = new TextField();
        endCity.setPromptText("Colonia Destino");
        endCity.setTranslateY(60);

        Button deleteButton = new Button("Delete");
        deleteButton.setTranslateY(90);
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if( !map.containsKey(startCity.getText()) ) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(startCity.getText() + " no existe");
                    alert.showAndWait();
                    return;
                }
                else if (!map.containsKey(endCity.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(endCity.getText() + " no existe");
                    alert.showAndWait();
                    return;

                }

                List<Link> linksToRemove = new ArrayList<>();
                for( Link link : links ){
                    if( link.getColonyBegin().equals(startCity.getText()) && link.getColonyEnd().equals(endCity.getText()) ){
                        linksToRemove.add(link);
                    }
                }
                for( Link link : linksToRemove ){
                    links.remove(link);
                }

                newStage.close();
                drawGraph();
            }
        });
        newGroup.getChildren().addAll(startCity, endCity, deleteButton, city1Label);
        newStage.setScene(newScene);
        newStage.show();


    }


    private void saveGraph(){
        try{
            FileWriter writer = new FileWriter(this.filePath);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            char quote = '"';
            String json = "{" + quote + "ciudad" + quote + ": { "  + quote + "colonias" + quote + ": [";

            for( Colony colony : colonies ){
                json += "{" + quote + "nombre" + quote + ": " + quote + colony.getName() + quote + "," + quote + "coordenadaX" + quote + ": " + (int)(colony.getX()*divider) + "," + quote + "coordenadaY" + quote + ": " + (int)(colony.getY()*divider) + "},";
            }
            json = json.substring(0, json.length() - 1);
            json += "]," + quote + "enlaces" + quote + ": [";

            for( Link link : links ){
                json += "{" + quote + "coloniaInicial" + quote + ":" + quote + link.getColonyBegin() + quote + "," + quote + "coloniaFinal" + quote + ":" + quote + link.getColonyEnd() + quote + "," + quote + "distancia" + quote + ":" + (int)(link.getDistance()) + "," + quote + "capacidad" + quote + ":" + (int)(link.getCapacity()) + "},";
            }
            json = json.substring(0, json.length() - 1);
            json += "]," + quote + "centrales" + quote + ": [";

            for( Central central : centrals ){
                json += "{" + quote + "x" + quote + ":" + quote + (int)(central.getX()*divider) + quote + "," + quote + "y" + quote + ":" + quote + (int)(central.getY()*divider) + quote + "},";
            }
            json = json.substring(0, json.length() - 1);
            json += "]}}";
            writer.write(json);
            writer.close();
        }
        catch(Exception e){

        }
    }

    public int[] dijkstra( Colony start, Colony end ){
        costs = new int[colonies.size()];
        for(int i = 0; i < colonies.size(); i++){
            costs[i] = Integer.MAX_VALUE;
        }

        for( int i = 0; i < this.adjacencyMatrixDijkstra.length; i++){
            for( int j = 0; j < this.adjacencyMatrixDijkstra[i].length; j++){
                if (this.adjacencyMatrixDijkstra[i][j] == -1){
                    this.adjacencyMatrixDijkstra[i][j] = 0;
                }
                //System.out.print(this.adjacencyMatrixDijkstra[i][j] + " ");
            }
            //System.out.println();
        }

        Queue<Colony> unvisited = new LinkedList<>();
        unvisited.add(colonies.get(map.get(start.getName())));
        List<Colony> visited = new ArrayList<>();
        int index = map.get(start.getName());
        //System.out.print(colonies.get(index).getName() + " ");
        costs[ map.get(start.getName()) ] = 0;
        int[] previous = new int[colonies.size() ];

        for( Colony c : colonies ){
            unvisited.add(c);
        }



        while( !unvisited.isEmpty() ){
            index = map.get(unvisited.peek().getName());
            //System.out.println("Index: " + index);

            for( int i = 0; i < adjacencyMatrixDijkstra[index].length - 1 ; i++){
                if( costs[i] > costs[index] + adjacencyMatrixDijkstra[index][i] && adjacencyMatrixDijkstra[index][i] > 0 && !visited.contains(colonies.get(index))){
                    costs[i] = costs[index] + adjacencyMatrixDijkstra[index][i];
                    //System.out.println("New cost at index: " + i + " = " + costs[index] + adjacencyMatrixDijkstra[index][i]);
                    previous[i] = index;
                    visited.add(unvisited.peek());

                }
                //System.out.println(" cost at index: " + index + " = " + costs[index] + " cost at index: " + i + " = " + adjacencyMatrixDijkstra[index][i]);

            }
            unvisited.poll();
        }


        return previous;
    }

    private void startDijkstra(){
        Stage newStage = new Stage();
        Group newGroup = new Group();
        newGroup.setTranslateX(10);
        Scene newScene = new Scene(newGroup, 300, 300);

        Label city1Label = new Label("Nombre colonia 1");
        city1Label.setTranslateY(0);
        TextField city1 = new TextField();
        city1.setPromptText("Colonia");
        city1.setTranslateY(30);

        Label city2Label = new Label("Nombre colonia 2");
        city2Label.setTranslateY(60);
        TextField city2 = new TextField();
        city2.setPromptText("Colonia");
        city2.setTranslateY(90);

        Button computeButton = new Button("Compute");
        computeButton.setTranslateY(130);
        computeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if( !map.containsKey(city1.getText())  ){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText( city1.getText() + " no existe");

                    alert.showAndWait();
                    return;
                }
                if( !map.containsKey(city2.getText())  ){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText( city2.getText() + " no existe");

                    alert.showAndWait();
                    return;
                }

                drawDijkstra(city1.getText(), city2.getText());

                newStage.close();

            }
        });
        newGroup.getChildren().addAll(city1, computeButton, city1Label, city2, city2Label);
        newStage.setScene(newScene);
        newStage.show();
    }

    private void drawDijkstra(String city1, String city2){
        int radius = 8;
        int translateX = 100;
        int translateY = 150;
        Group newGroup = new Group();

        int[] path = dijkstra(colonies.get(map.get(city1)), colonies.get(map.get(city2)));


        int[] x = new int[path.length];
        int[] y = new int[path.length];
        for( int i = 1; i < path.length; i++){
            x[i] = colonies.get(path[i]).getX();
            y[i] = colonies.get(path[i]).getY();
        }
        for( int i = 1; i < x.length - 1 ; i++){
            Line line = new Line(x[i], y[i], x[i+1], y[i+1]);
            System.out.print(i + "->");
            System.out.println();
            line.setStroke(Color.PURPLE);
            line.setStrokeWidth(5);
            line.setTranslateX(translateX);
            line.setTranslateY(translateY);
            newGroup.getChildren().add(line);
        }
        Line lastLine = new Line();
        lastLine.setStartX(x[x.length - 1]);
        lastLine.setStartY(y[x.length - 1]);
        lastLine.setEndX(colonies.get(map.get(city2)).getX());
        lastLine.setEndY(colonies.get(map.get(city2)).getY());
        lastLine.setStroke(Color.PURPLE);
        lastLine.setStrokeWidth(5);
        lastLine.setTranslateX(translateX);
        lastLine.setTranslateY(translateY);
        newGroup.getChildren().add(lastLine);

        for( Colony colony : colonies ){
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

        newGroup.getChildren().addAll(loadGraphButton,loadVoronoiDiagram, addColonyButton, addLinkButton, deleteLinkButton, deleteColonyButton, saveGraphButton, label1, shortestPathButton, maxFlowButton, optimalNetworkButton, seeGraphButton );
        stage.setScene(new Scene(newGroup, SCREEN_WIDTH, SCREEN_HEIGHT));
    }

    public static void main(String[] args) {
        launch();
    }

}


;

