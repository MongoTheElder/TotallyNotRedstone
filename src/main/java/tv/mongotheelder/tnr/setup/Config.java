package tv.mongotheelder.tnr.setup;

import net.minecraftforge.common.ForgeConfigSpec;
import tv.mongotheelder.tnr.TotallyNotRedstone;

public class Config {
    public static final String CATEGORY_BUTTON = "timed_buttons";
    public static final String CATEGORY_KEYPAD = "keypad";
    public static final String CATEGORY_WIRELESS_REDSTONE_RECEIVER = "wireless_redstone_receiver";
    public static final String CATEGORY_WIRELESS_REDSTONE_INDICATOR = "wireless_redstone_indicator";

    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.IntValue WIRELESS_REDSTONE_RECEIVER_TICK;
    public static ForgeConfigSpec.BooleanValue WIRELESS_REDSTONE_RECEIVER_UNBREAKABLE;

    public static ForgeConfigSpec.IntValue WIRELESS_REDSTONE_INDICATOR_TICK;
    public static ForgeConfigSpec.BooleanValue WIRELESS_REDSTONE_INDICATOR_UNBREAKABLE;

    public static ForgeConfigSpec.BooleanValue TIMED_BUTTONS_UNBREAKABLE;
    public static ForgeConfigSpec.IntValue TIMED_BUTTONS_PULSE_COUNT;
    public static ForgeConfigSpec.IntValue TIMED_BUTTONS_PULSE_DURATION;
    public static ForgeConfigSpec.DoubleValue TIMED_BUTTONS_PULSE_RATIO;
    public static ForgeConfigSpec.BooleanValue TIMED_BUTTONS_SETTABLE_WITH_SHIFT_CLICK;
    public static ForgeConfigSpec.BooleanValue TIMED_BUTTONS_ENABLE_SOUND;

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

        COMMON_BUILDER.push(CATEGORY_BUTTON);
        TIMED_BUTTONS_UNBREAKABLE = COMMON_BUILDER.comment(" Make timed buttons unbreakable").define("timed_buttons_unbreakable", false);
        TIMED_BUTTONS_PULSE_DURATION = COMMON_BUILDER.comment(" Number of ticks per pulse").defineInRange("timed_buttons_pulse_duration", 5, 2, 5000);
        TIMED_BUTTONS_PULSE_COUNT = COMMON_BUILDER.comment(" Default number of pulses between off and on").defineInRange("timed_buttons_pulse_count_default", 5, 1, TotallyNotRedstone.MAX_TIMED_BUTTON_PULSES);
        TIMED_BUTTONS_PULSE_RATIO = COMMON_BUILDER.comment(" Percentage of each pulse that the indicator lit").defineInRange("timed_buttons_pulse_ratio", 0.3, 0.01, 0.99);
        TIMED_BUTTONS_SETTABLE_WITH_SHIFT_CLICK = COMMON_BUILDER.comment(" Make timed buttons settable by the player by sneaking in addition to creative mode and the programmer").define("timed_buttons_sneaking", false);
        TIMED_BUTTONS_ENABLE_SOUND = COMMON_BUILDER.comment(" Default setting for 'Enable Sound'").define("timed_buttons_sound", true);

        COMMON_BUILDER.pop().push(CATEGORY_KEYPAD);
        KEYPAD_UNBREAKABLE = COMMON_BUILDER.comment(" Make keypads unbreakable").define("keypad_unbreakable", false);
        KEYPAD_SHOWS_CODE_WHEN_UNLOCKED = COMMON_BUILDER.comment(" Keypad remembers correctly entered code").define("keypad_remembers_code", true);

        COMMON_BUILDER.pop().push(CATEGORY_WIRELESS_REDSTONE_RECEIVER);
        WIRELESS_REDSTONE_RECEIVER_TICK = COMMON_BUILDER.comment(" Number of game ticks between redstone receiver updates").defineInRange("wireless_redstone_receiver_tick", 4, 1, 20);
        WIRELESS_REDSTONE_RECEIVER_UNBREAKABLE = COMMON_BUILDER.comment(" Make wireless redstone receivers unbreakable").define("redstone_receivers_unbreakable", false);

        COMMON_BUILDER.pop().push(CATEGORY_WIRELESS_REDSTONE_INDICATOR);
        WIRELESS_REDSTONE_INDICATOR_TICK = COMMON_BUILDER.comment(" Number of game ticks between redstone indicator updates").defineInRange("wireless_redstone_indicator_tick", 4, 1, 20);
        WIRELESS_REDSTONE_INDICATOR_UNBREAKABLE = COMMON_BUILDER.comment(" Make wireless redstone indicators unbreakable").define("redstone_indicators_unbreakable", false);

        COMMON_BUILDER.pop();
    }
}
