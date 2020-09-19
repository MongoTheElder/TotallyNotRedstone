package tv.mongotheelder.tnr.sequencer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class SequencerBlock extends Block {
    public SequencerBlock(Properties properties) {
        super(properties);
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (!world.isRemote) {
            if (world.getTileEntity(pos) instanceof SequencerTile) {
                SequencerTile te = (SequencerTile) world.getTileEntity(pos);
                if (te != null) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, te, te.getPos());
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

}
