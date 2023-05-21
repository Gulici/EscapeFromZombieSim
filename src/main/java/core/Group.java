package core;

import java.util.Set;
import java.util.HashSet;
import entity.Human;
import core.Position;

@SuppressWarnings("serial")
public class Group extends HashSet<Human> {
    private Human leader = null;

    public Group(Human agent) {
        leader = agent;
    }

    @Override
    public boolean add(Human agent) {
        boolean ret = super.add(agent);
        if(ret && leader == null)
            leader = agent;
        return ret;
    };

    public Human getLeader() {
        return leader;
    }

    public Group merge(Group g) {
        if (g == this)
            return this;
        this.addAll(g);
        this.leader = (this.leader.getPathLength() < g.getLeader().getPathLength()) ?
          this.leader : g.getLeader();
        return this;
    }

    public Position meanPosition() {
        double x = 0;
        double y = 0;
        for(Human agent: this) {
            Position pos = agent.getCenterPosition();
            x += pos.intX();
            y += pos.intY();
        }

        return new Position(x/this.size(), y/this.size());
    }
};
