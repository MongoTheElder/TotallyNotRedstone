package tv.mongotheelder.tnr.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.mongotheelder.tnr.buttons.TimedButtonScreen;
import tv.mongotheelder.tnr.buttons.TimedButtonTile;

import java.util.function.Supplier;

public class TimedButtonOpenGUIPacket {
    private static final Logger LOGGER = LogManager.getLogger();
    private final BlockPos pos;

    public TimedButtonOpenGUIPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(TimedButtonOpenGUIPacket msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static TimedButtonOpenGUIPacket decode(PacketBuffer buf) {
        return new TimedButtonOpenGUIPacket(buf.readBlockPos());
    }

    public static class Handler {
        public static void handle(TimedButtonOpenGUIPacket msg, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                World world = Minecraft.getInstance().world;
                if (world == null) {
                    LOGGER.error("Received a TimedButtonOpenGUIPacket for a null world");
                    context.get().setPacketHandled(true);
                    return;
                }

                if (world.isRemote) {
                    if (!world.isBlockPresent(msg.pos)) {
                        LOGGER.error("Received a TimedButtonOpenGUIPacket for an invalid block position: " + msg.pos);
                        return;
                    }
                    if (world.getTileEntity(msg.pos) instanceof TimedButtonTile) {
                        Minecraft.getInstance().displayGuiScreen(new TimedButtonScreen(new TranslationTextComponent("block.tnr.timed_button"), (TimedButtonTile) world.getTileEntity(msg.pos)));
                    } else {
                        LOGGER.error("Received a TimedButtonOpenGUIPacket for an invalid block type at " + msg.pos);
                    }
                    context.get().setPacketHandled(true);
                }
            });
        }
    }
}
