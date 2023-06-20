package entity;

import ai.FollowPath;
import ai.FollowPathToExit;
import ai.FollowToHuman;
import controller.EntityController;
import java.lang.Comparable;
import core.Motion;
import core.Size;
import configuration.HumanConf;
import sim.Sim;
import core.Group;
import core.Position;
import java.awt.*;

/**
 * Class responsible for Human agents
 */
public class Human extends Agent {

    // FollowPath object which agent should follow.
    protected FollowPath followPath;
    // Group to which agent will belong (at start it's single person group)
    Group group;
    // Color for drawing agents of this type.
    protected Color color;
    // Agents speed
    protected double speed;
    // Agents state can be either: 'FollowPath', 'KnockOver', 'Trampled'or 'Zombified'
    private String state;
    private int knockOverCounter;
    private int pushCounter;
    // Tick counter
    private int ticks = 0;
    // Counts how many humans changed to zombies.
    public static int changed = 0;
    protected Sim sim;
    protected int hp;
    // Number of ticks for poisoned human to change into zombie.
    private int zombificationCounter = 60;

    /**
     * Simple entity constructor placing entity at random position
     */
    public Human(Sim sim, EntityController entityController) {
        super(entityController);
        setSize(new Size(6,6));
        setPosition(sim.getMap().getRandomPosition());
        initializeState();
        state = "FollowPath";
        group = new Group(this);
        this.sim = sim;
    }

    /**
     * Initializes state of entity
     */
    protected void initializeState() {
        color = Color.CYAN;
        pushCounter = 0;
        knockOverCounter = 0;
        hp = HumanConf.hp;
        this.speed = HumanConf.defaultSpeed;
        this.motion = new Motion(this.speed);
        followPath = new FollowPathToExit();
    }

    /**
     * @return true if entity has been poisoned.
     */
    public boolean isZombified() {
        return state == "Zombified";
    }

    /**
     * Applies damage to the group to which the entity belongs.
     * @param total_damage maximum damage that with which attacking entity might attack (damage is chose randomly from 0 to this value)
     */
    public void damage(int total_damage) {
        this.group.damage(total_damage);
    }

    /**
     * Decreases hp by part of the damage that got applied to the group.
     * @param damage Damage applied to this Entity
     */
    public void decreaseHP(int damage) {
        hp -= damage;
        if (hp < 0) {
            sim.addToKillList(this);
            return;
        }
        if (damage > HumanConf.damageToChange) {
            state = "Zombified";
        }
    }

    /**
     * Calculates new speed for entity so that when group leader is to far it slows down
     */
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
        if (ticks == 30)
            ticks = 0;
        ticks++;
        switch (state) {
            case "FollowPath" -> {
                if (followPath instanceof FollowToHuman) {
                    // We want to make sure that the leader of group is current target.
                    ((FollowToHuman)followPath).setTarget(group.first());
                }
                followPath.update(this, sim);
                super.update(sim);
            }
            case "KnockOver" -> {
            }
            case "Trampled" -> {
                sim.addToKillList(this);
            }
            case "Zombified" -> {
                // When human is poisoned they still act like human till zombificationCounter reaches 0
                if (followPath instanceof FollowToHuman) {
                    ((FollowToHuman)followPath).setTarget(group.first());
                }
                followPath.update(this, sim);
                super.update(sim);
            }
        }
    }

    /**
     * Resets path of entity to their default behaviour.
     */
    public void resetPath() {
        this.speed = HumanConf.defaultSpeed;
        motion = new Motion(speed);
        followPath = new FollowPathToExit();
    }

    /**
     * @return Returns whether entity was knocked over.
     */
    public boolean isKnockedOver() {
        return state == "KnockOver";
    }

    /**
     * Handles possible changes in state of entity.
     */
    private void updateState() {
        switch (state) {
            case "FollowPath" -> {
                if (pushCounter >= 5) {
                    // If entity was pushed more than 4 times it changes state to "KnockOver"
                    state = "KnockOver";
                    knockOverCounter = 60;
                    color = Color.YELLOW;
                }
            }
            case "KnockOver" -> {
                if (pushCounter >= 20) {
                    // If entity was pushed more than 19 times it changes state to "Trampled"
                    state = "Trampled";
                    group.remove(this);
                    break;
                }
                if (knockOverCounter > 0) {
                    knockOverCounter --;
                }
                else {
                    // If knockOverCounter reaches 0 and entity wasn't Trampled it returns to following path
                    state = "FollowPath";
                    pushCounter = 0;
                    color = Color.cyan;
                }
            }
            case "Zombified" -> {
                // When entity is zombified it decreases zombificationCounter and after it reaches 0 it changes to zombie.
                zombificationCounter--;
                if (zombificationCounter == 0) {
                    changed++;
                    sim.addToZombifiedList(this);
                }
            }
            default -> state = "FollowPath";
        }
    }

    /**
     * Handling collisions for grouping, knocking over and attacking zombies.
     */
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
            if (this != group.first()) {
                if (!(followPath instanceof FollowToHuman))
                    followPath = new FollowToHuman(group.first());
                else
                    ((FollowToHuman)followPath).setTarget(group.first());
                //System.out.println("asdf");
            }
        }
        if(other instanceof Zombie && ticks == 30) {
            ((Zombie)other).damage(group.getTotalDamage(HumanConf.damage));
        }
    }

    /**
     * @param g Group to change.
     */
    public void setGroup(Group g) {
        group = g;
    }

    /**
     * @return Group to which entity belongs.
     */
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

    public void kill() {
        group.remove(this);
    }

    public String getState() {
        return state;
    }
}
