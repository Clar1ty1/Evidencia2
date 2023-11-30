package com.jose.evidencia2;

public class Link {
    private String colonyBegin;
    private String colonyEnd;
    private double distance;
    private double capacity;


    public Link(String colonyBegin, String colonyEnd, int distance, int capacity){
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
