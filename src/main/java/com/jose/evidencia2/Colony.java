package com.jose.evidencia2;

public class Colony {
    String name;
    int x;
    int y;

    public Colony( String name, int x, int y){
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName(){ return this.name; }
    public int getX() { return x; }
    public int getY() { return y; }

    public double computeDistance(Colony otraColonia) {
        int deltaX = this.x - otraColonia.x;
        int deltaY = this.y - otraColonia.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

}
