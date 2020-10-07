package tv.mongotheelder.tnr.buttons;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tv.mongotheelder.tnr.TotallyNotRedstone;
import tv.mongotheelder.tnr.networking.PacketHandler;
import tv.mongotheelder.tnr.setup.Config;
import tv.mongotheelder.tnr.setup.Registration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TimedButtonTile extends TileEntity implements ITickableTileEntity {
    private static final String PULSE_COUNT_KEY = "pulse_count";
    private static final String ENABLE_SOUND_KEY = "enable_sound";

    private int timerCount = 0;
    private int phase = 0;
    private int pulseCount;
    private final int pulseDuration;
    private final double pulseRatio;
    private boolean enableSound;

    public TimedButtonTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        pulseCount = Config.TIMED_BUTTONS_PULSE_COUNT.get();
        pulseDuration = Config.TIMED_BUTTONS_PULSE_DURATION.get();
        pulseRatio = Config.TIMED_BUTTONS_PULSE_RATIO.get();
        enableSound = Config.TIMED_BUTTONS_ENABLE_SOUND.get();
    }

    public TimedButtonTile() {
        this(Registration.TIMED_BUTTON_TILE.get());
    }

    public int getPulseCount() {
        return pulseCount;
    }

    public boolean getEnableSound() {
        return enableSound;
    }

    public void shortBeepSound() {
        if (enableSound) {
            world.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, Registration.TIMED_BUTTON_BEEP_SOUND.get(), SoundCategory.BLOCKS, 0.5F, 1.0F, false);
        }
    }

    public void longBeepSound() {
        if (enableSound) {
            world.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, Registration.TIMED_BUTTON_LONG_BEEP_SOUND.get(), SoundCategory.BLOCKS, 0.5F, 1.0F, false);
        }
    }

    @Override
    public void tick() {
        BlockState blockState = world.getBlockState(pos);
        TimedButtonStates state = blockState.get(TimedButton.STATE);
        if (state == TimedButtonStates.RUNNING) {
            if (phase < pulseCount) {
                if (timerCount < pulseDuration) {
                    boolean inLit = timerCount < pulseDuration * pulseRatio;
                    if (blockState.get(TimedButton.INDICATOR) != inLit) {
                        world.setBlockState(pos, blockState.with(TimedButton.INDICATOR, inLit));
                        // only beep on the off/on transitions
                        if (timerCount == 0) shortBeepSound();
                    }
                    timerCount++;
                } else {
                    timerCount = 0;
                    phase++;
                }
            } else {
                timerCount = 0;
                phase = 0;
                world.setBlockState(pos, blockState.with(TimedButton.STATE, TimedButtonStates.ON).with(TimedButton.INDICATOR, false));
                longBeepSound();
                markDirty();
            }
        } else {
            blockState = blockState.with(BlockStateProperties.POWERED, state == TimedButtonStates.ON);
            world.setBlockState(pos, blockState);
            ((TimedButton)blockState.getBlock()).updateNeighbors(blockState, world, pos);
        }
    }

    public void setConfig(int pulseCount, boolean enableSound) {
        setConfig(encodeNBT(pulseCount, enableSound));
    }

    public void setConfig(CompoundNBT config) {
        pulseCount = config.getInt(PULSE_COUNT_KEY);
        enableSound = config.getBoolean(ENABLE_SOUND_KEY);
        if (world != null && world.isRemote) {
            PacketHandler.sendTimedButtonNBT(this, config);
        }
        markDirty();
    }

    private CompoundNBT encodeNBT(int pulseCount, boolean enableSound) {
        CompoundNBT buttonConfig = new CompoundNBT();
        buttonConfig.putInt(PULSE_COUNT_KEY, pulseCount);
        buttonConfig.putBoolean(ENABLE_SOUND_KEY, enableSound);
        return buttonConfig;
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        if (tag.contains(TotallyNotRedstone.TIMED_BUTTONS_TAG)) {
            CompoundNBT buttonConfig = tag.getCompound(TotallyNotRedstone.TIMED_BUTTONS_TAG);
            pulseCount = buttonConfig.getInt(PULSE_COUNT_KEY);
            enableSound = buttonConfig.getBoolean(ENABLE_SOUND_KEY);
        }
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put(TotallyNotRedstone.TIMED_BUTTONS_TAG, encodeNBT(pulseCount, enableSound));
        return super.write(tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
        CompoundNBT tag = pkt.getNbtCompound();
        if (world == null) return;
        BlockState state = world.getBlockState(pos);
        read(state, tag);
    }
}
