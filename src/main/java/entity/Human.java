package entity;

import ai.FollowPath;
import ai.FollowPathToExit;
import ai.FollowToHuman;
import controller.EntityController;
import java.lang.Comparable;
import core.Motion;
import core.Size;
import sim.Sim;
import core.Group;
import core.Position;
import java.awt.*;

public class Human extends Agent implements Comparable<Human> {

    protected FollowPath followPath;
    Group group;
    protected Color color;
    private double speed = 1;
    private String state;
    private int knockOverCounter;
    private int pushCounter;
    private boolean alive = true;
    public Human(Sim sim, EntityController entityController) {
        super(entityController);
        setSize(new Size(6,6));
        setPosition(sim.getMap().getRandomPosition());
        color = Color.CYAN;
        pushCounter = 0;
        knockOverCounter = 0;
        state = "FollowPath";
        this.motion = new Motion(this.speed);
        followPath = new FollowPathToExit();
        group = new Group(this);
    }

    @Override
    public int compareTo(Human h) {
        return followPath.getLength() - h.getPathLength();
    }

    public void calculateSpeed() {
        Position mean = group.meanPosition();
        double distance = this.getCenterPosition().distanceTo(mean);
        double new_speed = this.speed - 0.05 *distance;
        if (new_speed > 0) {
          this.motion = new Motion(new_speed);
        }
        else
          this.motion = new Motion(0);
    }

    @Override
    public void update(Sim sim) {
        updateState();
        switch (state) {
            case "FollowPath" -> {
                if (followPath instanceof FollowToHuman)
                    ((FollowToHuman)followPath).setTarget(group.first());
                followPath.update(this, sim); handleMotion();
                super.update(sim);
            }
            case "KnockOver" -> {}
            case "Trampled" -> {
                alive = false;
                sim.addToKillList(this);
            }
        }
    }

    public void updateTarget() {
        followPath = new FollowToHuman(group.first());
    }

    private void updateState() {
        switch (state) {
            case "FollowPath" -> {
                if (pushCounter >= 5) {
                    state = "KnockOver";
                    knockOverCounter = 60;
                    color = Color.YELLOW;
                }
            }
            case "KnockOver" -> {
                if (pushCounter >= 20) {
                    state = "Trampled";
                    break;
                }
                if (knockOverCounter > 0) {
                    knockOverCounter --;
                }
                else {
                    state = "FollowPath";
                    pushCounter = 0;
                    color = Color.cyan;
                }
            }
            default -> state = "FollowPath";
        }
    }

    public void testCollision(Entity other, Sim sim) {
        super.handleCollision(other, sim);
    }

    @Override
    public void handleCollision(Entity other, Sim sim) {
        super.handleCollision(other, sim);
        if(other instanceof Human) {
            Human other_human = (Human)other;
            color = Color.BLUE;
            if(!this.group.contains(other_human)) {
                Group merged = group.merge(other_human.getGroup());
                this.group = merged;
                other_human.setGroup(merged);
            }
            followPath = new FollowToHuman(group.first());
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
        graphics2D.setColor(color);
        graphics2D.fillRect(getCenterPosition().intX(), getCenterPosition().intY(), size.getWidth(), size.getHeight());
    }

    public void increasePushCounter() {
        pushCounter++;
    }

    public int getPathLength() {
        return followPath.getLength();
    }

    public boolean isAlive(){
        return alive;
    }
}
