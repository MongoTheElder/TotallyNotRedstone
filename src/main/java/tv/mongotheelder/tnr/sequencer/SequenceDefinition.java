package tv.mongotheelder.tnr.sequencer;

public class SequenceDefinition {
    private int delay;
    private int duration;

    public SequenceDefinition(int delay, int duration) {
        this.delay = delay;
        this.duration = duration;
    }

    public int getDelay() {
        return delay;
    }

    public int getDuration() {
        return duration;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
