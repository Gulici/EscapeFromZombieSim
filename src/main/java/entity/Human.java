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

public class Human extends Agent {

    protected FollowPath followPath;
    Group group;
    protected Color color;
    private double speed = 1;
    private String state;
    private int knockOverCounter;
    private int pushCounter;
    private int hp = 1000;
    private boolean alive = true;
    private Sim sim;
    private int zombificationCounter = 60;
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
        this.sim = sim;
    }

    public void damage(int total_damage) {
        this.group.damage(total_damage);
    }

    public boolean isZombified() {
        return state == "Zombified";
    }

    public void decreaseHP(int damage) {
        hp -= damage;
        if (hp < 0) {
            sim.addToKillList(this);
            return;
        }
        if (damage > 100) {
            state = "Zombified";
        }
    }


    public void calculateSpeed() {
        Position mean = group.meanPosition();
        double distance = this.getCenterPosition().distanceTo(mean);
        double new_speed = this.speed - 0.005 *distance;
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
                if (followPath instanceof FollowToHuman) {
                    ((FollowToHuman)followPath).setTarget(group.first());
                }
                followPath.update(this, sim);
                super.update(sim);
            }
            case "KnockOver" -> {
            }
            case "Trampled" -> {
                alive = false;
                sim.addToKillList(this);
            }
        }
    }

    public void resetPath() {
        this.speed = 1;
        followPath = new FollowPathToExit();
    }

    public boolean isKnockedOver() {
        return state == "KnockOver";
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
                    group.remove(this);
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
            case "Zombified" -> {
                zombificationCounter--;
                if (zombificationCounter == 0) {
                    System.out.println("Changed to zombie");
                    sim.addToZombifiedList(this);
                }
            }
            default -> state = "FollowPath";
        }
    }

    @Override
    public void handleCollision(Entity other, Sim sim) {
        super.handleCollision(other, sim);
        if(other instanceof Human && !(other instanceof Zombie)) {
            Human other_human = (Human)other;
            if (other_human.isKnockedOver())
                return;
            color = Color.BLUE;
            if(!this.group.contains(other_human)) {
                Group merged = group.merge(other_human.getGroup());
                this.group = merged;
                other_human.setGroup(merged);
            }
            if (this != group.first())
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
        //if (this == group.first()) {
        //    graphics2D.setColor(Color.GREEN);
        //    Position p = group.meanPosition();
        //    graphics2D.fillRect(p.intX(), p.intY(), size.getWidth(), size.getHeight());
        //}

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
    public void kill() {
        group.remove(this);
    }

    public String getState() {
        return state;
    }
}
