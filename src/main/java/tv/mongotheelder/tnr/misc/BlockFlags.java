package tv.mongotheelder.tnr.misc;

public class BlockFlags {
    public static final int UPDATE_BLOCK = 1;
    public static final int SEND_TO_CLIENT = 2;
    public static final int DO_NOT_RE_RENDER = 4;
    public static final int RUN_ON_MAIN_THREAD = 8;
    public static final int NO_NEIGHBOR_REACTIONS = 16;
    public static final int NO_NEIGHBOR_REACTIONS_TO_DROPS = 32;
    public static final int BEING_MOVED = 64;

    public static final int NO_ACTIONS = 0;
    public static final int NORMAL = UPDATE_BLOCK + SEND_TO_CLIENT;
    public static final int STATIC_UPDATE = NORMAL + NO_NEIGHBOR_REACTIONS;
}
