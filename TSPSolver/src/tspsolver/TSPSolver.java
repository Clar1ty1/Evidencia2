/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tspsolver;

/**
 *
 * @author JOSE ORTEGA
 */

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TSPSolver {

    public static void main(String[] args) {
        try {
            // Cambia la ruta al archivo JSON según la ubicación en tu proyecto
            String filePath = "C:/Users/JOSE ORTEGA/Documents/colonia.json";
            JSONObject json = leerJsonDesdeArchivo(filePath);

            JSONArray coloniasArray = json.getJSONObject("ciudad").getJSONArray("colonias");

            List<Colonia> colonias = new ArrayList<>();

            for (int i = 0; i < coloniasArray.length(); i++) {
                JSONObject coloniaJson = coloniasArray.getJSONObject(i);
                Colonia colonia = new Colonia(
                        coloniaJson.getString("nombre"),
                        coloniaJson.getInt("coordenadaX"),
                        coloniaJson.getInt("coordenadaY")
                );
                colonias.add(colonia);
            }

            TSPSolver tspSolver = new TSPSolver();
            tspSolver.solveTSP(colonias);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JSONObject leerJsonDesdeArchivo(String filePath) throws Exception {
        try (FileReader fileReader = new FileReader(filePath)) {
            JSONTokener tokener = new JSONTokener(fileReader);
            return new JSONObject(tokener);
        }
    }

    private void solveTSP(List<Colonia> colonias) {
        Set<Colonia> coloniasRestantes = new HashSet<>(colonias);
        List<Colonia> rutaActual = new ArrayList<>();
        double distanciaParcial = 0.0;

        // Selecciona la primera colonia como punto de partida
        Colonia puntoPartida = colonias.get(0);
        coloniasRestantes.remove(puntoPartida);
        rutaActual.add(puntoPartida);

        // Inicia el proceso recursivo
        tspRecursivo(puntoPartida, coloniasRestantes, rutaActual, distanciaParcial);

        // Muestra la ruta y la distancia total
        mostrarRuta(rutaActual, calcularDistanciaTotal(rutaActual));
    }

    private void tspRecursivo(Colonia actual, Set<Colonia> coloniasRestantes, List<Colonia> rutaActual, double distanciaParcial) {
        if (coloniasRestantes.isEmpty()) {
            // Todas las colonias han sido visitadas, regresa a la colonia de inicio
            distanciaParcial += actual.calcularDistanciaA(rutaActual.get(0));
            rutaActual.add(rutaActual.get(0));  // Agrega la colonia de inicio al final de la ruta
            mostrarRuta(rutaActual, distanciaParcial);
            return;
        }

        Set<Colonia> copiaColoniasRestantes = new HashSet<>(coloniasRestantes);

        for (Colonia siguiente : copiaColoniasRestantes) {
            double nuevaDistanciaParcial = distanciaParcial + actual.calcularDistanciaA(siguiente);
            coloniasRestantes.remove(siguiente);
            rutaActual.add(siguiente);

            tspRecursivo(siguiente, coloniasRestantes, rutaActual, nuevaDistanciaParcial);

            // Deshace el movimiento para explorar otras posibilidades
            coloniasRestantes.add(siguiente);
            rutaActual.remove(rutaActual.size() - 1);
        }
    }

    private double calcularDistanciaTotal(List<Colonia> ruta) {
        double distanciaTotal = 0.0;

        for (int i = 0; i < ruta.size() - 1; i++) {
            distanciaTotal += ruta.get(i).calcularDistanciaA(ruta.get(i + 1));
        }

        return distanciaTotal;
    }

    private void mostrarRuta(List<Colonia> ruta, double distancia) {
        System.out.println("Ruta: " + ruta);
        System.out.println("Distancia total: " + distancia);
    }

    private static class Colonia {
        private final String nombre;
        private final int coordenadaX;
        private final int coordenadaY;

        public Colonia(String nombre, int coordenadaX, int coordenadaY) {
            this.nombre = nombre;
            this.coordenadaX = coordenadaX;
            this.coordenadaY = coordenadaY;
        }

        public double calcularDistanciaA(Colonia otraColonia) {
            int deltaX = this.coordenadaX - otraColonia.coordenadaX;
            int deltaY = this.coordenadaY - otraColonia.coordenadaY;
            return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        }

        @Override
        public String toString() {
            return nombre;
        }
    }
}

