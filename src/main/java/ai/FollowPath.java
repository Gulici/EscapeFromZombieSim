package ai;

import controller.AgentController;
import core.Position;
import entity.Agent;
import sim.Sim;

import java.util.ArrayList;
import java.util.List;

public class FollowPath {
    protected Position target;
    protected List<Position> path;

    public FollowPath() {
        path = new ArrayList<>();
    }
    public void update(Agent agent, Sim sim) {

        if(target == null) {

            List<Position> path = sim.getMap().findPath(agent.getPosition(), sim.getMap().getTargetPosition());
            if(!path.isEmpty()){
                target = path.get(0);
                this.path.addAll(path);
            }
        }
        handleMotion(agent);
    }
    protected boolean arrived(Agent entity) {
        return target != null && entity.getCenterPosition().isInRangeOf(target);
    }

    protected void handleMotion(Agent entity){
        AgentController controller = (AgentController) entity.getEntityController();
        if(arrived(entity)) {
            controller.stop();
        }
        if(!path.isEmpty() && entity.getCenterPosition().isInRangeOf(path.get(path.size()-1))){
            path.remove(path.size()-1);
        }
        if(!path.isEmpty()){
            controller.moveToTarget(path.get(path.size()-1), entity.getCenterPosition());
        }
    }

}
