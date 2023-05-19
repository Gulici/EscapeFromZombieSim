package display;

import input.Input;
import sim.Sim;

import javax.swing.*;
import java.awt.*;

public class Display extends JFrame {
    private final SimPanel simPanel;
    private final InfoPanel infoPanel;
    public Display(Input input, Sim sim){
        setTitle("title");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        Renderer renderer = new Renderer();
        simPanel = new SimPanel(sim, input);
        infoPanel = new InfoPanel(sim, input);

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
