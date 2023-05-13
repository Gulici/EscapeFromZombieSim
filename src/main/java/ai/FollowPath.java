package ai;

import controller.AgentController;
import core.Position;
import entity.NPC;
import sim.Sim;

import java.util.ArrayList;
import java.util.List;

public class FollowPath {
    private Position target;
    private List<Position> path;

    public FollowPath() {
        path = new ArrayList<>();
    }
    public void update(NPC entity, Sim sim) {

        if(target == null) {
            List<Position> path = sim.getMap().findPath(entity.getPosition(), sim.getMap().getTargetPosition());
            if(!path.isEmpty()){
                target = path.get(0);
                this.path.addAll(path);
            }
        }
        AgentController controller = (AgentController) entity.getEntityController();
        if(arrived(entity)) {
            controller.stop();
        }
        if(!path.isEmpty() && entity.getPosition().isInRangeOf(path.get(path.size()-1))){
            path.remove(path.size()-1);
        }
        if(!path.isEmpty()){
            controller.moveToTarget(path.get(path.size()-1), entity.getPosition());
            //System.out.println("moving");
        }
    }
    private boolean arrived(NPC entity) {
        return target != null && entity.getPosition().isInRangeOf(target);
    }

}
