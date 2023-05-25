package entity;

import ai.FollowPath;
import ai.FollowPathToExit;
import controller.EntityController;
import core.Motion;
import core.Size;
import sim.Sim;

import java.awt.*;

public class Human extends Agent{

    FollowPath followPath;
    public Human(Sim sim, EntityController entityController) {
        super(entityController);
        setSize(new Size(6,6));
        setPosition(sim.getMap().getRandomPosition());
        this.motion = new Motion(1);

        followPath = new FollowPathToExit();
    }

    @Override
    public void update(Sim sim) {
        followPath.update(this, sim);
        super.update(sim);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.BLUE);
        graphics2D.fillRect(getCenterPosition().intX(), getCenterPosition().intY(), size.getWidth(), size.getHeight());
    }

}
