package tv.mongotheelder.tnr.wireless;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.mongotheelder.tnr.TotallyNotRedstone;
import tv.mongotheelder.tnr.setup.Config;
import tv.mongotheelder.tnr.setup.Registration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WirelessRedstoneReceiverTile  extends TileEntity implements ITickableTileEntity {
    private static final Logger LOGGER = LogManager.getLogger();

    private BlockPos sourcePos;
    private int tickCount = 0;

    public WirelessRedstoneReceiverTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public WirelessRedstoneReceiverTile() {
        this(Registration.WIRELESS_REDSTONE_RECEIVER_TILE.get());
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        if (sourcePos != null) {
            tag.put(TotallyNotRedstone.WIRELESS_REDSTONE_RECEIVER_TAG, NBTUtil.writeBlockPos(sourcePos));
        }
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        // This is actually the default but placed here so you
        // know this is the place to potentially have a lighter read() that only
        // considers things needed client-side
        read(tag);
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        if (tag.contains(TotallyNotRedstone.WIRELESS_REDSTONE_RECEIVER_TAG)) {
            sourcePos = NBTUtil.readBlockPos(tag.getCompound(TotallyNotRedstone.WIRELESS_REDSTONE_RECEIVER_TAG));
        }
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT tag) {
        if (sourcePos != null) {
            tag.put(TotallyNotRedstone.WIRELESS_REDSTONE_RECEIVER_TAG, NBTUtil.writeBlockPos(sourcePos));
        }
        return super.write(tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getNbtCompound();
        handleUpdateTag(tag);
    }

    public void setSourcePos(BlockPos pos) {
        if (pos != null) {
            sourcePos = pos;
            markDirty();
        };
    }

    @Override
    public void tick() {
        if (world != null && !world.isRemote) {
            if (tickCount > Config.WIRELESS_REDSTONE_RECEIVER_TICK.get()) {
                tickCount = 0;
                if (isValidSourceBlock(world, sourcePos)) {
                    if (world.getBlockState(sourcePos).canProvidePower()) {
                        boolean oldPowered = getPowered();
                        boolean isPowered = false;
                        for (Direction d : Direction.values()) {
                            isPowered |= world.getBlockState(sourcePos).getWeakPower(world, sourcePos, d) > 0;
                        }
                        if (isPowered != oldPowered) {
                            //LOGGER.debug("Updating redstone state at "+pos+" on "+world);
                            BlockState newState = world.getBlockState(pos).with(BlockStateProperties.POWERED, isPowered);
                            ((WirelessRedstoneReceiver) newState.getBlock()).updatePoweredState(world, newState, pos);
                        }
                    }
                }
            } else {
                tickCount++;
            }
        }
    }

    private static boolean isRedstoneController(BlockState blockState) {
        return BlockTags.getCollection().getOrCreate(TotallyNotRedstone.REDSTONE_CONTROLLERS_TAG).contains(blockState.getBlock());
    }

    public static boolean isValidSourceBlock(World world, BlockPos pos) {
        return world != null && pos != null && world.isBlockPresent(pos) && isRedstoneController(world.getBlockState(pos));
    }

    private boolean getPowered() {
        return world != null && world.getBlockState(pos).get(BlockStateProperties.POWERED);
    }
}
