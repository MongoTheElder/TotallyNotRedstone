package tv.mongotheelder.tnr.sequencer;

import net.minecraft.nbt.CompoundNBT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SequencerConfig {
    public static final int SEQUENCE_COUNT = 5;
    public static final int COLOR_COUNT = SideColor.values().length;
    private SequencerMode mode = SequencerMode.NEVER;
    private long triggerLevel;
    private final ArrayList<SequenceDefinition> sequenceDefinition = new ArrayList<>(SEQUENCE_COUNT);
    private final Map<String, SideColor> sideMap = new HashMap<>();

    private static final Logger LOGGER = LogManager.getLogger();

    public SequencerConfig() {
        sideMap.put("top", SideColor.OFF);
        sideMap.put("bottom", SideColor.OFF);
        sideMap.put("left", SideColor.OFF);
        sideMap.put("right", SideColor.OFF);
        sideMap.put("back", SideColor.OFF);
        for (int i = 0; i < SEQUENCE_COUNT; i++) {
            sequenceDefinition.add(new SequenceDefinition(0, 0));
        }
    }

    public void loadConfigFromNBT(CompoundNBT nbt) {
        if (nbt.contains("sides")) {
            CompoundNBT sides = nbt.getCompound("sides");
            for (String key : sideMap.keySet()) {
                if (sides.contains(key)) {
                    int index = sides.getInt(key);
                    sideMap.put(key, SideColor.getByIndex(index));
                }
            }
        }
        if (nbt.contains("trigger")) {
            CompoundNBT trigger = nbt.getCompound("trigger");
            if (trigger.contains("mode")) {
                mode = SequencerMode.getByIndex(trigger.getInt("mode"));
            }
            if (trigger.contains("level")) {
                triggerLevel = trigger.getLong("level");
            }
        }
        if (nbt.contains("sequence")) {
            CompoundNBT seq = nbt.getCompound("sequence");
            if (seq.contains("delays")) {
                long[] delays = seq.getLongArray("delays");
                if (delays.length == SEQUENCE_COUNT) {
                    for (int i = 0; i < SEQUENCE_COUNT; i++) {
                        sequenceDefinition.get(i).setDelay(delays[i]);
                    }
                }
            }
            if (seq.contains("durations")) {
                long[] durations = seq.getLongArray("durations");
                if (durations.length == SEQUENCE_COUNT) {
                    for (int i = 0; i < SEQUENCE_COUNT; i++) {
                        sequenceDefinition.get(i).setDuration(durations[i]);
                    }
                }
            }
        }
    }

    public CompoundNBT saveConfigToNBT() {
        // Build the 'sides' NBT tree
        CompoundNBT sides = new CompoundNBT();
        for (String key : sideMap.keySet()) {
            sides.putInt(key, sideMap.get(key).getIndex());
        }

        // Guild the 'trigger' NBT tree
        CompoundNBT trigger = new CompoundNBT();
        trigger.putInt("mode", mode.getIndex());
        trigger.putLong("level", triggerLevel);

        // Build the 'sequence' NBT tree
        // This tree is a pivot of the objects with the delays and durations split into separate lists
        CompoundNBT sequence = new CompoundNBT();

        long[] delayVals = new long[SEQUENCE_COUNT];
        for(int i = 0; i < SEQUENCE_COUNT; i++) {
            delayVals[i] = sequenceDefinition.get(i).getDelay();
        }
        sequence.putLongArray("delays",delayVals);

        long[] durationVals = new long[SEQUENCE_COUNT];
        for(int i = 0; i < SEQUENCE_COUNT; i++) {
            durationVals[i] = sequenceDefinition.get(i).getDuration();
        }
        sequence.putLongArray("durations", durationVals);

        // Assemble the compound NBT tree for the configuration
        CompoundNBT config = new CompoundNBT();
        config.put("sides", sides);
        config.put("trigger", trigger);
        config.put("sequence", sequence);
        return config;
    }

    public SequenceDefinition getSequence(int index) {
        if (index < 0 || index >= SEQUENCE_COUNT) {
            LOGGER.error("Invalid sequence index");
            return null;
        }
        return sequenceDefinition.get(index);
    }

    public void setSequenceDefinition(int index, SequenceDefinition s) {
        if (index < 0 || index >= SEQUENCE_COUNT) {
            LOGGER.error("Attempted to set a sequence outside bounds");
            return;
        }
        sequenceDefinition.set(index, s);
    }

    public int getSideColorIndex(String side) {
        if (sideMap.containsKey(side)) {
            return sideMap.get(side).getIndex();
        }
        return SideColor.OFF.getIndex();
    }

    public void setSideColorIndex(String side, int index) {
        if (sideMap.containsKey(side)) {
            sideMap.put(side, SideColor.getByIndex(index));
            }
        }

    public SequencerMode getMode() {
        return mode;
    }

    public void setMode(SequencerMode mode) {
        this.mode = mode;
    }

    public long getThreshold() {
        return triggerLevel;
    }

    public void setThreshold(long threshold) {
        triggerLevel = threshold;
    }
}
