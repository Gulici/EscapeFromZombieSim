package display;

import input.Input;
import sim.Sim;
import sim.SimState;
import entity.Zombie;
import entity.Human;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InfoPanel extends JPanel implements ActionListener {
    Sim sim;
    SimState simState;
    Dimension dimension;
    JButton startButton, stopButton, setButton;
    JSlider zombieDamage, humanHP, damageToChange;

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

        JLabel zombieDamageLabel = new JLabel("Damage implied by zombie");
        zombieDamageLabel.setBounds(10, 190, 200, 50);
        add(zombieDamageLabel);
        zombieDamage = new JSlider(0, 1000, 300);
        zombieDamage.setVisible(true);
        zombieDamage.setBounds(10,250, 200, 50);
        zombieDamage.setEnabled(true);
        zombieDamage.setPaintLabels(true);
        zombieDamage.setLabelTable(zombieDamage.createStandardLabels(200));
        add(zombieDamage);
        
        JLabel humanHPLabel = new JLabel("Human HP");
        humanHPLabel.setBounds(10, 310, 200, 50);
        add(humanHPLabel);
        humanHP = new JSlider(1, 1000, 1000);
        humanHP.setVisible(true);
        humanHP.setBounds(10, 370, 200, 50);
        humanHP.setEnabled(true);
        humanHP.setPaintLabels(true);
        humanHP.setLabelTable(humanHP.createStandardLabels(200));
        add(humanHP);

        JLabel damageToChangeLabel = new JLabel("Minimum damage at once to change poison human");
        damageToChangeLabel.setBounds(10, 430, 200, 50);
        add(damageToChangeLabel);
        damageToChange = new JSlider(0, 1000, 1000);
        damageToChange.setVisible(true);
        damageToChange.setBounds(10, 490, 200, 50);
        damageToChange.setEnabled(true);
        damageToChange.setPaintLabels(true);
        damageToChange.setLabelTable(damageToChange.createStandardLabels(200));
        add(damageToChange);
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
            Zombie.setDamage(zombieDamage.getValue());
            Human.setHP(humanHP.getValue());
            Human.setDamageToChange(damageToChange.getValue());
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
