package display;

import sim.Sim;
import sim.SimState;

import javax.swing.*;
import java.awt.*;

/**
 * JFrame class that is handling GUI
 */
public class Display extends JFrame {
    private final SimPanel simPanel;
    private final InfoPanel infoPanel;
    public Display(Sim sim, SimState simState){
        setTitle("title");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        Renderer renderer = new Renderer();
        simPanel = new SimPanel(sim);
        infoPanel = new InfoPanel(sim, simState);

        add(infoPanel, BorderLayout.WEST);
        add(simPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void render(Sim sim){
        simPanel.repaint();
    }
}
