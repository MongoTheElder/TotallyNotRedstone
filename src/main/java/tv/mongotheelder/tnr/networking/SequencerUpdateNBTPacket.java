package tv.mongotheelder.tnr.networking;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.mongotheelder.tnr.sequencer.SequencerBlock;
import tv.mongotheelder.tnr.sequencer.SequencerTile;

import java.util.function.Supplier;

public class SequencerUpdateNBTPacket {
    private static final Logger LOGGER = LogManager.getLogger();
    private final BlockPos pos;
    private final CompoundNBT config;

    public SequencerUpdateNBTPacket(BlockPos pos, CompoundNBT config) {
        this.pos = pos;
        this.config = config;
    }

    public static void encode(SequencerUpdateNBTPacket msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeCompoundTag(msg.config);
    }

    public static SequencerUpdateNBTPacket decode(PacketBuffer buf) {
        return new SequencerUpdateNBTPacket(buf.readBlockPos(), buf.readCompoundTag());
    }

    public static class Handler {
        public static void handle(SequencerUpdateNBTPacket msg, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                PlayerEntity player = context.get().getSender();
                if (player == null) {
                    LOGGER.error("Received a SequencerUpdateNBTPacket from a null player");
                    return;
                }
                World world = player.getEntityWorld();
                if (!world.isBlockPresent(msg.pos)) {
                    LOGGER.error("Received a SequencerUpdateNBTPacket for an invalid block position at: "+msg.pos);
                    return;
                }
                if (!(world.getBlockState(msg.pos).getBlock() instanceof SequencerBlock)) {
                    LOGGER.error("Received a SequencerUpdateNBTPacket for an invalid block type at: "+msg.pos);
                    return;
                }
                TileEntity te = world.getTileEntity(msg.pos);
                if (!(te instanceof SequencerTile)) {
                    LOGGER.error("Received a SequencerUpdateNBTPacket for an invalid tile type at: "+msg.pos);
                    return;
                }
                ((SequencerTile)te).setConfig(msg.config);
            });
        }
    }
}
