package tv.mongotheelder.tnr.sequencer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SideSelectionButton extends Button {
    protected ResourceLocation resourceLocation;
    protected int count;
    protected int state;
    protected int xTexStart;
    protected int yTexStart;
    protected int xDiffTex;
    protected int yDiffTex;

    private static final Logger LOGGER = LogManager.getLogger();

    public SideSelectionButton(int xIn, int yIn, int widthIn, int heightIn, int count, String label, int state, Button.IPressable onPress) {
        super(xIn, yIn, widthIn, heightIn, label, onPress);
        this.state = state;
        this.count = count;
    }

    public void initTextureValues(int xTexStartIn, int yTexStartIn, int xDiffTexIn, int yDiffTexIn, ResourceLocation resourceLocationIn) {
        this.xTexStart = xTexStartIn;
        this.yTexStart = yTexStartIn;
        this.xDiffTex = xDiffTexIn;
        this.yDiffTex = yDiffTexIn;
        this.resourceLocation = resourceLocationIn;
    }

    public void setState(int state) {
        if (state < 0 || state >= count) {
            LOGGER.error("Attempted to set button state outside acceptable range");
            return;
        }
        this.state = state;
    }

    public void advanceState() {
        state++;
        if (state >= count) {
            state = 0;
        }
    }

    public void decrementState() {
        state--;
        if (state < 0) {
            state = count - 1;
        }
    }

    public int getState() {
        return state;
    }

    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(this.resourceLocation);
        RenderSystem.disableDepthTest();
        int i = this.xTexStart;
        int j = this.yTexStart;
        i += this.xDiffTex * state;

        if (this.isHovered()) {
            j += this.yDiffTex;
        }

        this.blit(this.x, this.y, i, j, this.width, this.height);
        RenderSystem.enableDepthTest();
    }

}
