package tv.mongotheelder.tnr.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import tv.mongotheelder.tnr.misc.SolidColors;
import tv.mongotheelder.tnr.setup.Registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GeneratorLoot extends LootTableProvider {
    public GeneratorLoot(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(
                Pair.of(Blocks::new, LootParameterSets.BLOCK)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationresults) {
        map.forEach((name, table) -> LootTableManager.func_227508_a_(validationresults, name, table));
    }

    private static class Blocks extends BlockLootTables {
        @Override
        protected void addTables() {
            this.registerDropSelfLootTable(Registration.KEYPAD_BLOCK.get());
            this.registerDropSelfLootTable(Registration.SMALL_SQUARE_TIMED_BUTTON_BLOCK.get());
            this.registerDropSelfLootTable(Registration.MEDIUM_SQUARE_TIMED_BUTTON_BLOCK.get());
            this.registerDropSelfLootTable(Registration.LARGE_SQUARE_TIMED_BUTTON_BLOCK.get());
            this.registerDropSelfLootTable(Registration.SEQUENCER_BLOCK.get());
            this.registerDropSelfLootTable(Registration.WIRELESS_REDSTONE_RECEIVER_BLOCK.get());
            for (SolidColors color: SolidColors.values()) {
                String colorName = color.name().toLowerCase();
                this.registerDropSelfLootTable(Registration.WIRELESS_REDSTONE_INDICATOR_BLOCKS.get(colorName).get());
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            List<Block> knownBlocks = new ArrayList<>();
            knownBlocks.add(Registration.KEYPAD_BLOCK.get());
            knownBlocks.add(Registration.SMALL_SQUARE_TIMED_BUTTON_BLOCK.get());
            knownBlocks.add(Registration.MEDIUM_SQUARE_TIMED_BUTTON_BLOCK.get());
            knownBlocks.add(Registration.LARGE_SQUARE_TIMED_BUTTON_BLOCK.get());
            knownBlocks.add(Registration.SEQUENCER_BLOCK.get());
            knownBlocks.add(Registration.WIRELESS_REDSTONE_RECEIVER_BLOCK.get());
            for (SolidColors color: SolidColors.values()) {
                String colorName = color.name().toLowerCase();
                knownBlocks.add(Registration.WIRELESS_REDSTONE_INDICATOR_BLOCKS.get(colorName).get());
            }
            return knownBlocks;
        }
    }
}
