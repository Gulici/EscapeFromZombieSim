package entity;

import core.CollisionBox;
import sim.Sim;

import java.awt.*;

public class Wall extends Entity{

    public Wall() {
        super();
    }

    @Override
    public void update(Sim sim) {

    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.darkGray);
        graphics2D.fillRect(position.intX(), position.intY(), size.getWidth(), size.getHeight());
        CollisionBox collisionBox = getCollisionBox();
        graphics2D.setColor(Color.red);
        graphics2D.drawRect(collisionBox.getBounds().x,collisionBox.getBounds().y,collisionBox.getBounds().width,collisionBox.getBounds().height);
    }

    @Override
    public boolean collidesWith(Entity other) {
        return true;
    }
}
