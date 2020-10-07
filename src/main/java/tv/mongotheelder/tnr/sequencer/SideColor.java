package tv.mongotheelder.tnr.sequencer;

import net.minecraft.util.IStringSerializable;

public enum SideColor implements IStringSerializable {
    OFF("off", 0),
    BLUE("blue", 1),
    GREEN("green", 2),
    YELLOW("yellow", 3),
    BLACK("black", 4),
    ORANGE("orange", 5);

    private final int index;
    private final String name;

    SideColor(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public static SideColor getByName(String name) {
        for(SideColor sc: SideColor.values()) {
            if (sc.getName().equals(name)) return sc;
        }
        return null;
    }

    public static SideColor getByIndex(int index) {
        for(SideColor sc: SideColor.values()) {
            if (sc.getIndex() == index) return sc;
        }
        return null;
    }

    @Override
    public String getString() {
        return getName();
    }
}
