package entity;

import ai.FollowPath;
import ai.FollowPathToExit;
import controller.EntityController;
import core.Motion;
import core.Size;
import sim.Sim;

import java.awt.*;

public class Human extends Agent{

    private Color color;
    private int pushCounter;
    private int knockOverCounter;
    private String state;

    FollowPath followPath;
    public Human(Sim sim, EntityController entityController) {
        super(entityController);
        setSize(new Size(6,6));
        setPosition(sim.getMap().getRandomPosition());
        this.motion = new Motion(0.5);
        color = Color.CYAN;
        pushCounter = 0;
        knockOverCounter = 0;
        state = "FollowPath";

        followPath = new FollowPathToExit();
    }

    @Override
    public void update(Sim sim) {
        updateState();
        switch (state) {
            case "FollowPath" -> {
                followPath.update(this, sim); handleMotion();
                handleCollisions(sim);
                apply(motion);
            }
            case "KnockOver" -> {}
            case "Trampled" -> sim.addToKillList(this);
        }
    }

    private void updateState() {
        switch (state) {
            case "FollowPath" -> {
                if (pushCounter >= 5) {
                    state = "KnockOver";
                    knockOverCounter = 60;
                    color = Color.YELLOW;
                }
            }
            case "KnockOver" -> {
                if (pushCounter >= 20) {
                    state = "Trampled";
                    break;
                }
                if (knockOverCounter > 0) {
                    knockOverCounter --;
                }
                else {
                    state = "FollowPath";
                    pushCounter = 0;
                    color = Color.cyan;
                }
            }
            default -> state = "FollowPath";
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(color);
        graphics2D.fillRect(getPosition().intX(), getPosition().intY(), size.getWidth(), size.getHeight());
    }

    public void increasePushCounter() {
        pushCounter++;
    }
}
