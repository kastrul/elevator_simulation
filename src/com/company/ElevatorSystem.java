package com.company;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.abs;
import static java.util.stream.Collectors.toList;

public class ElevatorSystem {
    private List<Elevator> elevators = new ArrayList<>();
    private int floors;
    private String MESSAGE = "Welcome";

    public void installSystem(int elevatorCount, int floorCount, int timeStep) {
        createNElevators(elevatorCount, timeStep);
        this.floors = floorCount;
        MESSAGE = "Building has " + elevatorCount + " elevators and " + floorCount + " floors.";
        System.out.println(MESSAGE);
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    private void createNElevators(int elevatorCount, int timeStep) {
        this.elevators = IntStream.range(1, elevatorCount + 1)
                .mapToObj(n -> new Elevator(n, timeStep))
                .collect(toList());
    }


    public int getNumberOfFloors() {
        return this.floors;
    }

    public void pressUp(int yourFloor, Elevator elev) {
        if (elev == null) {
            MESSAGE = "No free elevators at the moment!";
            System.out.println(MESSAGE);
        } else {
            if (yourFloor != getNumberOfFloors()) {
                MESSAGE = "Pressed up on floor " + yourFloor;
                elev.setDestinationUp(yourFloor);
            } else {
                MESSAGE = "Last floor!";
            }
        }
    }

    public void pressDown(int yourFloor, Elevator elev) {
        if (elev == null) {
            MESSAGE = "No free elevators at the moment!";
            System.out.println(MESSAGE);
        } else {
            if (yourFloor != 1) {
                MESSAGE = "Pressed down on floor " + yourFloor;
                elev.setDestinationDown(yourFloor);
            } else {
                MESSAGE = "First floor!";
            }
        }
    }

    public boolean goToFloor(int elevNr, int destination, int currentFloor) {
        Elevator elev = getWaitingElevator(currentFloor, elevNr);
        if (elev == null || !isFloorInBuilding(destination) || !isFloorInBuilding(currentFloor)) {
            MESSAGE = elev == null ? "Elevator " + elevNr + " has not been called on floor " + currentFloor : MESSAGE;
            return false;
        }

        boolean up = elev.isDestinationUp();

        if (up && currentFloor <= destination) {
            elev.setDestination(destination);
            MESSAGE = String.format("Elevator %d is moving to floor %d", elevNr, destination);
            return true;
        } else if (!up && currentFloor >= destination) {
            elev.setDestination(destination);
            MESSAGE = String.format("Elevator %d is moving to floor %d", elevNr, destination);
            return true;
        }
        MESSAGE = up ? "Elevator " + elevNr + " is going up!" : "Elevator " + elevNr + " is going down!";
        System.out.println(MESSAGE);
        return false;
    }

    public Elevator getFreeElevator(int floor) {
        Comparator<Elevator> byDistance =
                Comparator.comparingInt(e -> abs(e.getCurrentFloor() - floor));
        return elevators.stream()
                .filter(Elevator::isPassive)
                .min(byDistance)
                .orElse(null);
    }

    private Elevator getWaitingElevator(int floor, int elevNr) {
        return elevators.stream()
                .filter(Elevator::isWaiting)
                .filter(e -> e.getCurrentFloor() - floor == 0)
                .filter(e -> e.getNumber() == elevNr)
                .findFirst()
                .orElse(null);
    }

    public Elevator getElevator(int elevID) {
        return elevators.stream()
                .filter(elevator -> elevator.getNumber() == elevID)
                .findFirst()
                .orElse(null);
    }

    private boolean isFloorInBuilding(int floor) {
        if (1 <= floor && floor <= getNumberOfFloors()) {
            return true;
        } else {
            System.out.println("No such floor!");
            return false;
        }
    }

    public void getElevatorLocations() {
        elevators.forEach(e -> {
            String msg = String.format("Elevator: %d, floor: %d, state : %s, direction: %s",
                    e.getNumber(), e.getCurrentFloor(), e.getState(), e.getDirection());
            System.out.println(msg);
        });
    }
}
