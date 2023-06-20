package core;

import java.util.HashSet;
import entity.Human;
import java.util.Random;

@SuppressWarnings("serial")
/**
 * A Group is essentialy a Set which handles having a leader.
 * Additionally it handles damaging of members of goup.
 */
public class Group extends HashSet<Human> {
    private Human leader = null;
    /**
     * Initializes a group with single member.
     * @param agent Initial group member.
     */
    public Group(Human agent) {
        super();
        super.add(agent);
        leader = agent;
    }

    /**
     * @param unit_damage Damage a single unit can apply.
     * @return Group total damage based on single unit damage (group size * unit damage).
     */
    public int getTotalDamage(int unit_damage) {
        return size() * unit_damage;
    }

    /**
     * @return Group leader.
     */
    public Human first() {
        return leader;
    }

    /**
     * Returns a random integer.
     * @param min Minimum return value.
     * @param max Maximum return value.
     */
    public static int randInt(int min, int max) {
        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    /**
     * Damages every human in group by random damage in between 0 and avarage damage for this group.
     */
    public void damage(int damage) {
        int average_damage = damage / size();
        for (Human h: this) {
            h.decreaseHP(randInt(0, average_damage));
        }
    }

    /**
     * Merges two groups.
     * @param g Group to merge with.
     * @return Merged group.
     */
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

    /**
     * Calculates mean position of group.
     * @return mean position.
     */
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
    /**
     * Removes object o ensuring that if o was leader a new leader with shortest path is choosen.
     * @param o Object to be removed.
     * @return True if object was removed.
     */
    public boolean remove(Object o) {
        Human h = (Human)o;
        if(o == first()) {
            if (size() > 1) {
                h.setGroup(new Group(h));
                h.resetPath();
                boolean ret = super.remove(h);

                // Finding object with shortest path
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
