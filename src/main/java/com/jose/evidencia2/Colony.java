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

}
