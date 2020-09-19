package tv.mongotheelder.tnr.networking;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.mongotheelder.tnr.sequencer.SequencerBlock;

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
                PlayerEntity player = context.get().getSender();
                if (player == null || !player.isAddedToWorld()) {
                    LOGGER.error("Received a SequenceOpenGUIPacket for an invalid player");
                    return;
                }
                World world = player.getEntityWorld();

                if (world.isRemote) {
                    if (!world.isBlockPresent(msg.pos)) {
                        LOGGER.error("Received a SequenceOpenGUIPacket for an invalid block position: " + msg.pos);
                        return;
                    }
                    if (world.getBlockState(msg.pos).getBlock() instanceof SequencerBlock) {
                        // Open GUI
                    } else {
                        LOGGER.error("Received a SequenceOpenGUIPacket for an invalid block type at " + msg.pos);
                    }
                }
            });
        }
    }
}
