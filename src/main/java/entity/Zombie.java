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

public class Zombie extends Human {
    private Human target = null;
    private int ticks = 0;
    public Zombie(Sim sim, EntityController entityController) {
        super(sim, entityController);
        color = Color.GREEN;
    }

    @Override
    protected void initializeState() {
        this.speed = ZombieConf.defaultSpeed;
        this.motion = new Motion(this.speed);
        hp = 1000;
        followPath = new FollowPathToRandomPos();
    }

    public Zombie(Sim sim, EntityController entityController, Position p) {
        this(sim, entityController);
        setPosition(p);
    }

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

    @Override
    public void handleCollision(Entity other, Sim sim) {
        handleWallCollision(other, sim);

        if (other instanceof Human && !(other instanceof Zombie) && ticks == 30)
            ((Human)other).damage(group.getTotalDamage(ZombieConf.damage));


        if(other instanceof Zombie ) {
            Zombie other_zombie = (Zombie)other;
            if(!this.group.contains(other_zombie)) {
                Group merged = group.merge(other_zombie.getGroup());
                this.group = merged;
                other_zombie.setGroup(merged);
            }
            if (this != group.first()) {
                if (!(followPath instanceof FollowToHuman))
                    followPath = new FollowToHuman(group.first());
                else
                    ((FollowToHuman)followPath).setTarget(group.first());
                //System.out.println("asdf");
            }
        }
    }

    public boolean start_chasing(Entity en) {
        if (en instanceof Human && !(en instanceof Zombie) && getPosition().distanceTo(en.getPosition()) < ZombieConf.range) {
            Human target = (Human)en;
            if(target.isZombified())
                return false;
            followPath = new FollowToHuman(target);
            this.target = target;
            //System.out.println("Selected target");
            return true;
        }
        return false;
    }
    @Override
    public void draw(Graphics2D graphics2D) {
        super.draw(graphics2D);
        graphics2D.setColor(Color.PINK);
        if(ZombieConf.showPath) {
            for(Position p : followPath.getPath())
                graphics2D.fillRect(p.intX(), p.intY(), size.getWidth(), size.getHeight());
        }
        if(ZombieConf.showRange)
            graphics2D.drawArc(this.getCenterPosition().intX() - ZombieConf.range + size.getWidth()/2, this.getCenterPosition().intY() - ZombieConf.range + size.getHeight()/2, ZombieConf.range * 2, ZombieConf.range * 2, 0, 360);
    }
}
