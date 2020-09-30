package tv.mongotheelder.tnr;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.mongotheelder.tnr.setup.*;

@Mod("tnr")
public class TotallyNotRedstone {
    public static final String MODID = "tnr";
    public static final String SEQUENCER_CONFIG_TAG = "sequencer_config";
    public static final ResourceLocation REDSTONE_CONTROLLERS_TAG = new ResourceLocation(TotallyNotRedstone.MODID, "redstone_controllers");
    public static final ResourceLocation PROGRAMMER_TAG = new ResourceLocation(TotallyNotRedstone.MODID, "programmers");
    public static final String TIMED_BUTTON_PULSE_COUNT_KEY = "gui.tnr.timed_buttons.pulse_count";
    public static final String TIMED_BUTTON_ENABLE_SOUND_KEY = "gui.tnr.timed_buttons.enable_sound";
    public static final String TIMED_BUTTONS_TAG = "timed_buttons";
    public static final String WIRELESS_REDSTONE_SOURCE_POS_TAG = "wireless_redstone_source_pos";
    public static final String KEYPAD_CODE_TAG = "keypad";
    public static final String LOCKED_KEY = "gui.tnr.keypad.locked";
    public static final String UNLOCKED_KEY = "gui.tnr.keypad.unlocked";
    public static final ResourceLocation KEYPAD_GUI_PATH = new ResourceLocation(TotallyNotRedstone.MODID, "textures/gui/keypad_gui.png");
    public static final ResourceLocation SEQUENCER_GUI_PATH = new ResourceLocation(TotallyNotRedstone.MODID, "textures/gui/sequencer_gui.png");
    public static final ResourceLocation GUI_BUTTONS_PATH = new ResourceLocation(TotallyNotRedstone.MODID, "textures/gui/gui_buttons.png");
    public static final ResourceLocation TIMED_BUTTON_GUI_PATH = new ResourceLocation(TotallyNotRedstone.MODID, "textures/gui/timed_button_gui.png");

    public static IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    private static final Logger LOGGER = LogManager.getLogger();
    public TotallyNotRedstone() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        Registration.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
    }
}
