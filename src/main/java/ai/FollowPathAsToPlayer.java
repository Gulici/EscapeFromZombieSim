package ai;

import core.Position;
import entity.Agent;
import entity.Entity;
import entity.ManualAgent;
import sim.Sim;

import java.util.List;

public class FollowPathAsToPlayer extends FollowPath{
    @Override
    public void update(Agent agent, Sim sim) {

        Entity elManuel = null;
        for (Entity entity : sim.getEntityList()) {
            if (entity instanceof ManualAgent) {
                elManuel = entity;
                break;
            }
        }

        if((target == null) || !target.isInRangeOf(elManuel.getCenterPosition())) {

            path.clear();
            List<Position> path = sim.getMap().findPathAS(agent.getCenterPosition(), elManuel.getCenterPosition());
            if(!path.isEmpty()){
                target = path.get(0);
                this.path.addAll(path);
            }
        }

        handleMotion(agent);
    }
}
