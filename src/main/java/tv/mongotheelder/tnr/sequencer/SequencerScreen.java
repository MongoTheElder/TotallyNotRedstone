package tv.mongotheelder.tnr.sequencer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.mongotheelder.tnr.TotallyNotRedstone;
import tv.mongotheelder.tnr.gui.ColorSelectionWidget;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class SequencerScreen extends Screen {
    private static int GUI_WIDTH = 161;
    private static int GUI_HEIGHT = 157;
    private static int BUTTON_SIZE = 16;
    private static int BUTTON_TOP = 10;
    private static int BUTTON_LEFT = 10;
    private static int GAP = 2;
    private static final ResourceLocation COLOR_SELECTION_TEXTURE = new ResourceLocation(TotallyNotRedstone.MODID, "textures/gui/side_buttons.png");
    private static final ResourceLocation TRIGGER_BUTTON_TEXTURE = new ResourceLocation(TotallyNotRedstone.MODID, "textures/gui/trigger_button.png");
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(TotallyNotRedstone.MODID, "textures/gui/sequencer_gui.png");

    private SequencerTile tileEntity;
    private SequencerConfig config;
    private Map<String, Button> sides = new HashMap<>();

    private static final Logger LOGGER = LogManager.getLogger();

    public SequencerScreen(SequencerTile tileEntity) {
        super(new TranslationTextComponent("gui.redstonesequencer.sequencer.title"));
        this.tileEntity = tileEntity;
        if (tileEntity != null) {
            config = tileEntity.getConfig();
        }
        else {
            LOGGER.error("Attempted to create a Sequencer Screen with no associated tile entity");
        }
        setSize(GUI_WIDTH, GUI_HEIGHT);

        placeSideButtons();
        placeSequenceList();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int relX = (this.width - GUI_WIDTH) / 2;
        int relY = (this.height - GUI_HEIGHT) / 2;
        this.blit(relX, relY, 0, 0, GUI_WIDTH, GUI_HEIGHT);
        Minecraft.getInstance().fontRenderer.drawString("Delay", 12, 12, 0xff0000);
        Minecraft.getInstance().fontRenderer.drawString("Duration", 40, 12, 0xff0000);

        super.render(mouseX, mouseY, partialTicks);
    }

    private void placeSideButtons() {
        sides.put("top", newSideButton(0, 1, "top"));
        sides.put("left", newSideButton(1, 0, "left"));
        sides.put("right", newSideButton(1, 2, "right"));
        sides.put("bottom", newSideButton(2, 1, "bottom"));
        sides.put("back", newSideButton(2, 2, "back"));
        sides.put("trigger", newTriggerButton(1, 1));
        for (String key:sides.keySet()) {
            addButton(sides.get(key));
        }
    }

    private ColorSelectionWidget newSideButton(int row, int col, String side) {
        int state = config.getSideColorIndex(side);
        ColorSelectionWidget widget = new ColorSelectionWidget(BUTTON_TOP+(BUTTON_SIZE +GAP)*row, BUTTON_LEFT+(BUTTON_SIZE +GAP)*col, BUTTON_SIZE, BUTTON_SIZE, 6, side, state, new sidePressed());
        widget.initTextureValues(0, 0, BUTTON_SIZE, BUTTON_SIZE, COLOR_SELECTION_TEXTURE);
        return widget;
    }

    private ImageButton newTriggerButton(int row, int col) {
        ImageButton button = new ImageButton(BUTTON_TOP+(BUTTON_SIZE+GAP)*row, BUTTON_LEFT+(BUTTON_SIZE+GAP)*col, BUTTON_SIZE, BUTTON_SIZE, 0, 0, BUTTON_SIZE, TRIGGER_BUTTON_TEXTURE, new triggerPressed());
        return button;
    }

    private void placeSequenceList() {

    }

    private void showTriggerGUI() {

    }

    private void advanceSide(Button button) {
        String label = button.getMessage();
        ColorSelectionWidget widget = (ColorSelectionWidget) sides.get(label);
        widget.advanceState();
    }

    class triggerPressed implements Button.IPressable {
        @Override
        public void onPress(Button button) {
            showTriggerGUI();
        }
    }
    class sidePressed implements Button.IPressable {
        @Override
        public void onPress(Button button) {
            advanceSide(button);
        }
    }

}
