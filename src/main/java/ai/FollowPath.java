package ai;

import controller.AgentController;
import core.Position;
import entity.Agent;
import sim.Sim;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that handle motion through path of agent.
 */
public abstract class FollowPath {
    protected Position target;
    protected List<Position> path;

    public FollowPath() {
        path = new ArrayList<>();
    }
    public void update(Agent agent, Sim sim) {
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
    public int getLength() {
        return path.size();
    }

    public List<Position> getPath() {
        return path;
    }

}
