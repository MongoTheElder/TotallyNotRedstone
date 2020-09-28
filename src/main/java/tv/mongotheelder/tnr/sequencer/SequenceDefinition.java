package tv.mongotheelder.tnr.sequencer;

public class SequenceDefinition {
    private long delay;
    private long duration;

    public SequenceDefinition(long delay, long duration) {
        this.delay = delay;
        this.duration = duration;
    }

    public long getDelay() {
        return delay;
    }

    public long getDuration() {
        return duration;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}
