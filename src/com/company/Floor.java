package com.company;

public class Floor {

    private int number;
    private ElevatorButton buttonUp = null;
    private ElevatorButton buttonDown = null;

    public Floor(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void addUpButton() {
        this.buttonUp = new ElevatorButton();
    }

    public void addDownButton() {
        this.buttonDown = new ElevatorButton();
    }

    public void pressUpButton() {
        String message = String.format("Pressed up on floor %d", getNumber());
        String pressed = buttonUp != null ? message : "Last floor! No up button.";
        System.out.println(pressed);
    }

    public void pressDownButton() {
        String message = String.format("Pressed down on floor %d", getNumber());
        String pressed = buttonDown != null ? message : "First floor! No down button.";
        System.out.println(pressed);
    }
}
