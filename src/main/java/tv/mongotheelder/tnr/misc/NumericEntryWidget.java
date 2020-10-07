package tv.mongotheelder.tnr.misc;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;

import java.util.function.Consumer;

public class NumericEntryWidget extends TextFieldWidget {
    private long entryValue;
    private long minValue = 0;
    private long maxValue = 1000000;
    private int defaultColor;

    public NumericEntryWidget(FontRenderer fontIn, int xIn, int yIn, int widthIn, int heightIn, String msg) {
        super(fontIn, xIn, yIn, widthIn, heightIn, new StringTextComponent(msg));
        setResponder(new Validator(this));
        defaultColor = this.getFGColor();
    }

    private void setValue(String s) {
        entryValue = Long.parseLong(s);
    }

    public long getValue() {
        return entryValue;
    }

    public void setRange(long min, long max) {
        this.maxValue = max;
        this.minValue = min;
    }

    public class Validator implements Consumer<String> {
        private final NumericEntryWidget widget;

        private Validator(NumericEntryWidget widget) {
            this.widget = widget;
        }

        @Override
        public void accept(String s) {
            if (s.matches("[0-9]+")) {
                long v = Long.parseLong(s);
                if (v >= minValue && v <= maxValue) {
                    widget.setTextColor(defaultColor);
                    setValue(s);
                    return;
                }
            }
            widget.setTextColor(0xff0000);
        }
    }
}
