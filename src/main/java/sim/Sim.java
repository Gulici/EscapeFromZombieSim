package sim;

import ai.FollowPath;
import controller.AgentController;
import controller.ManualController;
import core.Position;
import display.Display;
import entity.*;
import input.Input;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sim {
    private Display display;
    private Input input;
    private List<Entity> entityList;
    private Map map;

    public Sim(int numberOfAgents){
        entityList = new ArrayList<>();
        input = new Input();
        map  = new Map(this);
        entityList.addAll(map.getWalls());
        display = new Display(input, this);

        entityList.add(new ManualAgent(new ManualController(input)));

//        for (int i = 0 ; i < 5 ; i++) {
//            entityList.add(new AStarEnjoyer(this, new AgentController()));
//        }
//        Agent agent1 = new AStarEnjoyer(this, new AgentController());
//        agent1.setPosition(new Position(getMap().getTileSize() * 38, getMap().getTileSize() * 38));
//        entityList.add(agent1);
//
//        Agent agent2 = new AStarEnjoyer( this, new AgentController());
//        agent2.setPosition(new Position(getMap().getTileSize() * 38, getMap().getTileSize() * 5));
//        entityList.add(agent2);
//
//        Agent agent3 = new AStarEnjoyer( this, new AgentController());
//        agent3.setPosition(new Position(getMap().getTileSize() * 38, getMap().getTileSize() * 10));
//        entityList.add(agent3);
//
//        Agent agent4 = new AStarEnjoyer( this, new AgentController());
//        agent4.setPosition(new Position(getMap().getTileSize() * 38, getMap().getTileSize() * 15));
//        entityList.add(agent4);
//
//        Agent agent5 = new AStarEnjoyer( this, new AgentController());
//        agent5.setPosition(new Position(getMap().getTileSize() * 38, getMap().getTileSize() * 25));
//        entityList.add(agent5);

        for (int i = 0; i < numberOfAgents ; i++) {
            entityList.add(new Human(this, new AgentController()));
        }
    }

    public void update(){
        entityList.forEach(entity -> entity.update(this));
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
