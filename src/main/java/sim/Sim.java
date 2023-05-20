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
    private Map map;

    public Sim(SimState simState){
        entityList = new ArrayList<>();
        input = new Input();
        map  = new Map(this);
        entityList.addAll(map.getWalls());
        display = new Display(input, this, simState);
    }

    public void setSim(SimState simState) {
        entityList.add(new ManualAgent(new ManualController(input)));


        for (int i = 0 ; i < simState.getNumberOfHumans() ; i++) {
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
