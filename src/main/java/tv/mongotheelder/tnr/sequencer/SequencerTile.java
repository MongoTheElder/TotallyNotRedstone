package tv.mongotheelder.tnr.sequencer;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.mongotheelder.tnr.TotallyNotRedstone;
import tv.mongotheelder.tnr.networking.PacketHandler;
import tv.mongotheelder.tnr.setup.Registration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class SequencerTile extends TileEntity implements ITickableTileEntity {
    private SequencerConfig config = new SequencerConfig();
    private final StateMachines stateMachines = new StateMachines();
    private boolean[] states = new boolean[SequencerConfig.SEQUENCE_COUNT];
    private static final Map<Direction, Map<Direction, String>> faceRotation = new HashMap<>();
    private static final Logger LOGGER = LogManager.getLogger();

    public SequencerTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public SequencerTile() {
        this(Registration.SEQUENCER_TILE.get());
    }

    private Direction getFacing() {
        BlockState blockState = world.getBlockState(pos);
        return blockState.get(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;
        BlockState blockState = world.getBlockState(pos);
        Direction facing = getFacing();
        int power = world.getBlockState(pos.offset(facing)).getWeakPower(world, pos.offset(facing.getOpposite()), facing);
        stateMachines.doTick(power);
        if (stateMachines.stateChanged()) {
            states = stateMachines.getRedstoneStates();
            notifyNeighbors();
        }
    }

    private void notifyNeighbors() {
       SequencerBlock seq = (SequencerBlock) world.getBlockState(pos).getBlock();
       seq.notifyNeighborsOfStateExcept(world, pos);
    }

    public int getRedstonePower(Direction side) {
        Direction facing = getFacing();
        if (side == facing.getOpposite()) return 0; // Don't emit redstone signal to the trigger face

        String face = transformFace(side.getOpposite(), facing);
        int index = config.getSideColorIndex(face);
        if (index == 0) return 0;
        boolean level = states[index-1];
        return level ? 15 : 0;
    }

    private String transformFace(Direction side, Direction facing) {
        String[][] sides = {{"trigger", "left", "right", "back"}, {"right", "trigger", "left", "back"}, {"back", "right", "trigger", "left"}, {"left", "back", "right", "trigger"}};
        switch (side) {
            case UP:
                return "top";
            case DOWN:
                return "bottom";
            default: // TODO: Fix this!
                return sides[facing.getHorizontalIndex()][side.getHorizontalIndex()];
        }
    }

    public void setConfig(SequencerConfig config) {
        this.config = config;
        if (world != null && world.isRemote) {
            PacketHandler.sendSequencerNBT(this, config.saveConfigToNBT());
        }
        markDirty();
    }

    public void setConfig(CompoundNBT config) {
        this.config.loadConfigFromNBT(config);

        if (world != null && world.isRemote) {
            PacketHandler.sendSequencerNBT(this, config);
        } else {
            updateStateMachines();
        }
        markDirty();
    }

    public SequencerConfig getConfig() {
        return config;
    }

    @Override
    public void read(CompoundNBT tag) {

        super.read(tag);
        if (tag.contains(TotallyNotRedstone.SEQUENCER_CONFIG_TAG)) {
            config.loadConfigFromNBT(tag.getCompound(TotallyNotRedstone.SEQUENCER_CONFIG_TAG));
        }
        if (world != null && !world.isRemote) {
            updateStateMachines();
        }
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put(TotallyNotRedstone.SEQUENCER_CONFIG_TAG, config.saveConfigToNBT());
        return super.write(tag);
    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
    }

    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    private void updateStateMachines() {
        stateMachines.setMachineConfig(config);
    }

}
