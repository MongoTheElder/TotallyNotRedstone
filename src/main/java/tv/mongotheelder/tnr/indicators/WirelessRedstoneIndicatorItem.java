package tv.mongotheelder.tnr.indicators;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.mongotheelder.tnr.receivers.WirelessRedstoneReceiverTile;
import tv.mongotheelder.tnr.wireless.AbstractWirelessRedstoneItem;

public class WirelessRedstoneIndicatorItem extends AbstractWirelessRedstoneItem {
    private static final Logger LOGGER = LogManager.getLogger();

    public WirelessRedstoneIndicatorItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    public boolean isValidSourceBlock(World world, BlockPos pos) {
        return WirelessRedstoneReceiverTile.isValidSourceBlock(world, pos);
    }
}
