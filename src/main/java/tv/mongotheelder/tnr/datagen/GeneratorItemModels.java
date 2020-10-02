package tv.mongotheelder.tnr.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import tv.mongotheelder.tnr.TotallyNotRedstone;
import tv.mongotheelder.tnr.misc.SolidColors;
import tv.mongotheelder.tnr.setup.Registration;

public class GeneratorItemModels  extends ItemModelProvider {
    public GeneratorItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, TotallyNotRedstone.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerRedstoneIndicators();
        withExistingParent(Registration.WIRELESS_REDSTONE_RECEIVER_ITEM.getId().getPath(), "tnr:block/wireless_redstone_receiver_on");
        withExistingParent(Registration.KEYPAD_ITEM.getId().getPath(), "tnr:block/keypad");
        registerTimedButton(Registration.LARGE_SQUARE_TIMED_BUTTON_ITEM.getId().getPath(), "tnr:block/large_timed_button_on");
        registerTimedButton(Registration.MEDIUM_SQUARE_TIMED_BUTTON_ITEM.getId().getPath(), "tnr:block/medium_timed_button_on");
        registerTimedButton(Registration.SMALL_SQUARE_TIMED_BUTTON_ITEM.getId().getPath(), "tnr:block/small_timed_button_on");
    }

    private void registerTimedButton(String path, String parent) {
        withExistingParent(path, parent)
                .transforms()
                    .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT)
                        .rotation(-90, 0, 0)
                        .translation(0, 1, -3)
                        .scale(0.55f, 0.55f, 0.55f)
                    .end()
                    .transform(ModelBuilder.Perspective.THIRDPERSON_LEFT)
                        .rotation(0, -135, 25)
                        .translation(0, 4, 2)
                        .scale(1.7f, 1.7f, 1.7f)
                    .end()
                    .transform(ModelBuilder.Perspective.FIRSTPERSON_LEFT)
                        .rotation(0, -135, 25)
                        .translation(0, 4, 2)
                        .scale(1.7f, 1.7f, 1.7f)
                    .end()
                    .transform(ModelBuilder.Perspective.GUI)
                        .rotation(75, 0, 0)
                        .translation(0, 2, 0)
                        .scale(1f, 1f, 1f)
                    .end()
                .end();
    }

    private void registerRedstoneIndicators() {
        for (SolidColors color: SolidColors.values()) {
            String colorName = color.name().toLowerCase();
            withExistingParent(Registration.WIRELESS_REDSTONE_INDICATOR_ITEMS.get(colorName).getId().getPath(), "tnr:block/wireless_redstone_indicator_"+colorName+"_on");
        }
    }
}
