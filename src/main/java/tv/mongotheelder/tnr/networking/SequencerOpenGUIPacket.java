package tv.mongotheelder.tnr.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.mongotheelder.tnr.TotallyNotRedstone;
import tv.mongotheelder.tnr.sequencer.SequencerBlock;
import tv.mongotheelder.tnr.sequencer.SequencerScreen;
import tv.mongotheelder.tnr.sequencer.SequencerTile;

import java.util.function.Supplier;

public class SequencerOpenGUIPacket {
    private static final Logger LOGGER = LogManager.getLogger();
    private final BlockPos pos;

    public SequencerOpenGUIPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(SequencerOpenGUIPacket msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static SequencerOpenGUIPacket decode(PacketBuffer buf) {
        return new SequencerOpenGUIPacket(buf.readBlockPos());
    }

    public static class Handler {
        public static void handle(SequencerOpenGUIPacket msg, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                World world = Minecraft.getInstance().world;
                if (world == null) {
                    LOGGER.error("Received a SequenceOpenGUIPacket for a null world");
                    context.get().setPacketHandled(true);
                    return;
                }

                if (world.isRemote) {
                    if (!world.isBlockPresent(msg.pos)) {
                        LOGGER.error("Received a SequenceOpenGUIPacket for an invalid block position: " + msg.pos);
                        return;
                    }
                    if (world.getTileEntity(msg.pos) instanceof SequencerTile) {
                        Minecraft.getInstance().displayGuiScreen(new SequencerScreen(new TranslationTextComponent("block.tnr.sequencer"), (SequencerTile) world.getTileEntity(msg.pos)));
                    } else {
                        LOGGER.error("Received a SequenceOpenGUIPacket for an invalid block type at " + msg.pos);
                    }
                    context.get().setPacketHandled(true);
                }
            });
        }
    }
}
