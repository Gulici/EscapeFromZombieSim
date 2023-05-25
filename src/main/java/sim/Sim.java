package sim;

import controller.AgentController;
import controller.ManualController;
import display.Display;
import entity.*;
import input.Input;

import java.util.ArrayList;
import java.util.List;

public class Sim {
    private Display display;
    private Input input;
    private List<Entity> entityList;
    private List<Agent> agentsList;
    private Map map;

    public Sim(SimState simState){
        entityList = new ArrayList<>();
        agentsList = new ArrayList<>();
        input = new Input();
        map  = new Map(this);
        entityList.addAll(map.getWalls());
        entityList.addAll(map.getExits());
        display = new Display(input, this, simState);
    }

    public void setSim(SimState simState) {
        for (int i = 0 ; i < simState.getNumberOfHumans() ; i++) {
            Human human = new Human(this, new AgentController());
            entityList.add(human);
            agentsList.add(human);
        }
    }

    public void resetEntity() {
       ArrayList<Entity> toRemove = new ArrayList<>();
       for (Entity entity : entityList) {
           if (!(entity instanceof Wall) && !(entity instanceof Exit)) {
               toRemove.add(entity);
           }
       }
       entityList.removeAll(toRemove);
       map.resetNodes();
    }

    public void update(){
        //entityList.forEach(entity -> entity.update(this));
        agentsList.forEach(agent -> agent.update(this));
    }
    public void render(){
        display.render(this);
    }

    public ArrayList<Entity> getEntityList() {
        return (ArrayList<Entity>) entityList;
    }
    public List<Entity> getCollidingEntities(Entity entity){
        List<Entity> collidingList = new ArrayList<>();

        for (Entity other : entityList){
            if (other.collidesWith(entity) && !other.equals(entity)) collidingList.add(other);
        }

        return collidingList;
    }
    public Map getMap() {
        return map;
    }
}
