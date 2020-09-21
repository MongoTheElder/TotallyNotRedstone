package tv.mongotheelder.tnr.keypad;

public interface IKeypad {
    String getCode();

    void setCode(String code);

    void setUnlockState(boolean state);

    void errorSound();

    boolean showCode();

    void toggleLock();

    boolean getUnlockState();

}
