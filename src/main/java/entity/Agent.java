package entity;

import controller.EntityController;
import core.CollisionBox;
import core.Motion;
import core.Position;
import core.Size;
import sim.Sim;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for all entities that can move.
 */
public abstract class Agent extends Entity{
    protected EntityController entityController;
    protected Motion motion;

    public Agent(EntityController entityController) {
        super();
        this.entityController = entityController;
        this.motion = new Motion(8);
        this.setSize(new Size(12,12));
    }
    @Override
    public void update(Sim sim) {
        handleMotion();
        handleCollisions(sim);
        sim.removeFromRegion(this);
        apply(motion);
        sim.updateRegion(this);
    }

    /**
     * Method that handle motion.
     */
    protected void handleMotion(){
        motion.update(entityController);
    }

    /**
     * Method that handle agents behavior during collision with wall entity.
     * @param other
     * @param sim
     */
    protected void handleWallCollision(Entity other, Sim sim) {
        if(other instanceof Wall ) {
            motion.stop(willCollideX(other.getCollisionBox()), willCollideY(other.getCollisionBox()));
        }
    }

    /**
     * Method that handle agents behavior during collision with other entities.
     * While agent colliding with Human increase their push counter and stop the motion.
     * If agent colliding with DeadHuman slow the motion.
     * @param other
     * @param sim
     */
    protected void handleCollision(Entity other, Sim sim){
        handleWallCollision(other, sim);

        if(other instanceof Human) {
            //push him
            ((Human) other).increasePushCounter();
            motion.stop(this.willCollideX(other.getCollisionBox()), this.willCollideY(other.getCollisionBox()));
        }
        if (other instanceof DeadHuman) {
            motion.slow();
        }
    }

    /**
     * Method that handle all collisions.
     * @param sim
     */
    protected void handleCollisions(Sim sim){
        List<Entity> collidingEntities = sim.getCollidingEntities(this);

        for(Entity other : collidingEntities){
            this.handleCollision(other, sim);
        }
    }

    /**
     * @param other
     * @return true if entity's collision box intersects with collision box of other
     */
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

    /**
     * @return predicted collision box after demanding move.
     */
    @Override
    public CollisionBox getCollisionBox() {
        Position predictPosition = Position.copyOf(position);
        predictPosition.apply(motion);

        return new CollisionBox(
                predictPosition.getX(),
                predictPosition.getY(),
                getSize().getWidth(),
                getSize().getHeight()
        );
    }

    /**
     * @param otherBox
     * @return true if agent's collision box will collide after move in X axis
     */
    public boolean willCollideX(CollisionBox otherBox){
        Position positionPredictX = Position.copyOf(position);
        positionPredictX.applyX(motion);

        CollisionBox predictCollisionBox = new CollisionBox (
                positionPredictX.getX(),
                positionPredictX.getY(),
                size.getWidth(),
                size.getHeight()
        );
        return predictCollisionBox.collidesWith(otherBox);
    }

    /**
     * @param otherBox
     * @return true if agent's collision box will collide after move in Y axis
     */
    public boolean willCollideY(CollisionBox otherBox){
        Position positionPredictY = Position.copyOf(position);
        positionPredictY.applyY(motion);

        CollisionBox predictCollisionBox = new CollisionBox (
                positionPredictY.getX(),
                positionPredictY.getY(),
                size.getWidth(),
                size.getHeight()
        );

        return predictCollisionBox.collidesWith(otherBox);
    }

    abstract public void kill();
}
