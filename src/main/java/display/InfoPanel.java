package display;

import input.Input;
import sim.Sim;
import sim.SimState;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class InfoPanel extends JPanel implements ActionListener, ChangeListener{
    Sim sim;
    SimState simState;
    Dimension dimension;
    JButton startButton, stopButton, setButton;
    JLabel labelPeople, labelZombie;
    JSlider sliderPeople,sliderZombie;

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
        
        labelPeople = new JLabel();
        labelPeople.setVisible(true);
        labelPeople.setText("Number of survivors");
        labelPeople.setBounds(10, 200,220,50);
        labelPeople.setFont(new Font("Arial",Font.BOLD,15));
        add(labelPeople);
        
        sliderPeople = new JSlider(0,100,25);
        sliderPeople.setVisible(true);
        sliderPeople.setBounds(10, 240, 200, 50);
        sliderPeople.setPaintTicks(true);
        sliderPeople.setBackground(Color.gray);
        sliderPeople.setPaintTrack(true);
        sliderPeople.setMajorTickSpacing(20);
        sliderPeople.setPaintLabels(true);
        sliderPeople.addChangeListener(this);
        add(sliderPeople);
        
//        labelZombie = new JLabel();
//        labelZombie.setVisible(true);
//        labelZombie.setText("Number of zombies");
//        labelZombie.setBounds(10, 300,160,50);
//        labelZombie.setFont(new Font("Arial",Font.BOLD,15));
//        add(labelZombie);
//        
//        sliderZombie = new JSlider();
//        sliderZombie.setVisible(true);
//        sliderZombie.setBounds(10, 340, 200, 50);
//        sliderZombie.setPaintTicks(true);
//        sliderZombie.setBackground(Color.gray);
//        sliderZombie.setPaintTrack(true);
//        sliderZombie.setMajorTickSpacing(20);
//        sliderZombie.setPaintLabels(true);
//        add(sliderZombie);
//        
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
    public void stateChanged(ChangeEvent e) {
    	simState.setNumberOfHumans(sliderPeople.getValue());
    	labelPeople.setText("Number of survivors - "+sliderPeople.getValue());
    	    
    }

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
