package tv.mongotheelder.tnr.sequencer;

import net.minecraft.util.IStringSerializable;

public enum SequencerMode implements IStringSerializable {
    ON("on", 0),
    OFF("off", 1),
    LOOP_ON("loop_on", 2),
    LOOP_OFF("loop_off", 3),
    NEVER("never", 4);

    private final int index;
    private final String name;

    SequencerMode(String name, int index) {
        this.name = name;
        this.index = index;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public static SequencerMode getByName(String name) {
        for(SequencerMode mode: SequencerMode.values()) {
            if (mode.getName().equals(name)) return mode;
        }
        return NEVER;
    }

    public static SequencerMode getByIndex(int index) {
        for(SequencerMode mode: SequencerMode.values()) {
            if (mode.getIndex() == index) return mode;
        }
        return NEVER;
    }
}
