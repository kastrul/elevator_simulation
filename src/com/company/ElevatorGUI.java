package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ElevatorGUI {
    private JPanel elevatorsPanel;
    private JSpinner yourFloor;
    private JPanel elevatorPanel;
    private JPanel buttonPanel;
    private JButton buttonUp;
    private JButton buttonDown;
    private JSpinner destinationFloor;
    private JButton buttonDestination;
    private JRadioButton eButton1;
    private JRadioButton eButton2;
    private JRadioButton eButton3;
    private JTextArea eLabel1;
    private JTextArea eLabel2;
    private JTextArea eLabel3;
    JPanel mainPanel;
    private JPanel floorPanel;
    private JTextArea messageArea;
    private ButtonGroup buttonGroup1;
    private int currentFloor;

    private ElevatorSystem elevSys;

    public ElevatorGUI(ElevatorSystem elevSys) {
        this.elevSys = elevSys;

    }

    public void start() {
        String labelText = String.format("Floor: %d/%d\nState: %s\nDir: %s"
                , 1, this.elevSys.getNumberOfFloors(), "PASSIVE", "NONE");
        eLabel1.setText(labelText);
        eLabel2.setText(labelText);
        eLabel3.setText(labelText);
        ((JSpinner.DefaultEditor) yourFloor.getEditor()).getTextField().setEditable(false);
        yourFloor.setValue(1);
        adjustButtons();
        addListeners();
    }

    private void addListeners() {
        yourFloor.addChangeListener(e -> adjustButtons());

        buttonUp.addActionListener((ActionEvent e) -> {
            Elevator elev = this.elevSys.getFreeElevator(currentFloor);
            this.elevSys.pressUp(currentFloor, elev);
            updateMessage();

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> updateElevatorLocation(elev));
        });

        buttonDown.addActionListener(e -> {
            Elevator elev = this.elevSys.getFreeElevator(currentFloor);
            this.elevSys.pressDown(currentFloor, elev);
            updateMessage();

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> updateElevatorLocation(elev));
        });

        buttonDestination.addActionListener(e -> {
            int destination = (Integer) destinationFloor.getValue();
            int selectedElev = Integer.parseInt(buttonGroup1.getSelection().getActionCommand());
            if (elevSys.goToFloor(selectedElev, destination, currentFloor)) {
                Elevator elev = elevSys.getElevator(selectedElev);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(() -> updateElevatorLocation(elev));
            }
            updateMessage();
        });
    }

    private void adjustButtons() {
        this.currentFloor = (Integer) yourFloor.getValue();
        if (currentFloor < 1) {
            yourFloor.setValue(1);
        } else if (currentFloor > elevSys.getNumberOfFloors()) {
            yourFloor.setValue(elevSys.getNumberOfFloors());
        } else if (currentFloor == 1) {
            buttonDown.setEnabled(false);
            buttonUp.setEnabled(true);
            buttonDestination.setEnabled(true);
        } else if (currentFloor == elevSys.getNumberOfFloors()) {
            buttonUp.setEnabled(false);
            buttonDown.setEnabled(true);
            buttonDestination.setEnabled(true);
        } else {
            buttonUp.setEnabled(true);
            buttonDown.setEnabled(true);
            buttonDestination.setEnabled(true);
        }
    }

    private void updateElevatorLocation(Elevator elev) {
        int elevNr = elev.getNumber();
        while (elev.isMoving()) {
            setElevatorLabel(elev, elevNr);
        }
        setElevatorLabel(elev, elevNr);
    }

    private void setElevatorLabel(Elevator elev, int elevNr) {
        String labelText = String.format("Floor: %d/%d\nState: %s\nDir: %s"
                , elev.getCurrentFloor(), this.elevSys.getNumberOfFloors(), elev.getState(), elev.getDirection());
        switch (elevNr) {
            case 1:
                eLabel1.setText(labelText);
                break;
            case 2:
                eLabel2.setText(labelText);
                break;
            case 3:
                eLabel3.setText(labelText);
                break;
        }
    }

    private void updateMessage() {
        messageArea.setText(elevSys.getMESSAGE());
    }
}
