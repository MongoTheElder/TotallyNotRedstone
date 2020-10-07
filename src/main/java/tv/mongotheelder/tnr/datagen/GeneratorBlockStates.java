package tv.mongotheelder.tnr.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.mongotheelder.tnr.TotallyNotRedstone;
import tv.mongotheelder.tnr.misc.SixWayFacingBlock;
import tv.mongotheelder.tnr.misc.SolidColors;
import tv.mongotheelder.tnr.setup.Registration;

public class GeneratorBlockStates extends BlockStateProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private ExistingFileHelper helper;

    protected GeneratorBlockStates(DataGenerator gen, ExistingFileHelper helper) {
        super(gen, TotallyNotRedstone.MODID, helper);
        this.helper = helper;
    }

    @Override
    protected void registerStatesAndModels() {
        generateWirelessRedstoneIndicators("wireless_redstone_indicator");
        generateWirelessRedstoneReceiver("wireless_redstone_receiver");
        generateSequencer("redstone_sequencer");
        generateKeypad("keypad");
        generateTimedButtons("timed_buttons");
    }

    private void generateWirelessRedstoneIndicators(String baseName) {
        for (SolidColors color: SolidColors.values()) {
            String colorName = color.name().toLowerCase();
            ModelFile onModel = new ModelFile.UncheckedModelFile(modLoc("block/"+baseName+"_"+colorName+"_on"));
            ModelFile offModel = new ModelFile.UncheckedModelFile(modLoc("block/"+baseName+"_"+colorName+"_off"));
            registerSixWayFacingPoweredBlock(Registration.WIRELESS_REDSTONE_INDICATOR_BLOCKS.get(colorName).get(), onModel, offModel);
        }
    }

    private void generateWirelessRedstoneReceiver(String baseName) {
        ModelFile onModel = new ModelFile.ExistingModelFile(modLoc("block/"+baseName+"_on"), helper);
        ModelFile offModel = new ModelFile.ExistingModelFile(modLoc("block/"+baseName+"_off"), helper);
        registerSixWayFacingPoweredBlock(Registration.WIRELESS_REDSTONE_RECEIVER_BLOCK.get(), onModel, offModel);
    }

    private void generateSequencer(String baseName) {
        horizontalBlock(Registration.SEQUENCER_BLOCK.get(), new ModelFile.ExistingModelFile(modLoc("block/sequencer"), helper));
    }

    private void generateKeypad(String baseName) {
        horizontalBlock(Registration.KEYPAD_BLOCK.get(), new ModelFile.ExistingModelFile(modLoc("block/keypad"), helper));
    }

    private void generateTimedButtons(String baseName) {

    }

    private void registerSixWayFacingBlock(SixWayFacingBlock block, ModelFile model) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    AttachFace face = state.get(BlockStateProperties.FACE);
                    Direction dir = state.get(BlockStateProperties.HORIZONTAL_FACING);
                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationX(getRotationX(face))
                            .rotationY((int) dir.getHorizontalAngle())
                            .build();
                });
    }

    private static int getRotationX(AttachFace face) {
        switch (face) {
            case CEILING:
                return 270;
            case FLOOR:
                return 90;
            default:
                return 0;
        }
    }

    private void registerSixWayFacingPoweredBlock(SixWayFacingBlock block, ModelFile onModel, ModelFile offModel) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    AttachFace face = state.get(BlockStateProperties.FACE);
                    Direction dir = state.get(BlockStateProperties.HORIZONTAL_FACING);
                    boolean powered = state.get(BlockStateProperties.POWERED);
                    return ConfiguredModel.builder()
                            .modelFile(powered ? onModel : offModel)
                            .rotationX(getRotationX(face))
                            .rotationY((int) dir.getHorizontalAngle())
                            .build();
                });
    }

    private void registerHorizontalFacingBlock() {

    }

    private void registerTimedButton() {

    }
}
