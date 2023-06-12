package sim;

import java.util.HashMap;

public class Stats {

    static int escapedPeople;
    static int deadPeople;
    public static HashMap<Long, Integer> escaped = new HashMap<Long,Integer>();
    public static HashMap<Long, Integer> dead = new HashMap<Long,Integer>();

    public Stats(){
        escapedPeople = 0;
        deadPeople = 0;
        escaped.put(0L,0);
        dead.put(0L,0);
    }

    public void addEscaped(long time){
        escapedPeople++;
        escaped.put(time, escapedPeople);
    }

    public void addDead(long time){
        deadPeople++;
        dead.put(time,deadPeople);
    }





}
