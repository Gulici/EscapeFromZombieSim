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
import entity.Human;
import java.awt.*;

public class Zombie extends Human {
    private Human target = null;
    int ticks = 0;
    public Zombie(Sim sim, EntityController entityController) {
        super(sim, entityController);
        color = Color.GREEN;
    }

    @Override
    protected void initializeState() {
        this.speed = ZombieConf.defaultSpeed;
        this.motion = new Motion(this.speed);
        followPath = new FollowPathToRandomPos();
    }

    public Zombie(Sim sim, EntityController entityController, Position p) {
        this(sim, entityController);
        setPosition(p);
    }

    @Override
    public void resetPath() {
        if (!(followPath instanceof FollowPathToRandomPos))
            followPath = new FollowPathToRandomPos();
    }


    @Override
    public void update(Sim sim) {
        if (ticks == 30) {
            sim.chaseInRangeEntities(this);
            ticks = 0;
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
        if (other instanceof Human && !(other instanceof Zombie) && ticks == 30)
            ((Human)other).damage(ZombieConf.damage);
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
