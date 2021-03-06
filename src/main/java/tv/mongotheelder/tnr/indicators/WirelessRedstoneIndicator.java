package tv.mongotheelder.tnr.indicators;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.mongotheelder.tnr.misc.SixWayFacingBlock;
import tv.mongotheelder.tnr.setup.Config;

import javax.annotation.Nullable;

public class WirelessRedstoneIndicator extends SixWayFacingBlock {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final float BASE_HARDNESS = 0.6f;

    private AxisAlignedBB SHAPE = new AxisAlignedBB(7, 7, 0, 9, 9, 0.25);

    public WirelessRedstoneIndicator(Block.Properties builder) {
        super(builder);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(BlockStateProperties.POWERED);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return rotatedShape(SHAPE, state.get(BlockStateProperties.FACE), state.get(BlockStateProperties.HORIZONTAL_FACING));
    }

    @Override
    @SuppressWarnings("deprecation")
    public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn, BlockPos pos) {
        return Config.WIRELESS_REDSTONE_INDICATOR_UNBREAKABLE.get() ? -1f : BASE_HARDNESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new WirelessRedstoneIndicatorTile();
    }
}
