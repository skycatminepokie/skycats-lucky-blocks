package com.skycatdev.skycatsluckyblocks.datagen;

import com.skycatdev.skycatsluckyblocks.LuckyBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class SkycatsLuckyBlocksRecipeGenerator extends FabricRecipeProvider {
    public SkycatsLuckyBlocksRecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, LuckyBlocks.LUCKY_BLOCK)
                .pattern("ggg")
                .pattern("gdg")
                .pattern("ggg")
                .input('g', Items.GOLD_INGOT)
                .input('d', Items.DISPENSER)
                .criterion("has_gold_ingot", conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter);
    }
}
