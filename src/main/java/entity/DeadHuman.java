package entity;

import controller.EntityController;

import java.awt.*;

public class DeadHuman extends Agent{
    public DeadHuman(EntityController entityController) {
        super(entityController);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.red);
        graphics2D.fillRect(position.intX(), position.intY(), size.getWidth(), size.getHeight());
    }

    public void kill() {}
}
