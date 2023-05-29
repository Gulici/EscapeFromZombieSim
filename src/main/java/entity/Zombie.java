package entity;
import ai.FollowPath;
import ai.FollowPathToExit;
import ai.FollowToHuman;
import controller.EntityController;
import java.awt.Color;
import sim.Sim;
import core.Size;
import entity.Human;

public class Zombie extends Human {
    FollowPath followPath;
    private int range = 50;
    private Human target;
    public Zombie(Sim sim, EntityController entityController) {
        super(sim, entityController);
        followPath = new FollowPathToExit();
        color = Color.LIGHT_GRAY;
    }


    public int getRange() {
        return range;
    }

    @Override
    public void update(Sim sim) {
        if(ticksPathChange == 300) {
            sim.chaseInRangeEntities(this);
            ticksPathChange = 0;
            //followPath = new FollowToHuman(target);
        }
        ticksPathChange++;
        followPath.update(this, sim);
        handleMotion();
        sim.removeFromRegion(this);
        apply(motion);
    }

    public boolean start_chasing(Entity en) {
        if (en instanceof Human && getPosition().distanceTo(en.getPosition()) < range) {
            followPath = new FollowToHuman((Human)en);
            target = (Human)en;
            return true;
        }
        return false;
    }
}
