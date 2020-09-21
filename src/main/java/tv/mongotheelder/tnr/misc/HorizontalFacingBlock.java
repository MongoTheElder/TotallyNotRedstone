package tv.mongotheelder.tnr.misc;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.shapes.VoxelShape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class HorizontalFacingBlock extends Block {
    private static final Logger LOGGER = LogManager.getLogger();

    public HorizontalFacingBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction.getAxis().isHorizontal()) {
                BlockState blockstate = this.getDefaultState();
                blockstate = blockstate.with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
                if (blockstate.isValidPosition(context.getWorld(), context.getPos())) {
                    return blockstate;
                }
            }
        }
        return null;
    }

    private static double flip(double d) {
        return 16.0D - d;
    }

    /**
     * rotatedShape returns the base shape rotated by the attached face and player look angle. The base shape is defined as AttachedFace: WALL and Direction: NORTH
     *
     * @param x         X offset of the bounding box
     * @param y         Y offset of the bounding box
     * @param z         Z offset of the bounding box
     * @param width     Width of the bounding box (X)
     * @param height    Height of the bounding box (Y)
     * @param depth     Depth of the bounding box (Z)
     * @param direction Look direction
     * @return Rotated shape
     */
    public static VoxelShape rotatedShape(double x, double y, double z, double width, double height, double depth, Direction direction) {
        double x2 = x + width;
        double y2 = y + height;
        double z2 = z + depth;

        switch (direction) {
            case SOUTH:
            default:
                return Block.makeCuboidShape(x, y, z, x2, y2, z2);
            case WEST:
                return Block.makeCuboidShape(flip(z), y, x, flip(z2), y2, x2);
            case NORTH:
                return Block.makeCuboidShape(flip(x), y, flip(z), flip(x2), y2, flip(z2));
            case EAST:
                return Block.makeCuboidShape(z, y, flip(x), z2, y2, flip(x2));
        }
    }

    public static VoxelShape rotatedShape(AxisAlignedBB base, Direction direction) {
        return rotatedShape(base.minX, base.minY, base.minZ, base.getXSize(), base.getYSize(), base.getZSize(), direction);
    }

    public static Direction getFacing(BlockState blockState) {
        if (blockState == null) return Direction.SOUTH;
        return blockState.get(BlockStateProperties.HORIZONTAL_FACING);
    }
}
