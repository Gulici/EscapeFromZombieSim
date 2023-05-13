package entity;

import controller.EntityController;
import core.CollisionBox;
import core.Position;
import sim.Sim;

import java.awt.*;

public class ManualAgent extends Agent {
    public ManualAgent(EntityController entityController) {
        super(entityController);
        setPosition(new Position( 50,50));
    }

    @Override
    public void draw(Graphics2D graphics2D){
        graphics2D.setColor(Color.white);
        graphics2D.fillRect(position.intX(), position.intY(), size.getWidth(), size.getHeight());
        graphics2D.setColor(Color.BLUE);
        graphics2D.drawLine(position.intX() + size.getWidth()/2, position.intY(), position.intX() + size.getWidth()/2, position.intY() + size.getHeight());
        graphics2D.drawLine(position.intX(), position.intY() + size.getHeight()/2, position.intX() + size.getWidth(), position.intY() + size.getHeight()/2);
    }
}
