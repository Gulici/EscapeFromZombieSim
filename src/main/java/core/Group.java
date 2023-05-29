package core;

import java.util.TreeSet;
import entity.Human;

@SuppressWarnings("serial")
public class Group extends TreeSet<Human> {
    public Group(Human agent) {
        super();
        super.add(agent);
    }

    public Group merge(Group g) {
        if (g == this)
            return this;
        this.addAll(g);
        return this;
    }

    public Position meanPosition() {
        double x = 0;
        double y = 0;
        for(Human agent: this) {
            Position pos = agent.getCenterPosition();
            x += pos.getX();
            y += pos.getY();
        }

        return new Position(x/this.size(), y/this.size());
    }
};
