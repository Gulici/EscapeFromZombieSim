package display;

import sim.Sim;
import sim.SimState;
import sim.configuration.HumanConf;
import sim.configuration.ZombieConf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

/**
 * Panel that contain components for management of simulation settings.
 */
public class InfoPanel extends JPanel implements ActionListener {
    Sim sim;
    SimState simState;
    Dimension dimension;
    JButton startButton, stopButton, setButton;
    JSlider zombieDamage, humanHP, damageToChange, humanSpeed, zombieSpeed, zombieRange, humanDamage, zombieHP, numberOfHumans, numberOfZombies;
    JCheckBox showZombieRange, showZombiePath;

    public InfoPanel (Sim sim, SimState simState) {
        this.sim = sim;
        this.simState = simState;
        dimension = new Dimension(250,sim.getMap().getMapHeight());
        setPreferredSize(dimension);
        setBackground(Color.gray);
        setDoubleBuffered(true);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        startButton = new JButton();
        startButton.setVisible(true);
        startButton.setMaximumSize(new Dimension(100, 30));
        //startButton.setBounds(10,  10, 100, 50);
        startButton.addActionListener(this);
        startButton.setText("Start");
        startButton.setEnabled(false);
        add(startButton);

        stopButton = new JButton();
        stopButton.setMaximumSize(new Dimension(100, 30));
        stopButton.setVisible(true);
        stopButton.addActionListener(this);
        stopButton.setText("Stop");
        stopButton.setEnabled(false);
        add(stopButton);

        setButton = new JButton();
        setButton.setMaximumSize(new Dimension(100, 30));
        setButton.setVisible(true);
        setButton.addActionListener(this);
        setButton.setText("Set");
        setButton.setEnabled(true);
        add(setButton);

         JLabel numberOfHumansLabel = new JLabel("Number of humans");
        add(numberOfHumansLabel);
        numberOfHumans = new JSlider(0, 100, 100);
        numberOfHumans.setVisible(true);
        numberOfHumans.setEnabled(true);
        numberOfHumans.setPaintLabels(true);
        numberOfHumans.setLabelTable(numberOfHumans.createStandardLabels(10));
        add(numberOfHumans);

        JLabel numberOfZombiesLabel = new JLabel("Number of zombies");
        add(numberOfZombiesLabel);
        numberOfZombies = new JSlider(0, 100, 20);
        numberOfZombies.setVisible(true);
        numberOfZombies.setEnabled(true);
        numberOfZombies.setPaintLabels(true);
        numberOfZombies.setLabelTable(numberOfZombies.createStandardLabels(10));
        add(numberOfZombies);

        JLabel zombieDamageLabel = new JLabel("Damage implied by zombie");
        add(zombieDamageLabel);
        zombieDamage = new JSlider(0, 1000, 300);
        zombieDamage.setVisible(true);
        zombieDamage.setEnabled(true);
        zombieDamage.setPaintLabels(true);
        zombieDamage.setLabelTable(zombieDamage.createStandardLabels(200));
        add(zombieDamage);

        JLabel humanDamageLabel = new JLabel("Damage implied by human");
        add(humanDamageLabel);
        humanDamage = new JSlider(0, 1000, 300);
        humanDamage.setVisible(true);
        humanDamage.setEnabled(true);
        humanDamage.setPaintLabels(true);
        humanDamage.setLabelTable(humanDamage.createStandardLabels(200));
        add(humanDamage);
        
        JLabel humanHPLabel = new JLabel("Human HP");
        add(humanHPLabel);
        humanHP = new JSlider(1, 1000, 1000);
        humanHP.setVisible(true);
        humanHP.setBounds(10, 370, 200, 50);
        humanHP.setEnabled(true);
        humanHP.setPaintLabels(true);
        humanHP.setLabelTable(humanHP.createStandardLabels(200));
        add(humanHP);

        JLabel zombieHPLabel = new JLabel("Zombie HP");
        add(zombieHPLabel);
        zombieHP = new JSlider(1, 1000, 1000);
        zombieHP.setVisible(true);
        zombieHP.setBounds(10, 370, 200, 50);
        zombieHP.setEnabled(true);
        zombieHP.setPaintLabels(true);
        zombieHP.setLabelTable(zombieHP.createStandardLabels(200));
        add(zombieHP);

        JLabel damageToChangeLabel = new JLabel("Minimum damage at once to change poison human");
        damageToChangeLabel.setBounds(10, 430, 200, 50);
        add(damageToChangeLabel);
        damageToChange = new JSlider(0, 1000, 100);
        damageToChange.setVisible(true);
        damageToChange.setBounds(10, 490, 200, 50);
        damageToChange.setEnabled(true);
        damageToChange.setPaintLabels(true);
        damageToChange.setLabelTable(damageToChange.createStandardLabels(200));
        add(damageToChange);

        JLabel humanSpeedLabel = new JLabel("Human speed");
        humanSpeedLabel.setBounds(10, 430, 200, 50);
        add(humanSpeedLabel);
        humanSpeed = new JSlider(0, 20, 10);
        humanSpeed.setVisible(true);
        humanSpeed.setBounds(10, 490, 200, 50);
        humanSpeed.setEnabled(true);
        humanSpeed.setPaintLabels(true);
        Hashtable<Integer, JLabel> values = new Hashtable<Integer, JLabel>();
        for(int i = 0; i <= 20; i+=2) {
            values.put(i, new JLabel(String.valueOf((double)i/10)));
        }
        humanSpeed.setLabelTable(values);
        add(humanSpeed);

        JLabel zombieSpeedLabel = new JLabel("Zombie speed");
        zombieSpeedLabel.setBounds(10, 430, 200, 50);
        add(zombieSpeedLabel);
        zombieSpeed = new JSlider(0, 20, 10);
        zombieSpeed.setVisible(true);
        zombieSpeed.setBounds(10, 490, 200, 50);
        zombieSpeed.setEnabled(true);
        zombieSpeed.setPaintLabels(true);
        zombieSpeed.setLabelTable(values);
        add(zombieSpeed);

        JLabel zombieRangeLabel = new JLabel("Range where zombie sees");
        zombieRangeLabel.setBounds(10, 430, 200, 50);
        add(zombieRangeLabel);
        zombieRange = new JSlider(0, 50, 50);
        zombieRange.setVisible(true);
        zombieRange.setBounds(10, 490, 200, 50);
        zombieRange.setEnabled(true);
        zombieRange.setPaintLabels(true);
        zombieRange.setLabelTable(zombieRange.createStandardLabels(5));
        add(zombieRange);

        showZombieRange = new JCheckBox("Show zombie range", true);
        add(showZombieRange);

        showZombiePath = new JCheckBox("Show zombie path", false);
        add(showZombiePath);
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
            ZombieConf.damage = zombieDamage.getValue();
            ZombieConf.defaultSpeed = (double)zombieSpeed.getValue()/10;
            ZombieConf.range = zombieRange.getValue();
            ZombieConf.showRange = showZombieRange.isSelected();
            ZombieConf.showPath = showZombiePath.isSelected();
            ZombieConf.hp = zombieHP.getValue();

            HumanConf.hp = humanHP.getValue();
            HumanConf.damageToChange = damageToChange.getValue();
            HumanConf.defaultSpeed = (double)humanSpeed.getValue()/10;
            HumanConf.damage = humanDamage.getValue();

            simState.numberOfHumans = numberOfHumans.getValue();
            simState.numberOfZombies = numberOfZombies.getValue();
            simState.setSetSim(true);

            startButton.setEnabled(true);
        }
    }
}
