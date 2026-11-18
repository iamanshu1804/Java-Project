package com.iamanshu.university.model;

public class Classroom {
    private final String name;
    private final String floor;
    private final int capacity;
    private boolean occupied;

    public Classroom(String name, String floor, int capacity, boolean occupied) {
        this.name = name;
        this.floor = floor;
        this.capacity = capacity;
        this.occupied = occupied;
    }

    public String getName() { return name; }
    public String getFloor() { return floor; }
    public int getCapacity() { return capacity; }
    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }
    public void toggleOccupied() { occupied = !occupied; }
    public String getStatus() { return occupied ? "Occupied" : "Free"; }
}
