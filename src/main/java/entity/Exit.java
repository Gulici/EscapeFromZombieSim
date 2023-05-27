package entity;

import sim.Sim;

import java.awt.*;
import java.util.List;

public class Exit extends Entity {

    public Exit() {
        super();
    }

    @Override
    public void update(Sim sim) {
        handleCollisions(sim);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.lightGray);
        graphics2D.fillRect(position.intX(), position.intY(), size.getWidth(), size.getHeight());    }

    @Override
    public boolean collidesWith(Entity other) {
        return this.getCollisionBox().collidesWith(other.getCollisionBox());
    }
    protected void handleCollision(Entity other, Sim sim){
        if (other instanceof Human) {
            sim.addToEscapeList((Agent) other);
        }
    }

    protected void handleCollisions(Sim sim){
        List<Entity> collidingEntities = sim.getCollidingEntities(this);

        for(Entity other : collidingEntities){
            this.handleCollision(other, sim);
        }
    }
}
