package display;

import input.Input;
import sim.Sim;

import javax.swing.*;
import java.awt.*;

public class Display extends JFrame {

    private Canvas canvas;
    private Renderer renderer;
    private SimPanel simPanel;



    public Display(Input input, Sim sim){
        setTitle("title");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        //addKeyListener(input);

        renderer = new Renderer();
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
