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
import tv.mongotheelder.tnr.buttons.TimedButton;
import tv.mongotheelder.tnr.buttons.TimedButtonTile;
import tv.mongotheelder.tnr.sequencer.SequencerBlock;
import tv.mongotheelder.tnr.sequencer.SequencerTile;

import java.util.function.Supplier;

public class TimedButtonNBTPacket {
    private static final Logger LOGGER = LogManager.getLogger();
    private final BlockPos pos;
    private final CompoundNBT config;

    public TimedButtonNBTPacket(BlockPos pos, CompoundNBT config) {
        this.pos = pos;
        this.config = config;
    }

    public static void encode(TimedButtonNBTPacket msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeCompoundTag(msg.config);
    }

    public static TimedButtonNBTPacket decode(PacketBuffer buf) {
        return new TimedButtonNBTPacket(buf.readBlockPos(), buf.readCompoundTag());
    }

    public static class Handler {
        public static void handle(TimedButtonNBTPacket msg, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                PlayerEntity player = context.get().getSender();
                if (player == null) {
                    LOGGER.error("Received a TimedButtonNBTPacket from a null player");
                    return;
                }
                World world = player.getEntityWorld();
                if (!world.isBlockPresent(msg.pos)) {
                    LOGGER.error("Received a TimedButtonNBTPacket for an invalid block position at: "+msg.pos);
                    return;
                }
                if (!(world.getBlockState(msg.pos).getBlock() instanceof TimedButton)) {
                    LOGGER.error("Received a TimedButtonNBTPacket for an invalid block type at: "+msg.pos);
                    return;
                }
                TileEntity te = world.getTileEntity(msg.pos);
                if (!(te instanceof TimedButtonTile)) {
                    LOGGER.error("Received a TimedButtonNBTPacket for an invalid tile type at: "+msg.pos);
                    return;
                }
                ((TimedButtonTile)te).setConfig(msg.config);
            });
        }
    }
}
