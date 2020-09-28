package tv.mongotheelder.tnr.sequencer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;

public abstract class AbstractFancyScreen extends Screen {
    /** Starting X position for the Gui. Inconsistent use for Gui backgrounds. */
    protected int guiLeft;
    /** Starting Y position for the Gui. Inconsistent use for Gui backgrounds. */
    protected int guiTop;
    /** The X size of the inventory window in pixels. */
    protected int xSize = 176;
    /** The Y size of the inventory window in pixels. */
    protected int ySize = 166;

    protected AbstractFancyScreen(ITextComponent titleIn) {
        super(titleIn);
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {

        this.drawGuiContainerBackgroundLayer(p_render_3_, p_render_1_, p_render_2_);
        //net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiContainerEvent.DrawBackground(this, p_render_1_, p_render_2_));
        RenderSystem.disableRescaleNormal();
        RenderSystem.disableDepthTest();

        super.render(p_render_1_, p_render_2_, p_render_3_);

        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)this.guiLeft, (float)this.guiTop, 0.0F);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableRescaleNormal();

        RenderSystem.glMultiTexCoord2f(33986, 240.0F, 240.0F);

        this.drawGuiContainerForegroundLayer(p_render_1_, p_render_2_);
        //net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiContainerEvent.DrawForeground(this, p_render_1_, p_render_2_));

        RenderSystem.popMatrix();
        RenderSystem.enableDepthTest();
    }

    public int getGuiLeft() { return guiLeft; }
    public int getGuiTop() { return guiTop; }
    public int getXSize() { return xSize; }
    public int getYSize() { return ySize; }

    abstract protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY);
    abstract protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY);

}
