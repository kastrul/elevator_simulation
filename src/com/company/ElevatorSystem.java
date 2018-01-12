package com.company;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

import static java.lang.Math.abs;
import static java.util.stream.Collectors.toList;

public class ElevatorSystem {
    private List<Elevator> elevators = new ArrayList<>();
    private List<Floor> floors = new ArrayList<>();

    public void installSystem(int elevatorCount, int floorCount, int timeStep) {
        createNElevators(elevatorCount, timeStep);
        createNFloors(floorCount);
        System.out.println("Building has " + elevatorCount + " elevators and " + floorCount + " floors.");
    }

    private void createNElevators(int elevatorCount, int timeStep) {
        this.elevators = IntStream.range(1, elevatorCount + 1)
                .mapToObj(n -> new Elevator(n, timeStep))
                .collect(toList());
    }

    private void createNFloors(int floorCount) {
        this.floors = IntStream.range(1, floorCount + 1)
                .mapToObj(Floor::new)
                .collect(toList());

        this.floors.forEach(floor -> {
            if (floor.getNumber() != 1) {
                floor.addDownButton();
            }
            if (floor.getNumber() != floorCount) {
                floor.addUpButton();
            }
        });
    }

    private int getNumberOfFloors() {
        return this.floors.size();
    }

    private Floor getFloor(int number) {
        return this.floors.get(number - 1);
    }

    public void pressUp(int yourFloor) {
        Floor currentFloor = getFloor(yourFloor);
        Elevator elev = getFreeElevator(currentFloor.getNumber());

        if (elev == null) {
            System.out.println("No free elevators at the moment!");
        } else {
            if (yourFloor != getNumberOfFloors()) {
                currentFloor.pressUpButton();
                elev.setDestinationUp(yourFloor);
            } else {
                currentFloor.pressUpButton();
            }
        }
    }

    public void pressDown(int yourFloor) {
        Floor currentFloor = getFloor(yourFloor);
        Elevator elev = getFreeElevator(currentFloor.getNumber());

        if (elev == null) {
            System.out.println("No free elevators at the moment!");
        } else {
            if (yourFloor != 1) {
                currentFloor.pressDownButton();
                elev.setDestinationDown(yourFloor);
            } else {
                currentFloor.pressDownButton();
            }
        }
    }

    public void enterElevator(int currentFloor) {
        Elevator elev = getWaitingElevator(currentFloor);
        if (elev != null) {
            selectFloor(elev, currentFloor);
        } else {
            System.out.println("Did you call an elevator?");
        }
    }

    private void selectFloor(Elevator elev, int currentFloor) {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Select floor?");
        int destination = keyboard.nextInt();

        boolean up = elev.isDestinationUp();
        String message = up ? "Elevator is going up!" : "Elevator is going down!";

        if (isFloorInBuilding(destination)) {
            if (up && currentFloor <= destination) {
                elev.setDestination(destination);
            } else if (!up && currentFloor >= destination) {
                elev.setDestination(destination);
            } else {
                System.out.println(message);
                selectFloor(elev, currentFloor);
            }
        } else {
            selectFloor(elev, currentFloor);
        }
    }

    private Elevator getFreeElevator(int floor) {
        Comparator<Elevator> byDistance =
                Comparator.comparingInt(e -> abs(e.getCurrentFloor() - floor));
        return elevators.stream()
                .filter(Elevator::isPassive)
                .min(byDistance)
                .orElse(null);
    }

    private Elevator getWaitingElevator(int floor) {
        return elevators.stream()
                .filter(Elevator::isWaiting)
                .filter(elevator -> elevator.getCurrentFloor() - floor == 0)
                .findFirst()
                .orElse(null);
    }

    boolean isFloorInBuilding(int floor) {
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
