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
    JButton startButton, stopButton, setButton;

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
        startButton.setText("Start");
        startButton.setEnabled(false);
        add(startButton);

        stopButton = new JButton();
        stopButton.setVisible(true);
        stopButton.setBounds(10,70, 100, 50);
        stopButton.addActionListener(this);
        stopButton.setText("Stop");
        stopButton.setEnabled(false);
        add(stopButton);

        setButton = new JButton();
        setButton.setVisible(true);
        setButton.setBounds(10,130, 100, 50);
        setButton.addActionListener(this);
        setButton.setText("Set");
        setButton.setEnabled(true);
        add(setButton);
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

            stopButton.setEnabled(true);
            startButton.setEnabled(false);
            setButton.setEnabled(false);

        }
        if (e.getSource().equals(stopButton)) {
            simState.setRunning(false);
            simState.setWasStopped(true);

            startButton.setEnabled(true);
            setButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
        if (e.getSource().equals(setButton)) {
            simState.setSetSim(true);

            startButton.setEnabled(true);
        }
    }
}
