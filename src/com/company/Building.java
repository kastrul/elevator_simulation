package com.company;

public class Building {
    private ElevatorConsole console;


    Building(int elevators, int floors, int timeStep) {
        ElevatorSystem system = new ElevatorSystem();
        system.installSystem(elevators, floors, timeStep);
        this.console = new ElevatorConsole(system);
    }


    public void enterBuilding() {
        this.console.start();
    }
}
