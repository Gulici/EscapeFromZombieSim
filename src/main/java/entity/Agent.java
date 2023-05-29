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
    protected void handleMotion(){
        motion.update(entityController);
    }
    protected void handleCollision(Entity other, Sim sim){
        if(other instanceof Wall ) {
            motion.stop(willCollideX(other.getCollisionBox()), willCollideY(other.getCollisionBox()));
        }

        if(other instanceof Human) {
            //push him
            ((Human) other).increasePushCounter();
            motion.stop(this.willCollideX(other.getCollisionBox()), this.willCollideY(other.getCollisionBox()));
//          /*
//          tutaj jest problem, z jakiegos powodu dziala na odwrot
//          być może
//           */

        }
        if (other instanceof DeadHuman) {
            motion.slow();
        }
    }
    protected void handleCollisions(Sim sim){
        List<Entity> collidingEntities = sim.getCollidingEntities(this);

        for(Entity other : collidingEntities){
            this.handleCollision(other, sim);
        }
    }
    @Override
    public boolean collidesWith(Entity other) {
        return this.getCollisionBox().collidesWith(other.getCollisionBox());
    }

    public void pushHuman (Human other) {
        other.increasePushCounter();
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
                predictPosition.getX(),
                predictPosition.getY(),
                getSize().getWidth(),
                getSize().getHeight()
        );
    }

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
}
