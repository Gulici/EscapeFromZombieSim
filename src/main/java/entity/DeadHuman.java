package entity;

import controller.EntityController;
import sim.Sim;

import java.awt.*;

/**
 * Class that is responsible for dead humans.
 */
public class DeadHuman extends Agent{
    public DeadHuman(EntityController entityController) {
        super(entityController);
    }

    @Override
    public void update(Sim sim) {
        handleCollisions(sim);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.red);
        graphics2D.fillRect(position.intX(), position.intY(), size.getWidth(), size.getHeight());
    }

    public void kill() {}
}
