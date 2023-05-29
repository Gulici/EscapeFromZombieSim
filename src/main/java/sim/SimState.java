package sim;

/*
    Class that's handle settings of simulation
 */
public class SimState {
    private int numberOfHumans = 50;
    private int numberOfZombies = 50;
    private boolean running = false;
    private boolean wasStopped = true;
    private boolean setSim = false;
    private boolean reset = false;
    private boolean wasReset = false;

    public SimState() {

    }

    public int getNumberOfHumans() {
        return numberOfHumans;
    }

    public int getNumberOfZombies() {
        return numberOfZombies;
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

    public boolean isSetSim() {
        return setSim;
    }
    public void setSetSim(boolean setSim) {
        this.setSim = setSim;
    }

    public boolean isWasStopped() {
        return wasStopped;
    }

    public void setWasStopped(boolean wasStopped) {
        this.wasStopped = wasStopped;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public boolean isWasReset() {
        return wasReset;
    }

    public void setWasReset(boolean wasReset) {
        this.wasReset = wasReset;
    }
}
