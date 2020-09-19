package tv.mongotheelder.tnr.setup;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tv.mongotheelder.tnr.sequencer.SequencerBlock;
import tv.mongotheelder.tnr.sequencer.SequencerTile;
import tv.mongotheelder.tnr.wireless.WirelessRedstoneReceiver;
import tv.mongotheelder.tnr.wireless.WirelessRedstoneReceiverItem;
import tv.mongotheelder.tnr.wireless.WirelessRedstoneReceiverTile;

import static tv.mongotheelder.tnr.TotallyNotRedstone.MODID;
import static tv.mongotheelder.tnr.setup.ModSetup.ITEM_GROUP;

public class Registration {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);

    public static void init() {
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

    // Buttons

    // Wireless
    public static final RegistryObject<WirelessRedstoneReceiver> WIRELESS_REDSTONE_RECEIVER_BLOCK = BLOCKS.register("wireless_redstone_receiver", () -> new WirelessRedstoneReceiver(Block.Properties.create(Material.IRON).hardnessAndResistance(1.2f).sound(SoundType.METAL)));
    public static final RegistryObject<WirelessRedstoneReceiverItem> WIRELESS_REDSTONE_RECEIVER_ITEM = ITEMS.register("wireless_redstone_receiver", () -> new WirelessRedstoneReceiverItem(WIRELESS_REDSTONE_RECEIVER_BLOCK.get(), new Item.Properties().group(ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<WirelessRedstoneReceiverTile>> WIRELESS_REDSTONE_RECEIVER_TILE = TILES.register("wireless_redstone_receiver", () -> TileEntityType.Builder.create(WirelessRedstoneReceiverTile::new, WIRELESS_REDSTONE_RECEIVER_BLOCK.get()).build(null));

}
