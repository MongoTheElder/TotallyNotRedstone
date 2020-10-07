package tv.mongotheelder.tnr.keypad;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import tv.mongotheelder.tnr.TotallyNotRedstone;
import tv.mongotheelder.tnr.networking.PacketHandler;
import tv.mongotheelder.tnr.setup.Config;
import tv.mongotheelder.tnr.setup.Registration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class KeypadTile extends TileEntity implements INamedContainerProvider, IKeypad {
    private String code = "";

    public KeypadTile(TileEntityType<?> tileEntityTypeIn) { super(tileEntityTypeIn); }

    public KeypadTile() {
        this(Registration.KEYPAD_TILE.get());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if (world == null) return;
        this.code = code;
        if (world.isRemote) {
            // On the client, send message to update the tile on the server and lock the keypad
            PacketHandler.sendKeypadSetCode(world, pos, code);
        } else {
            // Lock the keypad on the server
            setUnlockState(false);
        }
    }

    public boolean showCode() {
        return Config.KEYPAD_SHOWS_CODE_WHEN_UNLOCKED.get();
    }

    public boolean getUnlockState() {
        if (world == null || !world.isBlockPresent(pos)) return false;
        return world.getBlockState(pos).get(BlockStateProperties.POWERED);
    }

    public void setUnlockState(boolean state) {
        if (world == null || !world.isBlockPresent(pos)) return;
        BlockState oldState = world.getBlockState(pos);
        world.setBlockState(pos, oldState.with(BlockStateProperties.POWERED, state), 3);
        if (world.isRemote) {
            PacketHandler.sendKeypadSetState(world, pos, state);
        }
    }

    public void toggleLock() {
        if (world == null || !world.isBlockPresent(pos)) return;
        setUnlockState(!world.getBlockState(pos).get(BlockStateProperties.POWERED));
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
         return new KeypadContainer(id, world, pos);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("item.tnr.keypad");
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        if (!code.isEmpty()) {
            tag.putString(TotallyNotRedstone.KEYPAD_CODE_TAG, code);
        }
        return tag;
    }

    public void errorSound() {
        world.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, Registration.KEYPAD_ERROR_SOUND.get(), SoundCategory.BLOCKS, 0.5F, 1.0F, false);
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        read(state, tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        if (tag.contains(TotallyNotRedstone.KEYPAD_CODE_TAG)) {
            code = tag.get(TotallyNotRedstone.KEYPAD_CODE_TAG).getString();
        }
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT tag) {
        if (!code.isEmpty()) {
            tag.putString(TotallyNotRedstone.KEYPAD_CODE_TAG, code);
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
        if (world == null) return;
        BlockState state = world.getBlockState(pos);
        handleUpdateTag(state, tag);
    }

}
