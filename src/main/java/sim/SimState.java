package sim;

/*
    Class that's handle settings of simulation
 */
public class SimState {
    private int numberOfHumans = 10;
    private boolean running = false;

    public SimState() {

    }

    public int getNumberOfHumans() {
        return numberOfHumans;
    }

    public void setNumberOfHumans(int numberOfHumans) {
        this.numberOfHumans = numberOfHumans;
    }

    public boolean isRunning() {
        return running;
    }
    public void setRunning(boolean running) {
        this.running = running;
    }
}
