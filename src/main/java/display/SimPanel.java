package display;

import entity.Agent;
import sim.Sim;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panel that is visualizing process of simulation.
 */
public class SimPanel extends JPanel {
    Sim sim;
    Renderer renderer;
    Dimension dimension;

    public SimPanel(Sim sim){
        this.sim = sim;
        renderer = new Renderer();
        dimension = new Dimension(sim.getMap().getMapWidth(),sim.getMap().getMapHeight());
        setPreferredSize(dimension);
        setBackground(Color.black);
        setDoubleBuffered(true);
        setFocusable(true);
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics;
        renderer.render(sim.getEntityList(),g2);
        List<Agent> dead = sim.getDeadAgents();
        synchronized (dead) {
            for (Agent deadAgent : sim.getDeadAgents()) {
                deadAgent.draw(g2);
            }
        }
        g2.dispose();
    }
}
