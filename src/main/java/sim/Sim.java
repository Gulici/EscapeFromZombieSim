package sim;

import controller.AgentController;
import controller.ManualController;
import display.Display;
import entity.*;
import input.Input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Sim {
    private Display display;
    private Input input;
    private List<Entity> entityList;
    private HashMap<Integer,ArrayList<Entity>> entityInRegions;
    private List<Agent> agentsList;
    private List<Agent> agentsToKill;
    private List<Agent> agentsToEscape;
    private List<Agent> deadAgents;
    private List<Agent> escapeHumans;

    private Map map;

    public Sim(SimState simState){
        entityList = new ArrayList<>();
        agentsList = new ArrayList<>();
        agentsToKill = new ArrayList<>();
        deadAgents = new ArrayList<>();
        agentsToEscape = new ArrayList<>();
        escapeHumans = new ArrayList<>();
        entityInRegions = new HashMap<>();
        input = new Input();
        map  = new Map(this);

        entityList.addAll(map.getWalls());
        entityList.addAll(map.getExits());
        display = new Display(input, this, simState);
    }

    public void update(){
        entityList.forEach(entity -> entity.update(this));
        //agentsList.forEach(agent -> agent.update(this));
        removeAgent();
    }

    public void render(){
        display.render(this);
    }

    public void setSim(SimState simState) {
        for (int i = 0 ; i < simState.getNumberOfHumans() ; i++) {
            Human human = new Human(this, new AgentController());
            entityList.add(human);
            agentsList.add(human);
        }
        assignToRegions();
    }

    public void assignToRegions() {
        //initialize regions
        for (int key = 0 ; key < 16 ; key++) {
            entityInRegions.put(key, new ArrayList<>());
        }

        for (Entity entity : entityList) {
            //find regions
            ArrayList<Integer> regionKeys = findRegionKeys(entity);

            //assign to regions
            for (Integer key : regionKeys) {
                entityInRegions.get(key).add(entity);
            }
        }
    }
    public void updateRegion(Agent agent) {
        ArrayList<Integer> regionKeys = findRegionKeys(agent);
        for (Integer key : regionKeys) {
            entityInRegions.get(key).add(agent);
        }
    }

    public void removeFromRegion(Agent agent) {
        ArrayList<Integer> regionKey = findRegionKeys(agent);
        for (Integer key : regionKey) {
            entityInRegions.get(key).remove(agent);
        }
    }

    public ArrayList<Integer> findRegionKeys(Entity entity) {
        int regionKey;
        ArrayList<Integer> regionKeys = new ArrayList<>();
        int row = entity.getPosition().getRow();
        int col = entity.getPosition().getCol();

        int rowRegion = (int) row / 25;
        int colRegion = (int) col / 25;

        regionKey = rowRegion * 4 + colRegion;
        regionKeys.add(regionKey);

        boolean right = false, below = false;
        if (row % 25 == 0 && row != 0) {
           regionKeys.add(regionKey - 4);
            below = true;
        }
        if (col % 25 == 0 && col != 0) {
            regionKeys.add(regionKey - 1);
            right = true;
        }
        if (below && right) {
            regionKeys.add(regionKey - 5);
        }
        return regionKeys;
    }

    public void resetEntity() {
       ArrayList<Entity> toRemove = new ArrayList<>();
       for (Entity entity : entityList) {
           if (!(entity instanceof Wall) && !(entity instanceof Exit)) {
               toRemove.add(entity);
           }
       }
       entityList.removeAll(toRemove);
       deadAgents.clear();
       agentsList.clear();
       entityInRegions.clear();
       map.resetNodes();
    }
    public void removeAgent() {
        for (Agent agent : agentsToKill) {
            agentsList.remove(agent);
            entityList.remove(agent);
            removeFromRegion(agent);
            DeadHuman deadHuman = new DeadHuman(new AgentController());
            deadHuman.setPosition(agent.getPosition());
            deadHuman.setSize(agent.getSize());
            deadAgents.add(deadHuman);
            updateRegion(deadHuman);
        }
        agentsToKill.clear();
        for (Agent agent : agentsToEscape) {
            agentsList.remove(agent);
            entityList.remove(agent);
            removeFromRegion(agent);
            escapeHumans.add(agent);
        }
    }

    public void addToKillList(Agent agent) {
        agentsToKill.add(agent);
    }

    public void addToEscapeList(Agent agent) {
        agentsToEscape.add(agent);
    }

    public ArrayList<Entity> getEntityList() {
        return (ArrayList<Entity>) entityList;
    }
    public List<Entity> getCollidingEntities(Entity entity){
        List<Entity> collidingList = new ArrayList<>();

        ArrayList<Integer> entityRegion = findRegionKeys(entity);

        for (Integer key : entityRegion) {
            for (Entity other : entityInRegions.get(key)) {
                if (other.collidesWith(entity) && !other.equals(entity)) collidingList.add(other);
            }
        }
//        for (Entity other : entityList){
//            if (other.collidesWith(entity) && !other.equals(entity)) collidingList.add(other);
//        }
//        for (Entity other : deadAgents){
//            if (other.collidesWith(entity) && !other.equals(entity)) collidingList.add(other);
//        }


        return collidingList;
    }

    public Map getMap() {
        return map;
    }

    public List<Agent> getDeadAgents() {
        return deadAgents;
    }

    public void setDeadAgents(List<Agent> deadAgents) {
        this.deadAgents = deadAgents;
    }
}
