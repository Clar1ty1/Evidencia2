package com.jose.evidencia2;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class MaxFlow {
    private ArrayList<Colony> colonies;
    private ArrayList<Link> links;

    int[][] capacityMatrix;

    private WritableImage image;

    public MaxFlow(int height, int width, ArrayList<Colony> colonies, int[][] capacityMatrix) {
        this.colonies = colonies;
        this.capacityMatrix = capacityMatrix;
        this.image = new WritableImage(width, height);

    }

    private int calculateMaxFlow(String startNode , String sinkNode) {
        int numColonias = colonies.size();

        int source = getColonyIndex(startNode);
        int sink = getColonyIndex(sinkNode);
        int[] parent = new int[numColonias];



        int maxFlow = 0;

        while (bfs(source, sink, parent, capacityMatrix)) {
            int pathFlow = Integer.MAX_VALUE;

            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, capacityMatrix[u][v]);
            }

            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                capacityMatrix[u][v] -= pathFlow;
                capacityMatrix[v][u] += pathFlow;
            }

            maxFlow += pathFlow;
        }

        return maxFlow;
    }

    private boolean bfs(int source, int sink, int[] parent, int[][] capacityMatrix) {
        int coloniesCount = colonies.size();
        boolean[] visited = new boolean[coloniesCount];

        Arrays.fill(visited, false);

        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited[source] = true;
        parent[source] = -1;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v = 0; v < coloniesCount; v++) {
                if (!visited[v] && capacityMatrix[u][v] > 0) {
                    queue.add(v);
                    visited[v] = true;
                    parent[v] = u;
                }
            }
        }

        return visited[sink];
    }

    private void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    private int getColonyIndex(String nombre) {
        for (int i = 0; i < colonies.size(); i++) {
            if (colonies.get(i).getName().equals(nombre)) {
                return i;
            }
        }
        return -1;
    }

    private boolean searchColony(String nombre) {
        for (int i = 0; i < colonies.size(); i++) {
            if (colonies.get(i).getName().equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    public Canvas showMaxFlow() {
        // Create components for the dialog
        JLabel startLabel = new JLabel("Colonia Inicial:");
        JLabel sinkLabel = new JLabel("Colonia Final:");
        JTextField startTextField = new JTextField(20);
        JTextField sinkTextField = new JTextField(20);

        int result = JOptionPane.showConfirmDialog(
                null,
                new Object[]{startLabel, startTextField, sinkLabel, sinkTextField},
                "Seleccionar Colonias", JOptionPane.OK_CANCEL_OPTION);

        // Check if the user clicked "OK"

        String start = startTextField.getText();
        String sink = sinkTextField.getText();

        // Check if the colonies exist
        boolean coloniaInicial = searchColony(start);
        boolean coloniaFinal = searchColony(sink);

        if (coloniaInicial && coloniaFinal) {
            // Find the route with the maximum accumulated capacity
            double maxFlow = calculateMaxFlow(start, sink);
            System.out.println("Max Flow: " + maxFlow);

            if (maxFlow > 0) {
                // Display the inputs and results in a canvas
                Canvas canvas = new Canvas(image.getWidth(), image.getHeight());
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.drawImage(image, 0, 0);

                gc.setFont(new Font("Arial", 18));
                gc.setFill(Color.BLACK);
                gc.fillText("Start: " + start, 100, 30);
                gc.fillText("Sink: " + sink, 100, 50);
                gc.fillText("Max Flow: " + maxFlow, 100, 70);

                return canvas;
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró ninguna ruta entre las colonias", "Route Not Found", JOptionPane.ERROR_MESSAGE);
            }
        }



        return null;
    }



}