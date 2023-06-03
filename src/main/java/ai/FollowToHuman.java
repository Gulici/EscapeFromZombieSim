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
    private int ticksPerPathUpdate = 0;
    public FollowToHuman(Human target) {
        super();
        human_target = target;

    }
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
        
        //System.out.println(agent.getPosition().getX() + " " + agent.getPosition().getY() + " " + target.getX() + " " + target.getY());

        handleMotion(agent);
    }

    public void setTarget(Human target) {
        human_target = target;
    }
}
