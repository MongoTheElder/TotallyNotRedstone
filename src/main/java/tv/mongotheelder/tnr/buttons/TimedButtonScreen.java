package tv.mongotheelder.tnr.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import tv.mongotheelder.tnr.TotallyNotRedstone;
import tv.mongotheelder.tnr.misc.NumericEntryWidget;
import tv.mongotheelder.tnr.sequencer.AbstractFancyScreen;

public class TimedButtonScreen extends AbstractFancyScreen {
    private static final int GUI_WIDTH = 91;
    private static final int GUI_HEIGHT = 57;
    private static final int GAP = 2;
    private static final int DELAY_X = 17;
    private static final int DELAY_Y = 17;
    private static final int TEXT_HEIGHT = 10;
    private static final int ENTRY_WIDTH = 56;
    private static final int ENTRY_HEIGHT = 16;
    private static final int BUTTON_SIZE = 16;
    private NumericEntryWidget pulseWidget;
    private BetterToggleWidget enableSoundWidget;

    private final TimedButtonTile tileEntity;

    private int pulseCount;
    private boolean enableSound;

    public TimedButtonScreen(ITextComponent titleIn, TimedButtonTile tileEntity) {
        super(titleIn);
        this.tileEntity = tileEntity;
        xSize = GUI_WIDTH;
        ySize = GUI_HEIGHT;
        pulseCount = tileEntity.getPulseCount();
        enableSound = tileEntity.getEnableSound();
    }

    @Override
    protected void init() {
        super.init();
        pulseWidget = new NumericEntryWidget(Minecraft.getInstance().fontRenderer, getGuiLeft()+DELAY_X, getGuiTop()+DELAY_Y, ENTRY_WIDTH, ENTRY_HEIGHT, "");
        pulseWidget.setRange(0, TotallyNotRedstone.MAX_TIMED_BUTTON_PULSES);
        pulseWidget.setText(String.format("%d", pulseCount));
        addButton(pulseWidget);
        enableSoundWidget = new BetterToggleWidget(getGuiLeft()+DELAY_X, getGuiTop()+DELAY_Y+ENTRY_HEIGHT+GAP, BUTTON_SIZE, BUTTON_SIZE, enableSound);
        enableSoundWidget.initTextureValues(32, 32,  BUTTON_SIZE,  BUTTON_SIZE, TotallyNotRedstone.GUI_BUTTONS_PATH);
        addButton(enableSoundWidget);
    }

    private String getTranslatedString(String key) {
        return new TranslationTextComponent(key).getString();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TotallyNotRedstone.TIMED_BUTTON_GUI_PATH);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, getTranslatedString(TotallyNotRedstone.TIMED_BUTTON_PULSE_COUNT_KEY), getGuiLeft()+DELAY_X, getGuiTop()+DELAY_Y-TEXT_HEIGHT, 0x000000);
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, getTranslatedString(TotallyNotRedstone.TIMED_BUTTON_ENABLE_SOUND_KEY), getGuiLeft()+DELAY_X+BUTTON_SIZE+GAP, getGuiTop()+DELAY_Y+ENTRY_HEIGHT+3*GAP, 0x000000);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {

    }

    @Override
    public void onClose() {
        pulseCount = (int) pulseWidget.getValue();
        enableSound = enableSoundWidget.isStateTriggered();
        tileEntity.setConfig(pulseCount, enableSound);
        super.onClose();
    }
}
