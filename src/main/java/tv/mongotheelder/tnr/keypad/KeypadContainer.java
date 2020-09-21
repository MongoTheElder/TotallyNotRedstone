package tv.mongotheelder.tnr.keypad;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tv.mongotheelder.tnr.setup.Registration;

public class KeypadContainer extends Container {
    private final TileEntity tileEntity;


    public KeypadContainer(int windowId, World world, BlockPos pos) {
        super(Registration.KEYPAD_CONTAINER.get(), windowId);
        tileEntity = world.getTileEntity(pos);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public String getCode() {
        if (tileEntity == null) return null;
        return ((IKeypad)tileEntity).getCode();
    }

    public void setCode(String code) {
        if (tileEntity == null) return;
        ((IKeypad)tileEntity).setCode(code);
    }

    public void setLockState(boolean state) {
        if (tileEntity == null) return;
        ((IKeypad)tileEntity).setUnlockState(state);
    }

    public void errorSound() {
        if (tileEntity == null) return;
        ((IKeypad)tileEntity).errorSound();
    }

    public void toggleLock() {
        if (tileEntity == null) return;
        ((IKeypad)tileEntity).toggleLock();

    }

    public boolean getUnlockState() {
        if (tileEntity == null) return true;
        return ((IKeypad)tileEntity).getUnlockState();

    }

    public boolean showCode() {
        return ((IKeypad)tileEntity).showCode();
    }

}
