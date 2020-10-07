package tv.mongotheelder.tnr.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import tv.mongotheelder.tnr.TotallyNotRedstone;

@Mod.EventBusSubscriber(modid = TotallyNotRedstone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Generator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        if( event.includeServer() )
            registerServerProviders(event.getGenerator());

        if( event.includeClient() )
            registerClientProviders(event.getGenerator(), event);
    }

    private static void registerServerProviders(DataGenerator generator) {
        generator.addProvider(new GeneratorLoot(generator));
        generator.addProvider(new GeneratorRecipes(generator));
    }

    private static void registerClientProviders(DataGenerator generator, GatherDataEvent event) {
        ExistingFileHelper helper = event.getExistingFileHelper();

        generator.addProvider(new GeneratorBlockModels(generator, helper));
        generator.addProvider(new GeneratorBlockStates(generator, helper));
        generator.addProvider(new GeneratorItemModels(generator, helper));
        //generator.addProvider(new GeneratorLanguage(generator));
    }
}
