package ai;

import ai.FollowPath;
import entity.Agent;
import entity.Entity;
import entity.Human;
import sim.Sim;
import core.Position;

import java.util.List;

public class FollowToHuman extends FollowPath{
    private Human human_target;
    public FollowToHuman(Human target) {
        super();
        human_target = target;

        System.out.println(" " + target.getPosition().getRow());
    }
    @Override
    public void update(Agent agent, Sim sim) {

        if(!human_target.isAlive())
            return;
        if((target == null) || !target.isInRangeOf(human_target.getCenterPosition())) {

            path.clear();
            List<Position> path = sim.getMap().findPathAS(agent.getCenterPosition(), human_target.getCenterPosition());
            if(!path.isEmpty()){
                target = path.get(0);
                this.path.addAll(path);
            }
        }


        handleMotion(agent);
    }
}
