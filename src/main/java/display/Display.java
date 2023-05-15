package display;

import input.Input;
import sim.Sim;

import javax.swing.*;
import java.awt.*;

public class Display extends JFrame {
    private final SimPanel simPanel;
    public Display(Input input, Sim sim){
        setTitle("title");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        Renderer renderer = new Renderer();
        simPanel = new SimPanel(sim, input);

        add(simPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void render(Sim sim){
        simPanel.repaint();
    }
}
