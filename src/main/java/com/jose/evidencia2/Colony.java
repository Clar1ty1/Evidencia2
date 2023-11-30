package com.jose.evidencia2;

import java.util.List;

public class Colony {
    String name;
    int x;
    int y;
    double linkOffset = 0;
    public Colony( String name, int x, int y){
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName(){ return this.name; }
    public int getX() { return x; }
    public int getY() { return y; }
    public double getLinkOffset(){ return this.linkOffset; }

    public double computeDistance(Colony targetColony) {
        int deltaX = this.x - targetColony.x;
        int deltaY = this.y - targetColony.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
    private void mostrarRuta(List<Colony> ruta, double distancia) {
        System.out.println("Ruta: " + ruta);
        System.out.println("Distancia total: " + distancia);
    }
    public void  setLinkOffset(double offset){ this.linkOffset = offset; }

}
