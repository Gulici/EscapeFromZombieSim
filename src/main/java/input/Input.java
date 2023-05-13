package input;

import core.Position;

import java.awt.event.*;

public class Input implements KeyListener{
    private boolean[] pressed;

    public Input(){
        pressed = new boolean[255];
    }

    public boolean isPressed(int keyCode){
        return pressed[keyCode];
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        pressed[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressed[e.getKeyCode()] = false;
    }
}
