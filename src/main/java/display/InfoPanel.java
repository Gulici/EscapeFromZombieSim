package display;

import input.Input;
import sim.Sim;
import sim.SimState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InfoPanel extends JPanel implements ActionListener {
    Sim sim;
    SimState simState;
    Dimension dimension;
    JButton startButton;
    JButton stopButton;

    public InfoPanel (Sim sim, Input input, SimState simState) {
        this.sim = sim;
        this.simState = simState;
        dimension = new Dimension(250,sim.getMap().getMapHeight());
        setPreferredSize(dimension);
        setBackground(Color.gray);
        setDoubleBuffered(true);
        setLayout(null);

        startButton = new JButton();
        startButton.setVisible(true);
        startButton.setBounds(10,  10, 100, 50);
        startButton.addActionListener(this);
        Label startLabel = new Label("Start");
        startButton.add(startLabel);
        add(startButton);

        stopButton = new JButton();
        stopButton.setVisible(true);
        stopButton.setBounds(10,120, 100, 50);
        stopButton.addActionListener(this);
        Label stopLabel = new Label("Stop");
        stopButton.add(stopLabel);
        add(stopButton);
    }

//    public void paintComponent (Graphics graphics) {
//        super.paintComponent(graphics);
//        Graphics2D graphics2D = (Graphics2D) graphics;
//
//
//
//        graphics2D.dispose();
//    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource().equals(startButton)) {
            simState.setRunning(true);
            simState.setWasStopped(true);
        }
        if (e.getSource().equals(stopButton)) {
            simState.setRunning(false);
        }

    }
}
