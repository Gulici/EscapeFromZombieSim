package sim;

/**
 * Class that's handle settings of simulation
 */

public class SimState {
    public int numberOfHumans = 100;
    public int numberOfZombies = 20;
    private boolean running = false;
    private boolean wasStopped = true;
    private boolean setSim = false;
    private boolean reset = false;
    private boolean wasReset = false;

    public SimState() {}

    public int getNumberOfHumans() {
        return numberOfHumans;
    }

    public int getNumberOfZombies() {
        return numberOfZombies;
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
}
