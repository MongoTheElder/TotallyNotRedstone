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
    public static final String WIRELESS_REDSTONE_RECEIVER_TAG = "wireless_redstone_receiver";
    public static final String KEYPAD_CODE_TAG = "keypad";
    public static final String LOCKED_KEY = "gui.tnr.keypad.locked";
    public static final String UNLOCKED_KEY = "gui.tnr.keypad.unlocked";
    public static final ResourceLocation KEYPAD_GUI_PATH = new ResourceLocation(TotallyNotRedstone.MODID, "textures/gui/keypad_gui.png");

    public static IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    private static final Logger LOGGER = LogManager.getLogger();
    public TotallyNotRedstone() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        Registration.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
    }
}
