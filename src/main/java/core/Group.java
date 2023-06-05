package core;

import java.util.HashSet;
import entity.Human;

@SuppressWarnings("serial")
public class Group extends HashSet<Human> {
    private Human leader = null;
    public Group(Human agent) {
        super();
        super.add(agent);
        leader = agent;
    }

    public Human first() {
        return leader;
    }

    public Group merge(Group g) {
        if (g == this)
            return this;
        leader = (leader.getPathLength() < g.first().getPathLength()) ? leader : g.first();
        for(Human h: g) {
            h.setGroup(this);
            this.add(h);
        }
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

    @Override
    public boolean remove(Object o) {
        Human h = (Human)o;
        if(o == first()) {
            if (size() > 1) {
                h.setGroup(new Group(h));
                h.resetPath();
                boolean ret = super.remove(h);
                int lowest = Integer.MAX_VALUE;
                leader = null;
                for (Human current : this) {
                    if (current.getPathLength() < lowest) {
                        leader = current;
                        lowest = current.getPathLength();
                    }
                }
                first().resetPath();
                return ret;
            }
            return false;
        }
        h.setGroup(new Group(h));
        h.resetPath();
        boolean ret = super.remove(h);
        if (ret) {
            first().resetPath();
        }
        return ret;
    }
};
