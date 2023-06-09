package entity;

import core.CollisionBox;
import core.Position;
import core.Size;
import sim.Sim;

import java.awt.*;
import java.util.List;


/**
 * Abstract class that is parent for all types of entities.
 * Each instances of Entity need to have fields: Position, Size;
 * and implementation of methods: update(), draw(), collidesWith();
 */
public abstract class Entity {
    protected Position position;
    protected Size size;
    public Entity() {
        position = new Position(0, 0);
        size = new Size(10, 10);
    }

    public abstract void update(Sim sim);
    public abstract void draw(Graphics2D graphics2D);
    public abstract boolean collidesWith(Entity entity);

    /**
     * @return collision box of entity.
     */
    public CollisionBox getCollisionBox() {
        {
            return new CollisionBox(
                    position.getX(),
                    position.getY(),
                    size.getWidth(),
                    size.getHeight()
            );
        }
    }

    /**
     * @return center position of entity.
     */
    public Position getCenterPosition() {
        int x = getPosition().intX() + getSize().getWidth()/2;
        int y = getPosition().intY() + getSize().getHeight()/2;
        return new Position(x,y);
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Size getSize() {
        return size;
    }

    public Position getPosition(){
        return position;
    }
}
