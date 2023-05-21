package sim;

public class SimLoop implements Runnable {
    Sim sim;
    SimState simState;
    Thread thread;
    private final int TPS = 60;

    public SimLoop(){
        simState = new SimState();
        sim = new Sim(simState);
        startThread();
    }
    public void startThread(){
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long currentTime, deltaTime = 0, deltaAccumulated = 0;
        double tickInterval = (double) 1000000000 / TPS;
        long timer = 0;
        int tpsCounter = 0;
        int fpsCounter = 0;

        while (thread.isAlive()){

            if (!simState.isRunning()) {
                if(simState.isSetSim()) {
                    sim.resetEntity();
                    sim.setSim(simState);
                    sim.render();
                    simState.setSetSim(false);
                }

                if (simState.isReset()) {

                    simState.setReset(false);
                }
            }

            if (simState.isRunning()) {
                if(simState.isWasStopped()) {
                    simState.setWasStopped(false);
                    lastTime = System.nanoTime();
                    deltaAccumulated = 0;
                    timer = 0;
                    tpsCounter = 0;
                    fpsCounter = 0;
                }

                currentTime = System.nanoTime();
                deltaTime = (currentTime - lastTime);
                deltaAccumulated += deltaTime;
                timer += deltaTime;
                lastTime = currentTime;

                while (deltaAccumulated >= tickInterval) {
                    deltaAccumulated -= tickInterval;
                    tpsCounter++;
                    sim.update();
                }
                sim.render();
                fpsCounter++;

                if (timer >= 1000000000) {
                    System.out.println("TPS:" + tpsCounter + " FPS:" + fpsCounter);
                    tpsCounter = 0;
                    fpsCounter = 0;
                    timer = 0;
                }
            }
        }
    }
}
