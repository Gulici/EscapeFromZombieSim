package display;

import entity.Entity;

import java.awt.*;
import java.util.ArrayList;

public class Renderer {
    public void render(ArrayList<Entity> entityList, Graphics2D graphics2D){
        entityList.forEach(entity -> entity.draw(graphics2D));
    }
}
