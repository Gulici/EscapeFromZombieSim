package sim;

/*
    Class that's handle settings of simulation
 */
public class SimState {
    private int numberOfHumans = 10;

    public SimState() {

    }

    public int getNumberOfHumans() {
        return numberOfHumans;
    }

    public void setNumberOfHumans(int numberOfHumans) {
        this.numberOfHumans = numberOfHumans;
    }
}
