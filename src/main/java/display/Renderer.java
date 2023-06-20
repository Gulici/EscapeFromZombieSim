package display;

import entity.Entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class responsible for drawing entities.
 */
public class Renderer {
    public void render(List<Entity> entityList, Graphics2D graphics2D){
        entityList.forEach(entity -> entity.draw(graphics2D));
    }
}
