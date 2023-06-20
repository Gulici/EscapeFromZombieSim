package ai;

import core.Node;
import core.Position;
import entity.Agent;
import sim.Sim;

import java.util.ArrayList;
import java.util.List;

/**
 * Child class that handle motion to exit, used by Humans.
 */
public class FollowPathToExit extends FollowPath {
    @Override
    public void update(Agent agent, Sim sim) {

        if(target == null) {

            //find node in with it located
            Node currentNode = sim.getMap().getNode(agent.getCenterPosition());

            //find shorter path
            if (currentNode.getPathToExitRight().size() < currentNode.getPathToExitLeft().size()) {
                path = currentNode.getPathToExitRight();
            }
            else
                path = currentNode.getPathToExitLeft();
            target = path.get(0);
        }
        handleMotion(agent);
    }
}
