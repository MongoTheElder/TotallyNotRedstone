package tv.mongotheelder.tnr.buttons;

import net.minecraft.util.IStringSerializable;

public enum TimedButtonStates implements IStringSerializable {
    OFF("off", 0),
    RUNNING("running", 1),
    ON("on", 2);

    private final int index;
    private final String name;

    TimedButtonStates(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() { return name; }

    public int getIndex() { return index; }

    public TimedButtonStates getByName(String name) {
        for(TimedButtonStates tbs: TimedButtonStates.values()) {
            if (tbs.getName().equals(name)) return tbs;
        }
        return OFF;
    }

    public TimedButtonStates advanceState() {
        switch (this) {
            default:
            case OFF:
                return RUNNING;
            case RUNNING:
                return ON;
            case ON:
                return OFF;
        }
    }
}

