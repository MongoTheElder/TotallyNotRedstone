package tv.mongotheelder.tnr.keypad;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.StateContainer;
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
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.mongotheelder.tnr.misc.HorizontalFacingBlock;
import tv.mongotheelder.tnr.setup.Config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Keypad extends HorizontalFacingBlock {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final float BASE_HARDNESS = 1.2f;

    private static final AxisAlignedBB SHAPE = new AxisAlignedBB(6.5D, 5.5D, 0.0D, 10.5D, 10.5D, 0.5D);

    public Keypad(Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(BlockStateProperties.POWERED, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn, BlockPos pos) {
        return Config.KEYPAD_UNBREAKABLE.get() ? -1f : BASE_HARDNESS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return rotatedShape(SHAPE, state.get(BlockStateProperties.HORIZONTAL_FACING));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(BlockStateProperties.POWERED);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new KeypadTile();
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (!world.isRemote) {
             if (world.getTileEntity(pos) instanceof KeypadTile) {
                KeypadTile te = (KeypadTile) world.getTileEntity(pos);
                if (te != null) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, te, te.getPos());
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canProvidePower(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return blockState.get(BlockStateProperties.POWERED) ? 15 : 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return side == getDirection(blockState) ? blockState.get(BlockStateProperties.POWERED) ? 15 : 0 : 0;
    }

    private Direction getDirection(BlockState blockState) {
        return HorizontalFacingBlock.getFacing(blockState);
    }
}
