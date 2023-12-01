package com.jose.evidencia2;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;


import javax.swing.*;
import java.util.ArrayList;


public class MaxFlow {
    private ArrayList<Colony> colonies;
    private ArrayList<Link> links;
    private ArrayList<Central> centrals;

    private double[][] capMatrix;
    private WritableImage image;

    public MaxFlow(int height, int width, ArrayList<Colony> colonies, ArrayList<Central> centrals, ArrayList<Link> links, double[][] capMatrix) {
        this.colonies = colonies;
        this.centrals = centrals;
        this.links = links;
        this.capMatrix = capMatrix;

        this.image = new WritableImage(width, height);

    }

    private double getMaxFlow(String start, String sink) {
        // Step 1: Create a residual graph
        ResidualGraph residualGraph = new ResidualGraph(colonies, links, capMatrix);

        // Step 2: Find paths and update flow until no more paths exist
        while (residualGraph.hasAugmentingPath(start, sink)) {
            double pathFlow = residualGraph.findAugmentingPath(start, sink);

            // Update the flow and residual capacities of the edges in the path
            residualGraph.updateResidualGraph(pathFlow, start, sink);
        }

        // Step 3: Calculate the maximum flow
        double maxFlow = residualGraph.calculateMaxFlow(start);

        return maxFlow;
    }

    private boolean searchColony(String colony) {
        for (Colony c : colonies) {
            if (c.getName().equals(colony)) {
                return true;
            }
        }
        return false;
    }

    public Canvas calculateMaxFlow() {
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
        if (result == JOptionPane.OK_OPTION) {
            String start = startTextField.getText();
            String sink = sinkTextField.getText();

            // Check if the colonies exist
            boolean coloniaInicial = searchColony(start);
            boolean coloniaFinal = searchColony(sink);

            if (coloniaInicial && coloniaFinal) {
                // Find the route with the maximum accumulated capacity
                double maxFlow = getMaxFlow(start, sink);
                System.out.println("Max Flow: " + maxFlow);

                if (maxFlow > 0) {
                    // Display the inputs and results in a canvas
                    Canvas canvas = new Canvas(image.getWidth(), image.getHeight());
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    gc.drawImage(image, 0, 0);

                    gc.setFill(Color.BLACK);
                    gc.fillText("Start: " + start, 100, 20);
                    gc.fillText("Sink: " + sink, 100, 40);
                    gc.fillText("Max Flow: " + maxFlow, 100, 60);

                    return canvas;
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró ninguna ruta entre las colonias", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }


        }
        return null;
    }
}