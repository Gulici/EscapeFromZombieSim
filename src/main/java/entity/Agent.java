package entity;

import controller.EntityController;
import core.CollisionBox;
import core.Motion;
import core.Position;
import core.Size;
import sim.Sim;

import java.awt.*;
import java.util.List;

public abstract class Agent extends Entity{
    protected EntityController entityController;
    protected Motion motion;

    public Agent(EntityController entityController) {
        super();
        this.entityController = entityController;
        this.motion = new Motion(6);
        this.setSize(new Size(12,12));
    }
    @Override
    public void update(Sim sim) {
        handleMotion();
        handleCollisions(sim);
        apply(motion);
    }
    protected void handleMotion(){
        motion.update(entityController);
    }
    protected void handleCollision(Entity other){
        if(other instanceof Wall){
            motion.stop(willCollideX(other.getCollisionBox()),willCollideY(other.getCollisionBox()));
        }
    }
    protected void handleCollisions(Sim sim){
        List<Entity> collidingEntities = sim.getCollidingEntities(this);
        for(Entity other : collidingEntities){
            this.handleCollision(other);
        }
    }
    @Override
    public boolean collidesWith(Entity other) {
        return this.getCollisionBox().collidesWith(other.getCollisionBox());
    }
    public void apply(Motion motion) {
        position.apply(motion);
    }
    public EntityController getEntityController(){
        return entityController;
    }

    @Override
    public CollisionBox getCollisionBox() {
        Position predictPosition = Position.copyOf(position);
        predictPosition.apply(motion);

        return new CollisionBox(
                new Rectangle(
                        predictPosition.intX(),
                        predictPosition.intY(),
                        this.size.getWidth(),
                        this.size.getHeight()
                )
        );
    }

    public boolean willCollideX(CollisionBox otherBox){
        Position positionPredictX = Position.copyOf(position);
        positionPredictX.applyX(motion);

        CollisionBox predictCollisionBox = new CollisionBox(
                new Rectangle(positionPredictX.intX(),
                        positionPredictX.intY(),
                        size.getWidth(),
                        size.getHeight()
                )
        );

        return predictCollisionBox.collidesWith(otherBox);
    }
    public boolean willCollideY(CollisionBox otherBox){
        Position positionPredictY = Position.copyOf(position);
        positionPredictY.applyY(motion);

        CollisionBox predictCollisionBox = new CollisionBox(
                new Rectangle(positionPredictY.intX(),
                        positionPredictY.intY(),
                        size.getWidth(),
                        size.getHeight()
                )
        );

        return predictCollisionBox.collidesWith(otherBox);
    }
}
