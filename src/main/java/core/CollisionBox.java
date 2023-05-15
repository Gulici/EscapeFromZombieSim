package core;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class CollisionBox {

    private Rectangle2D bounds;

    public CollisionBox(double x , double y, int width, int height){
        this.bounds = new Rectangle2D.Double(x,y,width,height);
    }
    public boolean collidesWith(CollisionBox other){
        return bounds.intersects(other.getBounds());
    }
    public Rectangle2D getBounds() {
        return bounds;
    }
}
