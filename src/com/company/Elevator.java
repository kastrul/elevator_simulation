package com.company;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.company.ElevatorDirection.DOWN;
import static com.company.ElevatorDirection.NONE;
import static com.company.ElevatorDirection.UP;
import static com.company.ElevatorState.*;

public class Elevator {
    private ElevatorState state;
    private ElevatorDirection direction;
    private int number;
    private int timeStep;
    private int currentFloor = 1;
    private int destinationFloor = 1;
    private ExecutorService executor = Executors.newSingleThreadExecutor();


    Elevator(int number, int timeStep) {
        this.state = PASSIVE;
        this.direction = NONE;
        this.number = number;
        this.timeStep = timeStep;

    }

    public ElevatorDirection getDirection() {
        return direction;
    }

    public String getState() {
        return this.state.toString();
    }

    public boolean isPassive() {
        return this.state == PASSIVE;
    }

    public boolean isWaiting() {
        return this.state == WAITING;
    }

    public boolean isMoving() {
        return this.state == MOVING || this.state == MOVING_FULL;
    }

    public boolean isDestinationUp() {
        return this.direction == UP;
    }

    public int getNumber() {
        return number;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setDestinationUp(int floor) {
        this.state = MOVING;
        this.direction = UP;
        this.destinationFloor = floor;
        moveToDestination();
    }

    public void setDestinationDown(int floor) {
        this.state = MOVING;
        this.direction = DOWN;
        this.destinationFloor = floor;
        moveToDestination();
    }

    public void setDestination(int floor) {
        this.state = MOVING_FULL;
        this.destinationFloor = floor;
        moveToDestination();
    }

    private void moveToDestination() {
        String msg = String.format(
                "   Elevator number %d ordered to %d.", this.number, this.destinationFloor);
        System.out.println(msg);
        moveElevator();
    }

    private void moveElevator() {
        String message = String.format("    Elevator %d : Floor %d", number, getCurrentFloor());
        System.out.println(message);

        if (currentFloor != destinationFloor) {
            executor.submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(this.timeStep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean destinationUp = destinationFloor > currentFloor;
                int i = destinationUp ? 1 : -1;
                this.currentFloor = this.currentFloor + i;
                moveElevator();
            });
        } else {
            arrival();
        }
    }

    private void arrival() {
        String message = String.format(
                "   Elevator %d waiting on floor %d", this.number, this.currentFloor);
        System.out.println(message);
        if (this.state == MOVING_FULL) {
            this.state = PASSIVE;
            this.direction = NONE;
        } else {
            this.state = WAITING;
        }
    }

}
