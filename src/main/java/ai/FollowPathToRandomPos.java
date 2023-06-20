package ai;

import ai.FollowPath;
import core.Node;
import core.Position;
import entity.Agent;
import sim.Sim;
import java.util.List;


/**
 * Path used for default behaviour of zombies.
 * Chooses new random position each time zombie reaches previous destination.
 */
public class FollowPathToRandomPos extends FollowPath {
    private Position random_target;
    protected boolean arrived(Agent entity, Position target) {
        return target != null && entity.getCenterPosition().isInRangeOf(target);
    }

    /**
     * Chooses new target position if zombie arrived.
     */
    public void update(Agent agent, Sim sim) {
        if (random_target == null || path.size() < 1) {
            random_target = sim.getMap().getRandomPosition();
            path.clear();
            List<Position> path = sim.getMap().findPathAS(agent.getCenterPosition(), random_target);
            if(!path.isEmpty()){
                target = path.get(0);
                this.path.addAll(path);
            }
        }
        handleMotion(agent);
    }
}
