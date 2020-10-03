package tv.mongotheelder.tnr.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;
import tv.mongotheelder.tnr.misc.SolidColors;
import tv.mongotheelder.tnr.setup.Registration;

import java.util.function.Consumer;

public class GeneratorRecipes extends RecipeProvider {
    public GeneratorRecipes(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(Registration.WIRELESS_REDSTONE_RECEIVER_ITEM.get())
                .key('r', Items.REDSTONE_LAMP)
                .key('s', Items.STONE_SLAB)
                .key('p', Tags.Items.ENDER_PEARLS)
                .patternLine(" r ")
                .patternLine(" p ")
                .patternLine("sss")
                .addCriterion("has_redstone_lamp", hasItem(Items.REDSTONE_LAMP))
                .addCriterion("has_ender_pearl", hasItem(Tags.Items.ENDER_PEARLS))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(Registration.SEQUENCER_ITEM.get())
                .key('r', Tags.Items.DUSTS_REDSTONE)
                .key('c', Items.CLOCK)
                .patternLine(" r ")
                .patternLine("rcr")
                .patternLine(" rr")
                .addCriterion("has_redstone_dust", hasItem(Tags.Items.DUSTS_REDSTONE))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(Registration.LARGE_SQUARE_TIMED_BUTTON_ITEM.get(), 4)
                .key('b', Items.STONE_BUTTON)
                .key('c', Items.CLOCK)
                .key('s', Items.STONE_SLAB)
                .patternLine(" c ")
                .patternLine("bbb")
                .patternLine("sss")
                .addCriterion("has_stone", hasItem(Tags.Items.STONE))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(Registration.MEDIUM_SQUARE_TIMED_BUTTON_ITEM.get(), 4)
                .key('b', Items.STONE_BUTTON)
                .key('c', Items.CLOCK)
                .key('s', Items.STONE_SLAB)
                .patternLine(" c ")
                .patternLine("bbb")
                .patternLine("s s")
                .addCriterion("has_stone", hasItem(Tags.Items.STONE))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(Registration.SMALL_SQUARE_TIMED_BUTTON_ITEM.get(), 4)
                .key('b', Items.STONE_BUTTON)
                .key('c', Items.CLOCK)
                .key('s', Items.STONE_SLAB)
                .patternLine(" c ")
                .patternLine("bbb")
                .patternLine(" s ")
                .addCriterion("has_stone", hasItem(Tags.Items.STONE))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(Registration.KEYPAD_ITEM.get())
                .key('b', Items.STONE_BUTTON)
                .key('r', Tags.Items.DUSTS_REDSTONE)
                .patternLine("bbb")
                .patternLine("brb")
                .patternLine("bbb")
                .addCriterion("has_redstone", hasItem(Tags.Items.DUSTS_REDSTONE))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(Registration.PROGRAMMER_ITEM.get())
                .key('b', Items.STONE_BUTTON)
                .key('r', Items.REPEATER)
                .patternLine("bbb")
                .patternLine("brb")
                .patternLine("bbb")
                .addCriterion("has_redstone", hasItem(Tags.Items.DUSTS_REDSTONE))
                .build(consumer);
        for (SolidColors color: SolidColors.values()) {
            String colorName = color.name().toLowerCase();
            ShapedRecipeBuilder.shapedRecipe(Registration.WIRELESS_REDSTONE_INDICATOR_ITEMS.get(colorName).get())
                    .key('g', color.getPane())
                    .key('t', Items.REDSTONE_TORCH)
                    .key('p', Tags.Items.ENDER_PEARLS)
                    .patternLine(" g ")
                    .patternLine("gtg")
                    .patternLine("gpg")
                    .addCriterion("has_redstone", hasItem(Tags.Items.DUSTS_REDSTONE))
                    .addCriterion("has_ender_pearl", hasItem(Tags.Items.ENDER_PEARLS))
                    .build(consumer);
        }
    }
}
