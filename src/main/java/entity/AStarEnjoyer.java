package entity;

import ai.FollowPath;
import ai.FollowPathAsToPlayer;
import controller.EntityController;
import core.Motion;
import core.Position;
import core.Size;
import sim.Sim;

import java.awt.*;

public class AStarEnjoyer extends Agent {

    FollowPath followPath;
    public AStarEnjoyer(Sim sim, EntityController entityController) {
        super(entityController);
        setPosition(sim.getMap().getRandomPosition());
        position.setX(sim.getMap().getTileSize() * 38);
        setSize(new Size(10,10));
        this.motion = new Motion(5);
        followPath = new FollowPathAsToPlayer();
    }

    @Override
    public void update(Sim sim) {
        followPath.update(this,sim);
        super.update(sim);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.RED);
        graphics2D.fillRect(getPosition().intX(), getPosition().intY(), getSize().getWidth(), getSize().getHeight());
    }
}
