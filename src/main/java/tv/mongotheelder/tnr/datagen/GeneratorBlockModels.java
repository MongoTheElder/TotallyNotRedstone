package tv.mongotheelder.tnr.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import tv.mongotheelder.tnr.TotallyNotRedstone;
import tv.mongotheelder.tnr.misc.SolidColors;

public class GeneratorBlockModels extends BlockModelProvider {

    public GeneratorBlockModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, TotallyNotRedstone.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerColorizedModels("wireless_redstone_indicator");
    }

    private void registerColorizedModels(String baseName) {
        for (SolidColors color: SolidColors.values()) {
            String colorName = color.name().toLowerCase();
            String modelName = "tnr:block/"+baseName+"_"+colorName;
            withExistingParent(modelName+"_on", "tnr:block/"+baseName).texture("indicator", "tnr:block/"+color.getPrimary());
            withExistingParent(modelName+"_off", "tnr:block/"+baseName).texture("indicator", "tnr:block/"+color.getSecondary());
        }
    }
}
