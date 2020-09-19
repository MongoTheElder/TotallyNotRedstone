package tv.mongotheelder.tnr.sequencer;

import net.minecraft.nbt.CompoundNBT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SequencerConfig {
    public static final int SEQUENCE_COUNT = 5;
    private SequencerMode mode = SequencerMode.NEVER;
    private int triggerDelay;
    private int triggerLevel;
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
                    String side = sides.getString(key);
                    if (sideMap.containsKey(side)) {
                        sideMap.put(key, SideColor.getByName(side));
                    } else {
                        sideMap.put(key, SideColor.OFF);
                    }
                }
            }
        }
        if (nbt.contains("trigger")) {
            CompoundNBT trigger = nbt.getCompound("trigger");
            if (trigger.contains("mode")) {
                mode = SequencerMode.getByIndex(trigger.getInt("mode"));
            }
            if (trigger.contains("delay")) {
                triggerDelay = trigger.getInt("delay");
            }
            if (trigger.contains("level")) {
                triggerLevel = trigger.getInt("level");
            }
        }
        if (nbt.contains("sequence")) {
            CompoundNBT seq = nbt.getCompound("sequence");
            if (seq.contains("delays")) {
                int[] delays = seq.getIntArray("delays");
                if (delays.length == SEQUENCE_COUNT) {
                    for (int i = 0; i < SEQUENCE_COUNT; i++) {
                        sequenceDefinition.get(i).setDelay(delays[i]);
                    }
                }
            }
            if (seq.contains("durations")) {
                int[] durations = seq.getIntArray("durations");
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
        trigger.putInt("delay", triggerDelay);
        trigger.putInt("level", triggerLevel);

        // Build the 'sequence' NBT tree
        // This tree is a pivot of the objects with the delays and durations split into separate lists
        CompoundNBT sequence = new CompoundNBT();
        CompoundNBT delays = new CompoundNBT();
        int[] delayVals = new int[SEQUENCE_COUNT];
        for(int i = 0; i < SEQUENCE_COUNT; i++) {
            delayVals[i] = sequenceDefinition.get(i).getDelay();
        }
        delays.putIntArray("delays",delayVals);
        CompoundNBT durations = new CompoundNBT();
        int[] durationVals = new int[SEQUENCE_COUNT];
        for(int i = 0; i < SEQUENCE_COUNT; i++) {
            durationVals[i] = sequenceDefinition.get(i).getDuration();
        }
        durations.putIntArray("durations", durationVals);

        sequence.put("delays", delays);
        sequence.put("durations", durations);

        // Assemble the compound NBT tree for the configuration
        CompoundNBT config = new CompoundNBT();
        config.put("sides", sides);
        config.put("trigger", trigger);
        config.put("sequence", sequence);
        return config;
    }


    public SequenceDefinition getSequence(int index) {
        if (index < 0 || index >= sequenceDefinition.size()) {
            LOGGER.error("Invalid sequence index");
            return null;
        }
        return sequenceDefinition.get(index);
    }

    public int getSideColorIndex(String side) {
        if (sideMap.containsKey(side)) {
            return sideMap.get(side).getIndex();
        }
        return SideColor.OFF.getIndex();
    }
}
