package ai;

import ai.FollowPath;
import entity.Agent;
import entity.Entity;
import entity.Human;
import sim.Sim;
import core.Position;

import java.util.List;

/**
 * Path object used by both zombies (to follow to humans in range), and humans(to follow to group leaders).
 */
public class FollowToHuman extends FollowPath{
    private Human human_target;
    private int ticksPerPathUpdate = 0;
    /**
     * Initializes class
     * @param target Human to follow.
     */
    public FollowToHuman(Human target) {
        super();
        human_target = target;

    }

    /**
     * Each 60 ticks updates path to new location of targeted human
     */
    @Override
    public void update(Agent agent, Sim sim) {

        if(target == null || ticksPerPathUpdate == 60 || !target.isInRangeOf(human_target.getCenterPosition())) {
            path.clear();
            //System.out.println(path.size());
            List<Position> path = sim.getMap().findPathAS(agent.getCenterPosition(), human_target.getCenterPosition());
            if(!path.isEmpty()){
                target = path.get(0);
                this.path.addAll(path);
            }
            ticksPerPathUpdate = 0;
            ((Human)agent).calculateSpeed();
        }
        ticksPerPathUpdate++;
        handleMotion(agent);
    }

    /**
     * Changes followd human
     * @param target New target.
     */
    public void setTarget(Human target) {
        human_target = target;
    }
}
