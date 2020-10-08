package tv.mongotheelder.tnr.wireless;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.mongotheelder.tnr.TotallyNotRedstone;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractWirelessRedstoneTile extends TileEntity {
    protected BlockPos sourcePos;
    protected Direction face;
    private static final Logger LOGGER = LogManager.getLogger();

    public AbstractWirelessRedstoneTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public void setSourcePos(BlockPos sourcePos) {
        if (sourcePos != null) {
            this.sourcePos = sourcePos;
            markDirty();
        } else {
            LOGGER.error("Attempted to set a null source block position");
        }
    }

    public void setFace(Direction face) {
        if (face != null) {
            this.face = face;
            markDirty();
        } else {
            LOGGER.error("Attempted to set a null source face");
        }
    }

    protected static boolean isRedstoneController(BlockState blockState) {
        ITag<Block> tag = BlockTags.getCollection().get(TotallyNotRedstone.REDSTONE_CONTROLLERS_TAG);
        return tag != null && tag.contains(blockState.getBlock());
    }

    public static boolean isValidSourceBlock(World world, BlockPos pos) {
        return world != null && pos != null && world.isBlockPresent(pos) && isRedstoneController(world.getBlockState(pos));
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        if (sourcePos != null) {
            tag.put(TotallyNotRedstone.WIRELESS_REDSTONE_SOURCE_POS_TAG, NBTUtil.writeBlockPos(sourcePos));
            tag.putString(TotallyNotRedstone.WIRELESS_REDSTONE_FACE_TAG, face.getName2());
        }
        return tag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        read(state, tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getNbtCompound();
        if (world == null) return;
        BlockState state = world.getBlockState(pos);
        handleUpdateTag(state, tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        if (tag.contains(TotallyNotRedstone.WIRELESS_REDSTONE_SOURCE_POS_TAG)) {
            sourcePos = NBTUtil.readBlockPos(tag.getCompound(TotallyNotRedstone.WIRELESS_REDSTONE_SOURCE_POS_TAG));
            face = Direction.byName(tag.getString(TotallyNotRedstone.WIRELESS_REDSTONE_FACE_TAG));
        }
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT tag) {
        if (sourcePos != null) {
            tag.put(TotallyNotRedstone.WIRELESS_REDSTONE_SOURCE_POS_TAG, NBTUtil.writeBlockPos(sourcePos));
        }
        if (face != null) {
            tag.putString(TotallyNotRedstone.WIRELESS_REDSTONE_FACE_TAG, face.getName2());
        }
        return super.write(tag);
    }

    protected boolean getPowered() {
        return world != null && world.getBlockState(pos).get(BlockStateProperties.POWERED);
    }
}
