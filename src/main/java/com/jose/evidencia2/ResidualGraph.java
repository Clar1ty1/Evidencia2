package com.jose.evidencia2;

import java.util.*;

public class ResidualGraph {
    private double[][] capacityMatrix;
    private boolean[] visited;
    private int[] parent;
    private Map<String, Integer> map;

    public ResidualGraph(ArrayList<Colony> colonies, ArrayList<Link> links, double[][] capMatrix) {
        int numColonies = colonies.size();
        this.capacityMatrix = capMatrix;
        this.visited = new boolean[numColonies];
        this.parent = new int[numColonies];
        this.map = new HashMap<>();

        // Populate the map with colony names and indices
        for (int i = 0; i < colonies.size(); i++) {
            map.put(colonies.get(i).getName(), i);
        }

    }

    public boolean hasAugmentingPath(String startNode, String sinkNode) {
        int start = map.get(startNode);
        int sink = map.get(sinkNode);

        Arrays.fill(visited, false);
        Arrays.fill(parent, -1);

        // Use BFS to find a path from start to sink
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        visited[start] = true;

        while (!queue.isEmpty()) {
            int current = queue.poll();

            for (int next = 0; next < capacityMatrix.length; next++) {
                if (!visited[next] && capacityMatrix[current][next] > 0) {
                    queue.add(next);
                    parent[next] = current;
                    visited[next] = true;
                }
            }
        }

        // Return true if there is a path from start to sink
        return visited[sink];
    }

    public double findAugmentingPath(String startNode, String sinkNode) {
        int start = map.get(startNode);
        int sink = map.get(sinkNode);
        double pathFlow = Double.MAX_VALUE;

        // Find the minimum capacity along the augmenting path
        for (int v = sink; v != start; v = parent[v]) {
            int u = parent[v];
            pathFlow = Math.min(pathFlow, capacityMatrix[u][v]);
        }

        return pathFlow;
    }

    public void updateResidualGraph(double pathFlow, String startNode, String sinkNode) {
        int start = map.get(startNode);
        int sink = map.get(sinkNode);
        // Update the residual capacities along the augmenting path
        for (int v = sink; v != start; v = parent[v]) {
            int u = parent[v];
            capacityMatrix[u][v] -= pathFlow;
            capacityMatrix[v][u] += pathFlow;
        }
    }

    public double calculateMaxFlow(String startNode) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String clave = entry.getKey();
            Integer valor = entry.getValue();
            System.out.println(clave + ": " + valor);
        }

        int start = map.get(startNode);
        double maxFlow = 0.0;

        // Calculate the maximum flow from the source (start) to all reachable vertices
        for (int v = 0; v < capacityMatrix.length; v++) {
            if (visited[v]) {
                maxFlow += capacityMatrix[v][start];
            }
        }

        return maxFlow;
    }
}