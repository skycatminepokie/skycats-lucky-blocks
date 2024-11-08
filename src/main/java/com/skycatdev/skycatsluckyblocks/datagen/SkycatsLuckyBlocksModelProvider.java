package com.skycatdev.skycatsluckyblocks.datagen;

import com.skycatdev.skycatsluckyblocks.LuckyBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;

public class SkycatsLuckyBlocksModelProvider extends FabricModelProvider {

    public SkycatsLuckyBlocksModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(LuckyBlocks.LUCKY_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(LuckyBlocks.WEAPON_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(LuckyBlocks.WITHER_BLOCK);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
