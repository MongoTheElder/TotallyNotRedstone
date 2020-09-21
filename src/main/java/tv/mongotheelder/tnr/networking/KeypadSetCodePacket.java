package tv.mongotheelder.tnr.networking;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.mongotheelder.tnr.keypad.IKeypad;

import java.util.function.Supplier;

public class KeypadSetCodePacket {
    private static final Logger LOGGER = LogManager.getLogger();
    private final BlockPos pos;
    private final String code;

    public KeypadSetCodePacket(BlockPos pos, String code) {
        this.pos = pos;
        this.code = code;
    }

    public static void encode(KeypadSetCodePacket msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeString(msg.code);
    }

    public static KeypadSetCodePacket decode(PacketBuffer buf) {
        return new KeypadSetCodePacket(buf.readBlockPos(), buf.readString());
    }

    public static class Handler {
        public static void handle(KeypadSetCodePacket msg, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                PlayerEntity player = context.get().getSender();
                if (player == null) {
                    LOGGER.error("Received a KeypadSetCodePacket from a null player");
                    return;
                }
                World world = player.getEntityWorld();
                if (!world.isBlockPresent(msg.pos)) {
                    LOGGER.error("Received a KeypadSetCodePacket for an invalid block position: "+msg.pos);
                    return;
                }
                TileEntity te = world.getTileEntity(msg.pos);
                if (te == null) {
                    LOGGER.error("Received a KeypadSetCodePacket for an invalid tile entity at: "+msg.pos);
                    return;
                }
                ((IKeypad) te).setCode(msg.code);
            });
        }
    }
}
