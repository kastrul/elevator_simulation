package com.company;

import java.util.Scanner;

public class ElevatorConsole {
    private ElevatorSystem elevSys;

    ElevatorConsole(ElevatorSystem elevSys) {
        this.elevSys = elevSys;
    }

    private void mainApplication() {
        Scanner keyboard = new Scanner(System.in);

        boolean inBuilding = true;
        int currentFloor = 1;
        while (inBuilding) {
            String command = keyboard.nextLine().toLowerCase();

            currentFloor = command.startsWith("f") && elevSys.isFloorInBuilding(Integer.parseInt(command.substring(1)))
                    ? Integer.parseInt(command.substring(1)) : currentFloor;
            command = command.startsWith("f") ? "f" : command;

            switch (command) {
                case "f":
                    System.out.println("You're on floor " + currentFloor + ".");
                    break;
                case "u":
                    elevSys.pressUp(currentFloor);
                    break;
                case "d":
                    elevSys.pressDown(currentFloor);
                    break;
                case "e":
                    elevSys.enterElevator(currentFloor);
                    break;
                case "loc":
                    elevSys.getElevatorLocations();
                    break;
                case "exit":
                    System.out.println("Leaving building!");
                    inBuilding = false;
                    break;
            }
        }
    }


    public void start() {
        System.out.println("Welcome to elevator building.");
        System.out.println("Write: ");
        System.out.println("  'fXX' to choose floor number XX");
        System.out.println("  'e' to enter elevator");
        System.out.println("  'u' to call elevator when going up");
        System.out.println("  'd' to call elevator when going down");
        System.out.println("  'loc' to see elevators locations");
        System.out.println("  'exit' to exit building");
        mainApplication();
    }
}
