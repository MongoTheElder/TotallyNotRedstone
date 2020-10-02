package tv.mongotheelder.tnr.setup;

import com.mojang.authlib.yggdrasil.response.HasJoinedMinecraftServerResponse;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tv.mongotheelder.tnr.TotallyNotRedstone;
import tv.mongotheelder.tnr.buttons.LargeTimedButton;
import tv.mongotheelder.tnr.buttons.MediumTimedButton;
import tv.mongotheelder.tnr.buttons.SmallTimedButton;
import tv.mongotheelder.tnr.buttons.TimedButtonTile;
import tv.mongotheelder.tnr.indicators.WirelessRedstoneIndicator;
import tv.mongotheelder.tnr.indicators.WirelessRedstoneIndicatorItem;
import tv.mongotheelder.tnr.indicators.WirelessRedstoneIndicatorTile;
import tv.mongotheelder.tnr.keypad.Keypad;
import tv.mongotheelder.tnr.keypad.KeypadContainer;
import tv.mongotheelder.tnr.keypad.KeypadTile;
import tv.mongotheelder.tnr.misc.SolidColors;
import tv.mongotheelder.tnr.sequencer.SequencerBlock;
import tv.mongotheelder.tnr.sequencer.SequencerTile;
import tv.mongotheelder.tnr.receivers.WirelessRedstoneReceiver;
import tv.mongotheelder.tnr.receivers.WirelessRedstoneReceiverItem;
import tv.mongotheelder.tnr.receivers.WirelessRedstoneReceiverTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static tv.mongotheelder.tnr.TotallyNotRedstone.MODID;
import static tv.mongotheelder.tnr.setup.ModSetup.ITEM_GROUP;

public class Registration {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);

    public static Map<String, RegistryObject<WirelessRedstoneIndicator>> WIRELESS_REDSTONE_INDICATOR_BLOCKS = new HashMap<>();
    public static Map<String, RegistryObject<WirelessRedstoneIndicatorItem>> WIRELESS_REDSTONE_INDICATOR_ITEMS = new HashMap<>();

    public static void init() {
        generateWirelessRedstoneIndicators();
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // Sequencer
    public static final RegistryObject<SequencerBlock> SEQUENCER_BLOCK = BLOCKS.register("sequencer", () -> new SequencerBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(1.2f).sound(SoundType.METAL)));
    public static final RegistryObject<Item> SEQUENCER_ITEM = ITEMS.register("sequencer", () -> new BlockItem(SEQUENCER_BLOCK.get(), new Item.Properties().group(ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<SequencerTile>> SEQUENCER_TILE = TILES.register("sequencer", () -> TileEntityType.Builder.create(SequencerTile::new, SEQUENCER_BLOCK.get()).build(null));

    // Keypad
    public static final RegistryObject<Keypad> KEYPAD_BLOCK = BLOCKS.register("keypad", () -> new Keypad(Block.Properties.create(Material.IRON).hardnessAndResistance(hardness(Config.KEYPAD_UNBREAKABLE, 1.2F)).sound(SoundType.METAL)));
    public static final RegistryObject<Item> KEYPAD_ITEM = ITEMS.register("keypad", () -> new BlockItem(KEYPAD_BLOCK.get(), new Item.Properties().group(ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<KeypadTile>>KEYPAD_TILE = TILES.register("keypad", () -> TileEntityType.Builder.create(KeypadTile::new, KEYPAD_BLOCK.get()).build(null));
    public static final RegistryObject<SoundEvent> KEYPAD_ERROR_SOUND = SOUNDS.register("keypad_error", () -> new SoundEvent(new ResourceLocation(TotallyNotRedstone.MODID, "keypad_error")));
    public static final RegistryObject<ContainerType<KeypadContainer>> KEYPAD_CONTAINER = CONTAINERS.register("keypad", () -> IForgeContainerType.create((windowId, inv, data) -> new KeypadContainer(windowId, TotallyNotRedstone.proxy.getClientWorld(), data.readBlockPos())));

    // Buttons
    public static final RegistryObject<LargeTimedButton> LARGE_SQUARE_TIMED_BUTTON_BLOCK = BLOCKS.register("large_timed_button", () -> new LargeTimedButton(Block.Properties.create(Material.ROCK).hardnessAndResistance(hardness(Config.TIMED_BUTTONS_UNBREAKABLE, 0.6f)).sound(SoundType.STONE)));
    public static final RegistryObject<MediumTimedButton> MEDIUM_SQUARE_TIMED_BUTTON_BLOCK = BLOCKS.register("medium_timed_button", () -> new MediumTimedButton(Block.Properties.create(Material.ROCK).hardnessAndResistance(hardness(Config.TIMED_BUTTONS_UNBREAKABLE, 0.6f)).sound(SoundType.STONE)));
    public static final RegistryObject<SmallTimedButton> SMALL_SQUARE_TIMED_BUTTON_BLOCK = BLOCKS.register("small_timed_button", () -> new SmallTimedButton(Block.Properties.create(Material.ROCK).hardnessAndResistance(hardness(Config.TIMED_BUTTONS_UNBREAKABLE, 0.6f)).sound(SoundType.STONE)));
    public static final RegistryObject<Item> LARGE_SQUARE_TIMED_BUTTON_ITEM = ITEMS.register("large_timed_button", () -> new BlockItem(LARGE_SQUARE_TIMED_BUTTON_BLOCK.get(), new Item.Properties().group(ITEM_GROUP)));
    public static final RegistryObject<Item> MEDIUM_SQUARE_TIMED_BUTTON_ITEM = ITEMS.register("medium_timed_button", () -> new BlockItem(MEDIUM_SQUARE_TIMED_BUTTON_BLOCK.get(), new Item.Properties().group(ITEM_GROUP)));
    public static final RegistryObject<Item> SMALL_SQUARE_TIMED_BUTTON_ITEM = ITEMS.register("small_timed_button", () -> new BlockItem(SMALL_SQUARE_TIMED_BUTTON_BLOCK.get(), new Item.Properties().group(ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<TimedButtonTile>> TIMED_BUTTON_TILE = TILES.register("timed_button", () -> TileEntityType.Builder.create(TimedButtonTile::new, LARGE_SQUARE_TIMED_BUTTON_BLOCK.get(), MEDIUM_SQUARE_TIMED_BUTTON_BLOCK.get(), SMALL_SQUARE_TIMED_BUTTON_BLOCK.get()).build(null));
    public static final RegistryObject<SoundEvent> TIMED_BUTTON_BEEP_SOUND = SOUNDS.register("timed_button_beep", () -> new SoundEvent(new ResourceLocation(TotallyNotRedstone.MODID, "timed_button_beep")));
    public static final RegistryObject<SoundEvent> TIMED_BUTTON_LONG_BEEP_SOUND = SOUNDS.register("timed_button_long_beep", () -> new SoundEvent(new ResourceLocation(TotallyNotRedstone.MODID, "timed_button_long_beep")));

    // Wireless Receiver
    public static final RegistryObject<WirelessRedstoneReceiver> WIRELESS_REDSTONE_RECEIVER_BLOCK = BLOCKS.register("wireless_redstone_receiver", () -> new WirelessRedstoneReceiver(Block.Properties.create(Material.IRON).hardnessAndResistance(hardness(Config.WIRELESS_REDSTONE_RECEIVER_UNBREAKABLE, 1.2F)).sound(SoundType.METAL)));
    public static final RegistryObject<WirelessRedstoneReceiverItem> WIRELESS_REDSTONE_RECEIVER_ITEM = ITEMS.register("wireless_redstone_receiver", () -> new WirelessRedstoneReceiverItem(WIRELESS_REDSTONE_RECEIVER_BLOCK.get(), new Item.Properties().group(ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<WirelessRedstoneReceiverTile>> WIRELESS_REDSTONE_RECEIVER_TILE = TILES.register("wireless_redstone_receiver", () -> TileEntityType.Builder.create(WirelessRedstoneReceiverTile::new, WIRELESS_REDSTONE_RECEIVER_BLOCK.get()).build(null));

    // Wireless Indicator
    //public static final RegistryObject<WirelessRedstoneIndicator> WIRELESS_REDSTONE_INDICATOR_BLOCK = BLOCKS.register("wireless_redstone_indicator", () -> new WirelessRedstoneIndicator(Block.Properties.create(Material.IRON).hardnessAndResistance(hardness(Config.WIRELESS_REDSTONE_INDICATOR_UNBREAKABLE, 1.2F)).sound(SoundType.METAL)));
    //public static final RegistryObject<WirelessRedstoneIndicatorItem> WIRELESS_REDSTONE_INDICATOR_ITEM = ITEMS.register("wireless_redstone_indicator", () -> new WirelessRedstoneIndicatorItem(WIRELESS_REDSTONE_INDICATOR_BLOCK.get(), new Item.Properties().group(ITEM_GROUP)));
    public static RegistryObject<TileEntityType<WirelessRedstoneIndicatorTile>> WIRELESS_REDSTONE_INDICATOR_TILE;

    // Utility
    public static final RegistryObject<Item> PROGRAMMER_ITEM = ITEMS.register("programmer", () -> new Item(new Item.Properties().group(ITEM_GROUP)));

    private static float hardness(ForgeConfigSpec.BooleanValue key, float hardnessValue) {
        return key.get() ? -1.0f:hardnessValue;
    }

    private static void generateWirelessRedstoneIndicators() {
        for (SolidColors color : SolidColors.values()) {
            String colorName = color.name().toLowerCase();
            RegistryObject<WirelessRedstoneIndicator> block = BLOCKS.register("wireless_redstone_indicator_" + colorName, () -> new WirelessRedstoneIndicator(Block.Properties.create(Material.IRON).hardnessAndResistance(hardness(Config.WIRELESS_REDSTONE_INDICATOR_UNBREAKABLE, 1.2F)).sound(SoundType.METAL)));
            WIRELESS_REDSTONE_INDICATOR_BLOCKS.put(colorName, block);
            WIRELESS_REDSTONE_INDICATOR_ITEMS.put(colorName, ITEMS.register("wireless_redstone_indicator_"+colorName, () -> new WirelessRedstoneIndicatorItem(block.get(), new Item.Properties().group(ITEM_GROUP))));
        }
        WIRELESS_REDSTONE_INDICATOR_TILE = TILES.register("wireless_redstone_indicator", () -> TileEntityType.Builder.create(WirelessRedstoneIndicatorTile::new, getWirelessRedstoneIndicatorBlocks()).build(null));
    }

    private static Block[] getWirelessRedstoneIndicatorBlocks() {
        Block[] blocks = new Block[SolidColors.values().length];
        int i = 0;
        for (RegistryObject<WirelessRedstoneIndicator> block: WIRELESS_REDSTONE_INDICATOR_BLOCKS.values()) {
            blocks[i++] = block.get();
        }
        return blocks;
    }
}
