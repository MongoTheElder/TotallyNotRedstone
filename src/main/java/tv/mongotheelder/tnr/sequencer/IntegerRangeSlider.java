package tv.mongotheelder.tnr.sequencer;

import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;

public class IntegerRangeSlider extends AbstractSlider {
    private long currentValue;
    private final long min;
    private final long max;

    protected IntegerRangeSlider(int xIn, int yIn, int widthIn, int heightIn, long min, long max, long valueIn) {
        super(xIn, yIn, widthIn, heightIn, new StringTextComponent(""), MathHelper.clamp((valueIn-min)/(double)(max-min), 0.0D, 1.0D));
        this.max = max;
        this.min = min;
        currentValue = MathHelper.clamp(valueIn, min, max);
        func_230972_a_();
    }

    @Override
    // updateMessage
    protected void func_230972_a_() {
        setMessage(new StringTextComponent(Long.toString(currentValue)));
    }

    @Override
    // applyValue
    protected void func_230979_b_() {
        currentValue = Math.round(this.sliderValue*(max-min)+min);
    }

    public long getCurrentValue() {
        return currentValue;
    }

}
