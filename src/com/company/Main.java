package com.company;

public class Main {

    public static void main(String[] args) {
        // write your code here
        int ELEVATORS = 3;
        int FLOORS = 13;
        int TIME_STEP = 2;

        Building building = new Building(ELEVATORS, FLOORS, TIME_STEP);
        building.enterBuilding();
    }
}
