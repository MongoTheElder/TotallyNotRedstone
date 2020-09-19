package tv.mongotheelder.tnr.setup;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import tv.mongotheelder.tnr.TotallyNotRedstone;
import tv.mongotheelder.tnr.networking.PacketHandler;

public class ModSetup {
    public static final ItemGroup ITEM_GROUP = new ItemGroup(TotallyNotRedstone.MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Registration.SEQUENCER_BLOCK.get());
        }
    };

    public static void init(final FMLCommonSetupEvent event) {
        PacketHandler.register();
    }

}
