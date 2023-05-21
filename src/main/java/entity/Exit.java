package entity;

import sim.Sim;

import java.awt.*;

public class Exit extends Entity {

    public Exit() {
        super();
    }

    @Override
    public void update(Sim sim) {

    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.lightGray);
        graphics2D.fillRect(position.intX(), position.intY(), size.getWidth(), size.getHeight());    }

    @Override
    public boolean collidesWith(Entity entity) {
        return false;
    }
}
