package display;

import input.Input;
import sim.Sim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class InfoPanel extends JPanel {
    Sim sim;
    Dimension dimension;

    public InfoPanel (Sim sim, Input input) {
        this.sim = sim;
        dimension = new Dimension(250,sim.getMap().getMapHeight());
        setPreferredSize(dimension);
        setBackground(Color.gray);
        setDoubleBuffered(true);
    }

    public void paintComponent (Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        graphics2D.dispose();
    }

}
