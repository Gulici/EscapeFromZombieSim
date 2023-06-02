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
    private Human target = null;
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
        followPath.update(this, sim);
        handleMotion();
        handleCollisions(sim);
        sim.removeFromRegion(this);
        apply(motion);
        sim.updateRegion(this);
    }

    @Override
    public void handleCollision(Entity other, Sim sim) {
        super.testCollision(other, sim);
    }

    public boolean start_chasing(Entity en) {
        if (en instanceof Human && getPosition().distanceTo(en.getPosition()) < range) {
            followPath = new FollowToHuman((Human)en);
            target = (Human)en;
            //System.out.println("Selected target");
            return true;
        }
        return false;
    }
}
