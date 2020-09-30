package tv.mongotheelder.tnr.sequencer;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import tv.mongotheelder.tnr.misc.HorizontalFacingBlock;
import tv.mongotheelder.tnr.networking.PacketHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SequencerBlock extends HorizontalFacingBlock {
    private static final AxisAlignedBB SHAPE = new AxisAlignedBB(0, 0, 0, 16, 16, 16);

    public SequencerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SequencerTile();
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (!world.isRemote) {
            if (world.getTileEntity(pos) instanceof SequencerTile) {
                PacketHandler.sendSequencerOpenGUI(pos, (ServerPlayerEntity) player);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return rotatedShape(SHAPE, state.get(BlockStateProperties.HORIZONTAL_FACING));
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canProvidePower(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        TileEntity te = blockAccess.getTileEntity(pos);
        if (te instanceof SequencerTile) {
            return ((SequencerTile)te).getRedstonePower(side);
        }
        return 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return getWeakPower(blockState, blockAccess, pos, side) > 0 ? 15: 0;
    }

    public void notifyNeighborsOfStateExcept(World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        world.notifyNeighborsOfStateExcept(pos, this, blockState.get(BlockStateProperties.HORIZONTAL_FACING));
    }
}
