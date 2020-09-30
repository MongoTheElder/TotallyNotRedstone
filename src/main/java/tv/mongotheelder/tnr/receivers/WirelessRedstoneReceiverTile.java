package tv.mongotheelder.tnr.receivers;

import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.mongotheelder.tnr.setup.Config;
import tv.mongotheelder.tnr.setup.Registration;
import tv.mongotheelder.tnr.wireless.AbstractWirelessRedstoneTile;

public class WirelessRedstoneReceiverTile  extends AbstractWirelessRedstoneTile implements ITickableTileEntity {
    private static final Logger LOGGER = LogManager.getLogger();

    private int tickCount = 0;

    public WirelessRedstoneReceiverTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public WirelessRedstoneReceiverTile() {
        this(Registration.WIRELESS_REDSTONE_RECEIVER_TILE.get());
    }

    @Override
    public void tick() {
        if (world != null && !world.isRemote) {
            if (tickCount > Config.WIRELESS_REDSTONE_RECEIVER_TICK.get()) {
                tickCount = 0;
                if (isValidSourceBlock(world, sourcePos)) {
                    if (world.getBlockState(sourcePos).canProvidePower()) {
                        boolean oldPowered = getPowered();
                        boolean isPowered = false;
                        for (Direction d : Direction.values()) {
                            isPowered |= world.getBlockState(sourcePos).getWeakPower(world, sourcePos, d) > 0;
                        }
                        if (isPowered != oldPowered) {
                            //LOGGER.debug("Updating redstone state at "+pos+" on "+world);
                            BlockState newState = world.getBlockState(pos).with(BlockStateProperties.POWERED, isPowered);
                            ((WirelessRedstoneReceiver) newState.getBlock()).updatePoweredState(world, newState, pos);
                        }
                    }
                }
            } else {
                tickCount++;
            }
        }
    }
}
