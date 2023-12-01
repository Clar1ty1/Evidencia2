package com.jose.evidencia2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class TreeSpanning {
    boolean solutionPrinted = false;
    public List< Colony > solution = new ArrayList<>();
    public int totalDistance = 0;
    public void solveTsp(List<Colony> colonias) {
        Set<Colony> coloniasRestantes = new HashSet<>(colonias);
        List<Colony> rutaActual = new ArrayList<>();
        double distanciaParcial = 0.0;

        // Selecciona la primera colonia como punto de partida
        Colony puntoPartida = colonias.get(0);
        coloniasRestantes.remove(puntoPartida);
        rutaActual.add(puntoPartida);

        // Inicia el proceso recursivo
        tspRecursivo(puntoPartida, coloniasRestantes, rutaActual, distanciaParcial);


        // Muestra la ruta y la distancia total

    }
    private void tspRecursivo(Colony actual, Set<Colony> coloniasRestantes, List<Colony> rutaActual, double distanciaParcial) {
        if ( coloniasRestantes.isEmpty() ) {

            if( !solutionPrinted ) {
                this.solutionPrinted = true;
                distanciaParcial += actual.computeDistance(rutaActual.get(0));
                rutaActual.add(rutaActual.get(0));  // Agrega la colonia de inicio al final de la ruta
                mostrarRuta(rutaActual, (int)distanciaParcial);
            }

            return;
        }

        Set<Colony> copiaColoniasRestantes = new HashSet<>(coloniasRestantes);

        for (Colony siguiente : copiaColoniasRestantes) {
            double nuevaDistanciaParcial = distanciaParcial + actual.computeDistance(siguiente);
            coloniasRestantes.remove(siguiente);
            rutaActual.add(siguiente);

            tspRecursivo(siguiente, coloniasRestantes, rutaActual, nuevaDistanciaParcial);

            // Deshace el movimiento para explorar otras posibilidades
            coloniasRestantes.add(siguiente);
            rutaActual.remove(rutaActual.size() - 1);
        }
    }

    public double calcularDistanciaTotal(List<Colony> ruta) {
        double distanciaTotal = 0.0;

        for (int i = 0; i < ruta.size() - 1; i++) {
            distanciaTotal += ruta.get(i).computeDistance(ruta.get(i + 1));
        }

        return distanciaTotal;
    }

    public void mostrarRuta(List<Colony> ruta, int distancia) {

        for( Colony colony : ruta) {
            this.solution.add(colony);
            this.totalDistance = distancia;
            System.out.print(colony.getName() + " ");
        }
        System.out.println();
        System.out.println("Distancia total: " + distancia);
    }
}




