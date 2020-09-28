package tv.mongotheelder.tnr.sequencer;

public class StateMachine {
    private long delay;
    private long duration;
    private boolean running;
    private boolean state;
    private long runTime;
    private boolean lastState;

    public StateMachine() {
        running = false;
        state = false;
        runTime = 0;
        lastState = false;
    }

    public void setTimes(long delay, long duration) {
        setDelay(delay);
        setDuration(duration);
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        if (running || duration <= 0 || delay < 0) return;
        running = true;
        runTime = 0;
        state = false;
    }

    public void stop() {
        running = false;
        state = false;
        runTime = 0;
    }

    public boolean getState() {
        return state;
    }

    public void doTick() {
        lastState = state;
        if (!running) return;

        runTime++;
        if (!state && runTime > delay) {
            state = true;
        }
        if (state && runTime > delay+duration) {
            stop();
        }
    }

    public boolean stateChanged() { return state != lastState; }
}
