package tv.mongotheelder.tnr.keypad;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import tv.mongotheelder.tnr.TotallyNotRedstone;

import java.util.HashMap;
import java.util.Map;

public class KeypadScreen extends ContainerScreen<KeypadContainer> {
    private static final int GUI_WIDTH = 161;
    private static final int GUI_HEIGHT = 139;
    private static final int BUTTON_X = 41;
    private static final int BUTTON_Y = 35;
    private static final int BUTTON_SIZE = 20;
    private static final int BUTTON_SPACING = 24;
    private static final int CODE_LENGTH = 5;
    private static final int CODE_OFFSET_X = 29;
    private static final int CODE_OFFSET_Y = 15;
    private static final int CODE_GAP = 22;
    private static final int ROW_MAX = 4;
    private static final int COL_MAX = 3;
    private static final String SET = "Set";
    private static final String CLEAR = "C";
    private static final String ENTER = "E";
    private final Map<String, Button> buttons = new HashMap<>();
    private String userCode = "";

    public KeypadScreen(KeypadContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        xSize = GUI_WIDTH;
        ySize = GUI_HEIGHT;
        if (screenContainer.showCode()) userCode = screenContainer.getCode();
    }

    @Override
    protected void init() {
        super.init();

        placeButtons();
        if (inCreative() || hasProgrammer()) {
            placeSetButton();
        }
        updateDisplay();
    }

    private boolean inCreative() {
        return playerInventory.player.isCreative();
    }

    private boolean hasProgrammer() {
        return ItemTags.getCollection().getOrCreate(TotallyNotRedstone.PROGRAMMER_TAG).contains(playerInventory.getCurrentItem().getItem());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TotallyNotRedstone.KEYPAD_GUI_PATH);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
    }

    private String getLockStateString() {
        boolean isUnlocked = container.getUnlockState();
        return new TranslationTextComponent(isUnlocked ? TotallyNotRedstone.UNLOCKED_KEY : TotallyNotRedstone.LOCKED_KEY).getFormattedText();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TotallyNotRedstone.KEYPAD_GUI_PATH);
        Minecraft.getInstance().fontRenderer.drawString(getLockStateString(), BUTTON_X+BUTTON_SPACING*COL_MAX, BUTTON_Y, 0x000000);
        for (int index = 0; index < userCode.length(); index++) {
            Minecraft.getInstance().fontRenderer.drawString(userCode.substring(index, index+1), (float)(CODE_OFFSET_X+index*CODE_GAP), (float)CODE_OFFSET_Y, 0xff0000);
        }
    }

    private void updateDisplay() {
        if (userCode.length() != CODE_LENGTH) {
            buttons.get(ENTER).active = false;
            if (buttons.containsKey(SET)) buttons.get(SET).active = false;
        } else {
            buttons.get(ENTER).active = true;
            if (buttons.containsKey(SET)) buttons.get(SET).active = true;
        }
    }

    private void placeButtons() {
        int[] buttonMap = { 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, 0, -1 };
        for (int row = 0; row < ROW_MAX; row++) {
            for (int col = 0; col < COL_MAX; col++) {
                int index = row*COL_MAX+col;
                switch (index) {
                    case 9:
                        buttons.put(CLEAR, new Button(getGuiLeft()+BUTTON_X+BUTTON_SPACING*col, getGuiTop()+BUTTON_Y+BUTTON_SPACING*row, BUTTON_SIZE, BUTTON_SIZE, CLEAR, new ClearButtonPress()));
                        addButton(buttons.get(CLEAR));
                        break;
                    case 11:
                        buttons.put(ENTER, new Button(getGuiLeft()+BUTTON_X+BUTTON_SPACING*col, getGuiTop()+BUTTON_Y+BUTTON_SPACING*row, BUTTON_SIZE, BUTTON_SIZE, ENTER, new EnterButtonPress()));
                        buttons.get(ENTER).active = false;
                        addButton(buttons.get(ENTER));
                        break;
                    default:
                        String label = String.format("%d", buttonMap[index]);
                        buttons.put(label, new Button(getGuiLeft()+BUTTON_X+BUTTON_SPACING*col, getGuiTop()+BUTTON_Y+BUTTON_SPACING*row, BUTTON_SIZE, BUTTON_SIZE, label, new NumberButtonPress()));
                        addButton(buttons.get(label));
                }
            }
        }
    }

    private void placeSetButton() {
        buttons.put(SET, new Button(getGuiLeft()+BUTTON_X+BUTTON_SPACING*COL_MAX, getGuiTop()+BUTTON_Y+BUTTON_SPACING*(ROW_MAX-1), BUTTON_SIZE, BUTTON_SIZE, SET, new SetButtonPress()));
        buttons.get(SET).active = false;
        addButton(buttons.get(SET));

    }

    private void addKey(String num) {
        if (userCode.length() == CODE_LENGTH) {
            container.errorSound();
            return;
        }
        userCode += num;
        updateDisplay();
    }

    private void checkCode() {
        if (container.getCode() == null) return;
        if (userCode.length() == CODE_LENGTH && userCode.equals(container.getCode())) {
            container.toggleLock();
        } else {
            container.errorSound();
            clearCode();
            container.setLockState(false);
        }
    }

    private void clearCode() {
        userCode = "";
        updateDisplay();
    }

    private void setCode() {
        container.setCode(userCode);
    }

    class NumberButtonPress implements Button.IPressable {
         @Override
        public void onPress(Button button) {
            addKey(button.getMessage());
        }
    }

    class EnterButtonPress implements Button.IPressable {
        @Override
        public void onPress(Button button) {
            checkCode();
        }
    }
    class ClearButtonPress implements Button.IPressable {
        @Override
        public void onPress(Button button) {
            clearCode();
        }
    }

    class SetButtonPress implements Button.IPressable {
        @Override
        public void onPress(Button button) {
            setCode();
        }
    }

}
