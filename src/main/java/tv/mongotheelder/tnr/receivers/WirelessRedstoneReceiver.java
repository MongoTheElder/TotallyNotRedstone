package tv.mongotheelder.tnr.receivers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.mongotheelder.tnr.misc.SixWayFacingBlock;
import tv.mongotheelder.tnr.setup.Config;

import javax.annotation.Nullable;

public class WirelessRedstoneReceiver extends SixWayFacingBlock {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final float BASE_HARDNESS = 1.2f;

    private AxisAlignedBB SHAPE = new AxisAlignedBB(5, 5, 0, 11, 11, 2);

    public WirelessRedstoneReceiver(Block.Properties builder) {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(BlockStateProperties.POWERED, false));
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
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new WirelessRedstoneReceiverTile();
    }

    @Deprecated
    @SuppressWarnings("deprecation")
    public float getBlockHardness(BlockState blockState, IBlockReader worldIn, BlockPos pos) {
        return Config.WIRELESS_REDSTONE_RECEIVER_UNBREAKABLE.get() ? -1f : BASE_HARDNESS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canProvidePower(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        //LOGGER.debug("Weak Redstone? "+getDirection(blockState));
        return blockState.get(BlockStateProperties.POWERED) ? 15 : 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        //LOGGER.debug("Strong Redstone? "+getDirection(blockState)+" = "+side);
        return side == getDirection(blockState) ? blockState.get(BlockStateProperties.POWERED) ? 15 : 0 : 0;
    }

    public void updatePoweredState(World world, BlockState blockState, BlockPos pos) {
        if (!world.isRemote) {
            world.setBlockState(pos, blockState, 3);

            Direction facing = getDirection(blockState).getOpposite();
            BlockPos facingBlock = pos.offset(facing);
            //LOGGER.debug("Updating redstone state for block at: "+pos+" looking at "+facingBlock);
            world.notifyNeighborsOfStateChange(pos, this);
            world.notifyNeighborsOfStateChange(facingBlock, this);
        }
    }

    private Direction getDirection(BlockState blockState) {
        return SixWayFacingBlock.getFacing(blockState);
    }
}
