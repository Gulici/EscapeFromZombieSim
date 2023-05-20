package sim;

/*
    Class that's handle settings of simulation
 */
public class SimState {
    private int numberOfHumans = 10;
    private boolean running = false;
    private boolean setSim = false;

    private boolean wasStopped = true;

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
