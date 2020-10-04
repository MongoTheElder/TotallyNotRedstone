package tv.mongotheelder.tnr.sequencer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.mongotheelder.tnr.TotallyNotRedstone;
import tv.mongotheelder.tnr.misc.NumericEntryWidget;

public class SequencerScreen extends AbstractFancyScreen {
    private static final int GUI_WIDTH = 160;
    private static final int GUI_HEIGHT = 171;

    private static final int ROW_COUNT = 5;
    private static final int SIDE_BUTTON_X = 6;
    private static final int TIME_ENTRY_X = 25;
    private static final int TIME_ENTRY_Y = 4;
    private static final int ENTRY_WIDTH = 56;
    private static final int ENTRY_HEIGHT = 16;
    private static final int GAP = 2;
    private static final int BUTTON_SIZE = 16;
    private static final int SLIDER_HEIGHT = 20;

    private static final int CONTROL_GROUP_Y = TIME_ENTRY_Y +(ENTRY_HEIGHT+GAP)*ROW_COUNT+11;
    private final SideSelectionButton[] sides = new SideSelectionButton[SequencerConfig.SEQUENCE_COUNT];
    private final TriggerModeButton[] trigger = new TriggerModeButton[SequencerMode.values().length];
    private SequencerTile tileEntity;
    private SequencerConfig blockConfig;
    private IntegerRangeSlider slider;
    private NumericEntryWidget[] delays = new NumericEntryWidget[SequencerConfig.SEQUENCE_COUNT];
    private NumericEntryWidget[] durations = new NumericEntryWidget[SequencerConfig.SEQUENCE_COUNT];

    private static final Logger LOGGER = LogManager.getLogger();

    public SequencerScreen(ITextComponent titleIn, SequencerTile tileEntity) {
        super(titleIn);
        xSize = GUI_WIDTH;
        ySize = GUI_HEIGHT;
        this.tileEntity = tileEntity;
    }

    @Override
    protected void init() {
        super.init();
        blockConfig = tileEntity.getConfig();

        placeTextFields();
        placeSideButtons();
        placeTriggerButtons();
        placeThresholdSlider();
    }

    private String getTranslatedString(String item) {
        return new TranslationTextComponent("gui.tnr.sequencer."+item).getFormattedText();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TotallyNotRedstone.SEQUENCER_GUI_PATH);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TotallyNotRedstone.SEQUENCER_GUI_PATH);
        Minecraft.getInstance().fontRenderer.drawString(getTranslatedString("delay"), TIME_ENTRY_X, TIME_ENTRY_Y, 0x000000);
        Minecraft.getInstance().fontRenderer.drawString(getTranslatedString("duration"), TIME_ENTRY_X +ENTRY_WIDTH+GAP, TIME_ENTRY_Y, 0x000000);
        Minecraft.getInstance().fontRenderer.drawString(getTranslatedString("trigger"), SIDE_BUTTON_X +ENTRY_WIDTH+GAP, CONTROL_GROUP_Y, 0x000000);
        Minecraft.getInstance().fontRenderer.drawString(getTranslatedString("threshold"), SIDE_BUTTON_X +ENTRY_WIDTH+GAP, CONTROL_GROUP_Y+BUTTON_SIZE+GAP+13, 0x000000);
    }

    private NumericEntryWidget textField(FontRenderer fr, int row, int col, long value) {
        NumericEntryWidget widget = new NumericEntryWidget(fr, getGuiLeft()+ TIME_ENTRY_X +(ENTRY_WIDTH+GAP)*col, getGuiTop()+ TIME_ENTRY_Y +9+(ENTRY_HEIGHT+GAP)*row, ENTRY_WIDTH, ENTRY_HEIGHT, "");
        widget.setText(Long.toString(value));
        widget.setRange(0, TotallyNotRedstone.MAX_DELAY_AND_DURATION);
        addButton(widget);
        return widget;
    }

    private void placeTextFields() {
        FontRenderer fr = Minecraft.getInstance().fontRenderer;

        for(int row = 0; row < SequencerConfig.SEQUENCE_COUNT; row++) {
            delays[row] = textField(fr, row, 0, blockConfig.getSequence(row).getDelay());
            durations[row] = textField(fr, row, 1, blockConfig.getSequence(row).getDuration());
        }
    }

    private void placeSideButtons() {
        sides[0] = new SideSelectionButton(getGuiLeft()+ SIDE_BUTTON_X +BUTTON_SIZE+GAP, getGuiTop()+CONTROL_GROUP_Y, BUTTON_SIZE, BUTTON_SIZE, SequencerConfig.COLOR_COUNT, "top", blockConfig.getSideColorIndex("top"), new AdvanceButtonColor());
        sides[1] = new SideSelectionButton(getGuiLeft()+ SIDE_BUTTON_X, getGuiTop()+CONTROL_GROUP_Y+BUTTON_SIZE+GAP, BUTTON_SIZE, BUTTON_SIZE, SequencerConfig.COLOR_COUNT, "left", blockConfig.getSideColorIndex("left"), new AdvanceButtonColor());
        sides[2] = new SideSelectionButton(getGuiLeft()+ SIDE_BUTTON_X +(BUTTON_SIZE+GAP)*2, getGuiTop()+CONTROL_GROUP_Y+BUTTON_SIZE+GAP, BUTTON_SIZE, BUTTON_SIZE, SequencerConfig.COLOR_COUNT, "right", blockConfig.getSideColorIndex("right"), new AdvanceButtonColor());
        sides[3] = new SideSelectionButton(getGuiLeft()+ SIDE_BUTTON_X +BUTTON_SIZE+GAP, getGuiTop()+CONTROL_GROUP_Y+(BUTTON_SIZE+GAP)*2, BUTTON_SIZE, BUTTON_SIZE, SequencerConfig.COLOR_COUNT, "bottom", blockConfig.getSideColorIndex("bottom"), new AdvanceButtonColor());
        sides[4] = new SideSelectionButton(getGuiLeft()+ SIDE_BUTTON_X +(BUTTON_SIZE+GAP)*2, getGuiTop()+CONTROL_GROUP_Y+(BUTTON_SIZE+GAP)*2, BUTTON_SIZE, BUTTON_SIZE, SequencerConfig.COLOR_COUNT, "back", blockConfig.getSideColorIndex("back"), new AdvanceButtonColor());
        for (SideSelectionButton widget: sides) {
            widget.initTextureValues(0, 0, 16, 16, TotallyNotRedstone.GUI_BUTTONS_PATH);
            addButton(widget);
        }
    }

    private void placeTriggerButtons() {
        for (int i = 0; i < 5; i++) {
            trigger[i] = new TriggerModeButton(getGuiLeft()+ SIDE_BUTTON_X +ENTRY_WIDTH+GAP+(BUTTON_SIZE+GAP)*i, getGuiTop()+CONTROL_GROUP_Y+GAP+10, BUTTON_SIZE, BUTTON_SIZE, SequencerMode.getByIndex(i), blockConfig.getMode(), new SequencerScreen.SetTriggerMode());
            trigger[i].initTextureValues(0, (i+1)*32, 16, 16, TotallyNotRedstone.GUI_BUTTONS_PATH);
            addButton(trigger[i]);
        }
    }

    private  void placeThresholdSlider() {
        slider = new IntegerRangeSlider(getGuiLeft()+SIDE_BUTTON_X+ENTRY_WIDTH+GAP, getGuiTop()+CONTROL_GROUP_Y+BUTTON_SIZE+GAP+23, (BUTTON_SIZE+GAP)*5-GAP, SLIDER_HEIGHT, 0, 14, blockConfig.getThreshold());
        addButton(slider);
    }

    static class AdvanceButtonColor implements Button.IPressable {
        @Override
        public void onPress(Button button) {
            ((SideSelectionButton) button).advanceState();
        }
    }

    class SetTriggerMode implements TriggerModeButton.IPressable {
        @Override
        public void onPress(TriggerModeButton button) {
            for(TriggerModeButton b: trigger) {
                b.setStateTriggered(b.mode == button.mode);
            }
        }
    }

    @Override
    public void onClose() {
        updateConfig();
        super.onClose();
    }

    private void updateConfig() {
        SequencerConfig newConfig = new SequencerConfig();
        for (TriggerModeButton b: trigger) {
            if (b.stateTriggered) newConfig.setMode(b.mode);
        }
        newConfig.setThreshold(slider.getCurrentValue());
        for (SideSelectionButton side: sides) {
            newConfig.setSideColorIndex(side.getMessage(), side.getState());
        }
        for (int row = 0; row < SequencerConfig.SEQUENCE_COUNT; row++) {
            SequenceDefinition s = new SequenceDefinition(delays[row].getValue(), durations[row].getValue());
            newConfig.setSequenceDefinition(row, s);
        }
        tileEntity.setConfig(newConfig);
    }
}
