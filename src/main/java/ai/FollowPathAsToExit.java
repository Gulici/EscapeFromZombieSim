package ai;

import core.Position;
import entity.Agent;
import sim.Sim;

import java.util.List;

public class FollowPathAsToExit extends FollowPath {

    @Override
    public void update(Agent agent, Sim sim) {

        if(target == null) {

            List<Position> path = sim.getMap().findPathAS(agent.getCenterPosition(), sim.getMap().getTargetPosition());
            if(!path.isEmpty()){
                target = path.get(0);
                this.path.addAll(path);
            }
        }
        handleMotion(agent);
    }
}
