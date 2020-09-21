package tv.mongotheelder.tnr.setup;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

public class Config {
    public static final String CATEGORY_SEQUENCER = "redstone_sequencer";
    public static final String CATEGORY_BUTTON = "timed_buttons";
    public static final String CATEGORY_KEYPAD = "keypad";
    public static final String CATEGORY_WIRELESS_REDSTONE_RECEIVER = "wireless_redstone_receiver";

    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue ENABLE_SEQUENCER_BLOCKS;
    public static ForgeConfigSpec.IntValue WIRELESS_REDSTONE_RECEIVER_TICK;

    public static ForgeConfigSpec.BooleanValue ENABLE_TIMED_BUTTONS;
    public static ForgeConfigSpec.BooleanValue TIMED_BUTTONS_UNBREAKABLE;

    public static ForgeConfigSpec.BooleanValue ENABLE_KEYPAD;
    public static ForgeConfigSpec.BooleanValue ENABLE_PROGRAMMER;
    public static ForgeConfigSpec.BooleanValue KEYPAD_UNBREAKABLE;
    public static ForgeConfigSpec.BooleanValue KEYPAD_SHOWS_CODE_WHEN_UNLOCKED;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        setupItems(COMMON_BUILDER);
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static void setupItems(ForgeConfigSpec.Builder COMMON_BUILDER) {
        COMMON_BUILDER.comment("Totally Not Redstone");
        COMMON_BUILDER.comment("(OK, it's totally redstone)");

        COMMON_BUILDER.push(CATEGORY_SEQUENCER);
        ENABLE_SEQUENCER_BLOCKS = COMMON_BUILDER.comment(" Enable Redstone Sequencer").define("enable_redstone_sequencer", true);
        COMMON_BUILDER.pop().push(CATEGORY_BUTTON);
        ENABLE_TIMED_BUTTONS = COMMON_BUILDER.comment(" Enable Timed Buttons").define("enable_timed_buttons", true);
        TIMED_BUTTONS_UNBREAKABLE = COMMON_BUILDER.comment(" Make timed buttons unbreakable").define("timed_buttons_unbreakable", true);
        COMMON_BUILDER.pop().push(CATEGORY_KEYPAD);
        ENABLE_KEYPAD = COMMON_BUILDER.comment(" Enable Keypad").define("enable_keypad", true);
        ENABLE_PROGRAMMER = COMMON_BUILDER.comment(" Enable Keypad Programmer").define("enable_programmer", true);
        KEYPAD_UNBREAKABLE = COMMON_BUILDER.comment(" Make keypads unbreakable").define("keypad_unbreakable", true);
        KEYPAD_SHOWS_CODE_WHEN_UNLOCKED = COMMON_BUILDER.comment(" Keypad remembers correctly entered code").define("keypad_remembers_code", true);
        COMMON_BUILDER.pop().push(CATEGORY_WIRELESS_REDSTONE_RECEIVER);
        WIRELESS_REDSTONE_RECEIVER_TICK = COMMON_BUILDER.comment(" Number of game ticks between redstone receiver updates").defineInRange("wireless_redstone_receiver_tick", 4, 1, 20);

        COMMON_BUILDER.pop();
    }

}
