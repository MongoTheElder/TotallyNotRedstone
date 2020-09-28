package tv.mongotheelder.tnr.sequencer;

import java.util.ArrayList;
import java.util.List;

public class StateMachines {
    private final List<StateMachine> machines;
    private boolean repeating;
    private SequencerMode mode;
    private long threshold;
    private long lastLevel;

    public StateMachines() {
        machines = new ArrayList<>(SequencerConfig.SEQUENCE_COUNT);
        for (int i = 0; i < SequencerConfig.SEQUENCE_COUNT; i++) {
            machines.add(new StateMachine());
        }
        clearStates();
        this.mode = SequencerMode.OFF;
        lastLevel = 0;
    }

    public void setThreshold(long threshold) {
        this.threshold = threshold;
    }

    public boolean[] getRedstoneStates() {
        boolean[] states = new boolean[SequencerConfig.SEQUENCE_COUNT];
        for (int i = 0; i < SequencerConfig.SEQUENCE_COUNT; i++) {
            states[i] = machines.get(i).getState();
        }
        return states;
    }

    public void setMode(SequencerMode mode) {
        this.mode = mode;
        repeating = (mode == SequencerMode.LOOP_OFF || mode == SequencerMode.LOOP_ON);
    }

    public void clearStates() {
        for (StateMachine m: machines) m.stop();
        lastLevel = 0;
    }

    public void setMachineConfig(SequencerConfig config) {
        setMode(config.getMode());
        setThreshold(config.getThreshold());
        for (int i = 0; i < SequencerConfig.SEQUENCE_COUNT; i++) {
            SequenceDefinition seq = config.getSequence(i);
            machines.get(i).setTimes(seq.getDelay(), seq.getDuration());
        }
    }

    public boolean isRunning() {
        boolean running = false;
        for (StateMachine m: machines) running |= m.isRunning();
        return running;
    }

    public void start() {
        if (mode == SequencerMode.NEVER || isRunning()) return;
        for (StateMachine m: machines) m.start();
    }

    public boolean isTriggered(int redstoneLevel) {
        boolean trig = false;
        switch (mode) {
            default:
            case NEVER:
                break;
            case ON:
                trig = (redstoneLevel > threshold && lastLevel != redstoneLevel);
                break;
            case LOOP_ON:
                trig = (redstoneLevel > threshold);
                break;
            case OFF:
                trig = (redstoneLevel <= threshold && lastLevel != redstoneLevel);
                break;
            case LOOP_OFF:
                trig = (redstoneLevel <= threshold);
                break;
        }
        lastLevel = redstoneLevel;
        return trig;
    }

    public void doTick(int redstoneLevel) {
        for (StateMachine m: machines) m.doTick();
        if (!isRunning()) {
            if (isTriggered(redstoneLevel)) {
                start();
            }
        }
    }

    public boolean stateChanged() {
        boolean changed = false;
        for (StateMachine m: machines) { changed |= m.stateChanged(); }
        return changed;
    }
}
