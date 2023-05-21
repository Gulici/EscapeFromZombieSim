package entity;

import ai.FollowPath;
import ai.FollowPathAsToExit;
import ai.FollowToHuman;
import controller.EntityController;
import controller.AgentController;
import core.Motion;
import core.Size;
import sim.Sim;
import core.Group;
import core.Position;
import java.util.Random;
import java.awt.*;

public class Human extends Agent{

    FollowPath followPath;
    Group group;
    public Color color;
    private int ticksPathChange;
    public Human(Sim sim, EntityController entityController) {
        super(entityController);
        Random rand = new Random();
        rand.setSeed(sim.getMap().getRandomPosition().intX());
        
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        
        color = new Color(r, g, b);
        setSize(new Size(6,6));
        setPosition(sim.getMap().getRandomPosition());
        this.motion = new Motion(1);
        followPath = new FollowPathAsToExit();
        group = new Group(this);
    }

    @Override
    public void update(Sim sim) {
        if(ticksPathChange == 30) {
            if(group.getLeader() != this) {
                ticksPathChange = 0;
                followPath = new FollowToHuman(group.getLeader());
            }
            else {
                Position mean = group.meanPosition();
                if(this.getCenterPosition().distanceTo(mean) > 5) {
                    ((AgentController)entityController).stop();
                }
            }
        }
        followPath.update(this, sim);
        super.update(sim);
        ticksPathChange++;
    }

    @Override
    public void handleCollision(Entity other) {
        super.handleCollision(other);
        if(other instanceof Human) {
            Human other_human = (Human)other;
            if(!this.group.contains(other_human)) {
                Group merged = group.merge(other_human.getGroup());
                this.group = merged;
                other_human.setGroup(merged);
            }
        }
    }

    public void setGroup(Group g) {
        group = g;
    }
    public Group getGroup() {
        return group;
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(group.getLeader().color);
        graphics2D.fillRect(getCenterPosition().intX(), getCenterPosition().intY(), size.getWidth(), size.getHeight());
    }

    public int getPathLength() {
        return followPath.getLength();
    }
}
