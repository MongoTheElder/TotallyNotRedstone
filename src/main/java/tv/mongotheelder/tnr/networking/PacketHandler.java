package tv.mongotheelder.tnr.networking;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import tv.mongotheelder.tnr.TotallyNotRedstone;
import tv.mongotheelder.tnr.buttons.TimedButtonTile;
import tv.mongotheelder.tnr.sequencer.SequencerTile;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PacketHandler {
    private static int ID = 0;
    private static final String PROTOCOL_VERSION = Integer.toString(1);

    public static int nextID() {
        return ID++;
    }

    public static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(TotallyNotRedstone.MODID, "general"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void register() {
        registerMessage(SequencerUpdateNBTPacket.class, SequencerUpdateNBTPacket::encode, SequencerUpdateNBTPacket::decode, SequencerUpdateNBTPacket.Handler::handle);
        registerMessage(SequencerOpenGUIPacket.class, SequencerOpenGUIPacket::encode, SequencerOpenGUIPacket::decode, SequencerOpenGUIPacket.Handler::handle);
        registerMessage(KeypadSetCodePacket.class, KeypadSetCodePacket::encode, KeypadSetCodePacket::decode, KeypadSetCodePacket.Handler::handle);
        registerMessage(KeypadSetStatePacket.class, KeypadSetStatePacket::encode, KeypadSetStatePacket::decode, KeypadSetStatePacket.Handler::handle);
        registerMessage(TimedButtonNBTPacket.class, TimedButtonNBTPacket::encode, TimedButtonNBTPacket::decode, TimedButtonNBTPacket.Handler::handle);
        registerMessage(TimedButtonOpenGUIPacket.class, TimedButtonOpenGUIPacket::encode, TimedButtonOpenGUIPacket::decode, TimedButtonOpenGUIPacket.Handler::handle);
    }

    public static void sendToServer(Object msg) {
        HANDLER.sendToServer(msg);
    }

    public static void sendToPlayer(Object msg, ServerPlayerEntity player) {
        HANDLER.sendTo(msg, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    private static <MSG> void registerMessage(Class<MSG> messageType, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
        HANDLER.registerMessage(nextID(), messageType, encoder, decoder, messageConsumer);
        if (ID > 0xFF)
            throw new RuntimeException("Too many messages!");
    }

    public static void sendSequencerNBT(SequencerTile te, CompoundNBT config) {

        if (te != null && te.getWorld() != null && te.getWorld().isRemote) {
            SequencerUpdateNBTPacket msg = new SequencerUpdateNBTPacket(te.getPos(), config);
            PacketHandler.sendToServer(msg);
        }
    }

    public static void sendTimedButtonNBT(TimedButtonTile te, CompoundNBT config) {
        if (te != null && te.getWorld() != null && te.getWorld().isRemote) {
            TimedButtonNBTPacket msg = new TimedButtonNBTPacket(te.getPos(), config);
            PacketHandler.sendToServer(msg);
        }
    }

    public static void sendSequencerOpenGUI(BlockPos pos, ServerPlayerEntity player) {
        SequencerOpenGUIPacket msg = new SequencerOpenGUIPacket(pos);
        PacketHandler.sendToPlayer(msg, player);
    }

    public static void sendTimedButtonOpenGUI(BlockPos pos, ServerPlayerEntity player) {
        TimedButtonOpenGUIPacket msg = new TimedButtonOpenGUIPacket(pos);
        PacketHandler.sendToPlayer(msg, player);
    }

    public static void sendKeypadSetCode(World world, BlockPos pos, String code) {
        // Send code string to the server
        if (world != null && world.isRemote) {
            KeypadSetCodePacket msg = new KeypadSetCodePacket(pos, code);
            PacketHandler.sendToServer(msg);
        }
    }

    public static void sendKeypadSetState(World world, BlockPos pos, boolean state) {
        // Send code string to the server
        if (world != null && world.isRemote) {
            KeypadSetStatePacket msg = new KeypadSetStatePacket(pos, state);
            PacketHandler.sendToServer(msg);
        }
    }
}

