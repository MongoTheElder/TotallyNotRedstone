package tv.mongotheelder.tnr.buttons;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;

public class SmallTimedButton extends TimedButton {
    private static final AxisAlignedBB SHAPE = new AxisAlignedBB(6, 6, 0, 10, 10, 2);

    public SmallTimedButton(Block.Properties properties) {
        super(properties);
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return rotatedShape(SHAPE, state.get(BlockStateProperties.FACE), state.get(BlockStateProperties.HORIZONTAL_FACING));
    }

}
