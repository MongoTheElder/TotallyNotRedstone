package tv.mongotheelder.tnr.indicators;

import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import tv.mongotheelder.tnr.misc.BlockFlags;
import tv.mongotheelder.tnr.setup.Config;
import tv.mongotheelder.tnr.setup.Registration;
import tv.mongotheelder.tnr.wireless.AbstractWirelessRedstoneTile;

public class WirelessRedstoneIndicatorTile  extends AbstractWirelessRedstoneTile implements ITickableTileEntity {
    private int tickCount;

    public WirelessRedstoneIndicatorTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public WirelessRedstoneIndicatorTile() {
        this(Registration.WIRELESS_REDSTONE_INDICATOR_TILE.get());
    }

    @Override
    public void tick() {
        if (world != null && !world.isRemote) {
            if (tickCount > Config.WIRELESS_REDSTONE_INDICATOR_TICK.get()) {
                tickCount = 0;
                if (isValidSourceBlock(world, sourcePos)) {
                    if (world.getBlockState(sourcePos).canProvidePower()) {
                        boolean oldPowered = getPowered();
                        boolean isPowered = world.getBlockState(sourcePos).getWeakPower(world, sourcePos, face.getOpposite()) > 0;
                        if (isPowered != oldPowered) {
                            //LOGGER.debug("Updating redstone state at "+pos+" on "+world);
                            BlockState newState = world.getBlockState(pos).with(BlockStateProperties.POWERED, isPowered);
                            world.setBlockState(pos, newState, BlockFlags.STATIC_UPDATE);
                        }
                    }
                }
            } else {
                tickCount++;
            }
        }
    }
}
