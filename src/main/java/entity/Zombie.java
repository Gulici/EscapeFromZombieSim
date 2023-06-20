package entity;
import ai.FollowPath;
import ai.FollowPathToRandomPos;
import ai.FollowToHuman;
import controller.EntityController;
import java.awt.Color;
import sim.Sim;
import configuration.ZombieConf;
import core.Size;
import core.Position;
import core.Motion;
import core.Group;
import entity.Human;
import java.awt.*;

/**
 * This class is responsible for zombie agents.
 */
public class Zombie extends Human {
    private Human target = null;
    private int ticks = 0;
    /**
     * Creates new zombie object.
     */
    public Zombie(Sim sim, EntityController entityController) {
        super(sim, entityController);
        color = Color.GREEN;
    }

    @Override
    protected void initializeState() {
        this.speed = ZombieConf.defaultSpeed;
        this.motion = new Motion(this.speed);
        hp = ZombieConf.hp;
        followPath = new FollowPathToRandomPos();
    }

    /**
     * Creates new zombie objects and places it on position p (used for replacing zombified humans)
     * @param p position at which a zombie should spawn
     */
    public Zombie(Sim sim, EntityController entityController, Position p) {
        this(sim, entityController);
        setPosition(p);
    }

    /**
     * Decreases hp till zombie dies
     */
    @Override
    public void decreaseHP(int damage) {
        hp -= damage;
        if (hp < 0) {
            sim.addToKillList(this);
            return;
        }
    }

    @Override
    public void resetPath() {
        this.speed = ZombieConf.defaultSpeed;
        motion = new Motion(speed);
        if (!(followPath instanceof FollowPathToRandomPos))
            followPath = new FollowPathToRandomPos();
    }


    @Override
    public void update(Sim sim) {
        if (ticks == 30){
            ticks = 0;
            if(this.group.first() == this) 
                sim.chaseInRangeEntities(this);
        }
        else if (followPath instanceof FollowToHuman && this.group.first() != this) {
            ((FollowToHuman)followPath).setTarget(group.first());
        }
        ticks++;
        followPath.update(this, sim);
        handleMotion();
        handleCollisions(sim);
        sim.removeFromRegion(this);
        apply(motion);
        sim.updateRegion(this);
    }

    /**
     * Handles collisions, supports attacking humans on contact and joining into groups.
     */
    @Override
    public void handleCollision(Entity other, Sim sim) {
        handleWallCollision(other, sim);

        // Attacking collided humans once per 30 ticks.
        if (other instanceof Human && !(other instanceof Zombie) && ticks == 30)
            ((Human)other).damage(group.getTotalDamage(ZombieConf.damage));


        // Group joining mechanics
        if(other instanceof Zombie ) {
            Zombie other_zombie = (Zombie)other;

            if(!this.group.contains(other_zombie)) {
                // We only join if they aren't in group already
                Group merged = group.merge(other_zombie.getGroup());
                this.group = merged;
                other_zombie.setGroup(merged);
            }
            if (this != group.first()) {
                // This code is responsible for updating path of zombie that got joined and was second in update order
                if (!(followPath instanceof FollowToHuman))
                    followPath = new FollowToHuman(group.first());
                else
                    ((FollowToHuman)followPath).setTarget(group.first());
            }
        }
    }

    /**
     * Method responsible for trying to start chasing an entity. Sim should give us entities that are in range or close to our range.
     * @param en Entity to start chasing if it's in range.
     * @return Returns true if Entity was in range and it started chasing it else false.
     */
    public boolean start_chasing(Entity en) {
        if (en instanceof Human && !(en instanceof Zombie) && getPosition().distanceTo(en.getPosition()) < ZombieConf.range) {
            Human target = (Human)en;
            if(target.isZombified())
                // We don't want to chase alredy zombified entities.
                return false;
            followPath = new FollowToHuman(target);
            this.target = target;
            return true;
        }
        return false;
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        super.draw(graphics2D);
        graphics2D.setColor(Color.PINK);
        if(ZombieConf.showPath) {
            // Drawing zombies path if it's selected in configuration.
            for(Position p : followPath.getPath())
                graphics2D.fillRect(p.intX(), p.intY(), size.getWidth(), size.getHeight());
        }
        if(ZombieConf.showRange)
            // Drawing zombie range if it's selected in configuration.
            graphics2D.drawArc(this.getCenterPosition().intX() - ZombieConf.range + size.getWidth()/2, this.getCenterPosition().intY() - ZombieConf.range + size.getHeight()/2, ZombieConf.range * 2, ZombieConf.range * 2, 0, 360);
    }
}
