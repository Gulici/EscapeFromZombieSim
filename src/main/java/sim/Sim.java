package sim;

import ai.FollowPath;
import controller.AgentController;
import controller.ManualController;
import display.Display;
import entity.Entity;
import entity.ManualAgent;
import entity.NPC;
import input.Input;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sim {
    private Display display;
    private Input input;
    private List<Entity> entityList;
    private Map map;

    public Sim(){
        entityList = new ArrayList<>();
        input = new Input();
        map  = new Map(this);
        entityList.addAll(map.getWalls());
        display = new Display(input, this);
        //entityList.add(new ManualAgent(new ManualController(input)));
        entityList.add(new NPC(this, new AgentController()));
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
        return entityList.stream()
                .filter(entity::collidesWith)
                .collect(Collectors.toList());
    }
    public Map getMap() {
        return map;
    }
}
