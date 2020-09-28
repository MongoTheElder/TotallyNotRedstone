package tv.mongotheelder.tnr.sequencer;

import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.util.math.MathHelper;

public class IntegerRangeSlider extends AbstractSlider {
    private long currentValue;
    private final long min;
    private final long max;

    protected IntegerRangeSlider(int xIn, int yIn, int widthIn, int heightIn, long min, long max, long valueIn) {
        super(xIn, yIn, widthIn, heightIn, MathHelper.clamp((valueIn-min)/(double)(max-min), 0.0D, 1.0D));
        this.max = max;
        this.min = min;
        currentValue = MathHelper.clamp(valueIn, min, max);
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        setMessage(Long.toString(currentValue));
    }

    @Override
    protected void applyValue() {
        currentValue = Math.round(this.value*(max-min)+min);
    }

    public long getCurrentValue() {
        return currentValue;
    }
}
