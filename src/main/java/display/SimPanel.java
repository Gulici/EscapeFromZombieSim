package display;

import entity.Agent;
import input.Input;
import sim.Sim;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimPanel extends JPanel {
    Sim sim;
    Renderer renderer;
    Dimension dimension;

    public SimPanel(Sim sim, Input input){
        this.sim = sim;
        renderer = new Renderer();
        dimension = new Dimension(sim.getMap().getMapWidth(),sim.getMap().getMapHeight());
        setPreferredSize(dimension);
        setBackground(Color.black);
        setDoubleBuffered(true);
        setFocusable(true);
        addKeyListener(input);
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics;
        sim.getMap().draw(g2);
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
