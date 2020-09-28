package tv.mongotheelder.tnr.sequencer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TriggerModeButton extends AbstractButton {
    protected final TriggerModeButton.IPressable onPress;
    protected final SequencerMode mode;
    protected ResourceLocation resourceLocation;
    protected boolean stateTriggered;
    protected int xTexStart;
    protected int yTexStart;
    protected int xDiffTex;
    protected int yDiffTex;

    public TriggerModeButton(int xIn, int yIn, int widthIn, int heightIn, SequencerMode mode, SequencerMode activeMode, TriggerModeButton.IPressable onPress) {
        super(xIn, yIn, widthIn, heightIn, "");
        this.onPress = onPress;
        this.mode = mode;
        this.stateTriggered = (mode == activeMode);
    }

    public void onPress() {
        this.onPress.onPress(this);
    }

    public void initTextureValues(int xTexStartIn, int yTexStartIn, int xDiffTexIn, int yDiffTexIn, ResourceLocation resourceLocationIn) {
        this.xTexStart = xTexStartIn;
        this.yTexStart = yTexStartIn;
        this.xDiffTex = xDiffTexIn;
        this.yDiffTex = yDiffTexIn;
        this.resourceLocation = resourceLocationIn;
    }

    public void setStateTriggered(boolean triggered) {
        this.stateTriggered = triggered;
    }

    public boolean isStateTriggered() {
        return this.stateTriggered;
    }

    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(this.resourceLocation);
        RenderSystem.disableDepthTest();
        int i = this.xTexStart;
        int j = this.yTexStart;
        if (this.stateTriggered) {
            i += this.xDiffTex;
        }

        if (this.isHovered()) {
            j += this.yDiffTex;
        }

        this.blit(this.x, this.y, i, j, this.width, this.height);
        RenderSystem.enableDepthTest();
    }

    @OnlyIn(Dist.CLIENT)
    public interface IPressable {
        void onPress(TriggerModeButton button);
    }
}
