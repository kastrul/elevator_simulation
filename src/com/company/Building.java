package com.company;

import javax.swing.*;

public class Building {
    private ElevatorGUI GUI;


    Building(int elevators, int floors, int timeStep) {
        ElevatorSystem system = new ElevatorSystem();
        system.installSystem(elevators, floors, timeStep);
        this.GUI = new ElevatorGUI(system);
    }


    public void enterBuildingGUI() {
        JFrame frame = new JFrame("ElevatorGUI");
        frame.setContentPane(GUI.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        this.GUI.start();
    }

}
