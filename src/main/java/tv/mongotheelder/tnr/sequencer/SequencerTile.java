package tv.mongotheelder.tnr.sequencer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import tv.mongotheelder.tnr.TotallyNotRedstone;
import tv.mongotheelder.tnr.setup.Registration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SequencerTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    private SequencerConfig config;

    public SequencerTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public SequencerTile() {
        this(Registration.SEQUENCER_TILE.get());
    }

    @Override
    public void tick() {

    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return null;
    }

    public void setConfig(SequencerConfig config) {
        this.config = config;
    }

    public void setConfig(CompoundNBT config) {
        this.config.loadConfigFromNBT(config);
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
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put(TotallyNotRedstone.SEQUENCER_CONFIG_TAG, config.saveConfigToNBT());
        return super.write(tag);
    }

}
