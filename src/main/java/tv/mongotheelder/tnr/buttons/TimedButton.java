package tv.mongotheelder.tnr.buttons;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import tv.mongotheelder.tnr.misc.SixWayFacingBlock;
import tv.mongotheelder.tnr.networking.PacketHandler;
import tv.mongotheelder.tnr.setup.Config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TimedButton extends SixWayFacingBlock {
    public static final EnumProperty<TimedButtonStates> STATE = EnumProperty.create("state", TimedButtonStates.class);

    public TimedButton(Properties properties) {
        super(properties);
        setDefaultState(this.stateContainer.getBaseState()
                .with(STATE, TimedButtonStates.OFF)
                .with(BlockStateProperties.POWERED, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(STATE, BlockStateProperties.POWERED);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TimedButtonTile();
    }

    private boolean shouldOpenGui(PlayerEntity player, Hand hand) {
        return player.isCreative() || (player.isCrouching() && Config.TIMED_BUTTONS_SETTABLE_WITH_SHIFT_CLICK.get());
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (shouldOpenGui(player, hand)) {
            if (!world.isRemote) {
                if (world.getTileEntity(pos) instanceof TimedButtonTile) {
                    PacketHandler.sendTimedButtonOpenGUI(pos, (ServerPlayerEntity) player);
                }
            }
        } else {
            switch (state.get(STATE)) {
                case OFF:
                    world.setBlockState(pos, state.with(STATE, TimedButtonStates.RUNNING));
                    break;
                case RUNNING:
                case ON:
                    world.setBlockState(pos, state.with(STATE, TimedButtonStates.OFF));
                    break;
            }
        }
        return ActionResultType.SUCCESS;
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
        return side == SixWayFacingBlock.getFacing(blockState) ? blockState.get(BlockStateProperties.POWERED) ? 15 : 0 : 0;
    }

}
