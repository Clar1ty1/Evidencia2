package com.jose.evidencia2;

public class Link {
    String colonyBegin;
    String colonyEnd;
    int distance;
    int capacity;

    public Link(String colonyBegin, String colonyEnd, int distance, int capacity){
        this.colonyBegin = colonyBegin;
        this.colonyEnd = colonyEnd;
        this.distance = distance;
        this.capacity = capacity;
    }

    public String getColonyBegin(){ return this.colonyBegin; }
    public String getColonyEnd(){ return this.colonyEnd; }

    public int getDistance(){ return this.distance; }
    public int getCapacity(){ return this.capacity; }
}
