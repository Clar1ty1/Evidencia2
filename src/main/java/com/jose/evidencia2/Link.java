package com.jose.evidencia2;

public class Link {
    String colonyBegin;
    String colonyEnd;
    double distance;
    double capacity;


    public Link(String colonyBegin, String colonyEnd, double distance, double capacity){
        this.colonyBegin = colonyBegin;
        this.colonyEnd = colonyEnd;
        this.distance = distance;
        this.capacity = capacity;
    }

    public String getColonyBegin(){ return this.colonyBegin; }
    public String getColonyEnd(){ return this.colonyEnd; }

    public double getDistance(){ return this.distance; }
    public double getCapacity(){ return this.capacity; }
}
