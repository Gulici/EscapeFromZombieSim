package sim;

import controller.AgentController;
import controller.ManualController;
import display.Display;
import entity.*;
import input.Input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;

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
    private List<Human> zombifiedHumans;

    private Map map;

    public Sim(SimState simState){
        entityList = Collections.synchronizedList(new ArrayList<>());
        agentsList = Collections.synchronizedList(new ArrayList<>());
        agentsToKill = Collections.synchronizedList(new ArrayList<>());
        deadAgents = Collections.synchronizedList(new ArrayList<>());
        agentsToEscape = Collections.synchronizedList(new ArrayList<>());
        escapeHumans = Collections.synchronizedList(new ArrayList<>());
        zombifiedHumans = Collections.synchronizedList(new ArrayList<>());
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
        for (int i = 0 ; i < simState.getNumberOfZombies() ; i++) {
            Zombie zombie = new Zombie(this, new AgentController());
            entityList.add(zombie);
            agentsList.add(zombie);
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

    public int findRegionKeys(int row, int col) {
        int rowRegion = (int) row / 25;
        int colRegion = (int) col / 25;
        int regionKey = rowRegion * 4 + colRegion;
        return regionKey;
    }

    public ArrayList<Integer> findRegionKeys(Entity entity) {
        ArrayList<Integer> regionKeys = new ArrayList<Integer>();
        int regionKey;
        int row = entity.getPosition().getRow();
        int col = entity.getPosition().getCol();
        regionKeys.add(findRegionKeys(row, col));

        regionKey = regionKeys.get(0);

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
        for (Human h: zombifiedHumans) {
            agentsList.remove(h);
            entityList.remove(h);
            removeFromRegion(h);
            h.kill();
            Zombie z = new Zombie(this, new AgentController(), h.getCenterPosition());
            agentsList.add(z);
            entityList.add(z);
            updateRegion(z);

        }
        zombifiedHumans.clear();
        for (Agent agent : agentsToKill) {
            agentsList.remove(agent);
            entityList.remove(agent);
            removeFromRegion(agent);
            agent.kill();
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
            agent.kill();
            removeFromRegion(agent);
            escapeHumans.add(agent);
        }
        agentsToEscape.clear();
    }

    public void addToKillList(Agent agent) {
        agentsToKill.add(agent);
    }

    public void addToEscapeList(Agent agent) {
        agentsToEscape.add(agent);
    }

    public void addToZombifiedList(Human h) {
        zombifiedHumans.add(h);
    }

    public List<Entity> getEntityList() {
        return entityList;
    }

    public void chaseInRangeEntities(Zombie zombie) {
        int range = zombie.getRange();
        int row = zombie.getPosition().getRow();
        int col = zombie.getPosition().getCol();
        List<Integer> regions = new ArrayList<Integer>();
        col = Math.max(col, 25);
        row = Math.max(row, 25);
        for(int i = col - range/2; i < col + range/2; i+=25)
            for(int j = row - range/2; j < row + range/2; j+=25) {
                regions.add(findRegionKeys(j, i));
            }
        //System.out.println(regions);

        for (Integer key : regions) {
            for (Entity other : entityInRegions.get(key)) {
                if(zombie.start_chasing(other))
                    return;
            }
        }
        System.out.println("resetting");
        zombie.resetPath();
//        for (Entity other : entityList){

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
