package tv.mongotheelder.tnr.buttons;

import net.minecraft.client.gui.widget.ToggleWidget;

public class BetterToggleWidget extends ToggleWidget {

    public BetterToggleWidget(int xIn, int yIn, int widthIn, int heightIn, boolean triggered) {
        super(xIn, yIn, widthIn, heightIn, triggered);
    }

    @Override
    public void onClick(double p_onClick_1_, double p_onClick_3_) {
        super.onClick(p_onClick_1_, p_onClick_3_);
        setStateTriggered(!isStateTriggered());
    }
}
