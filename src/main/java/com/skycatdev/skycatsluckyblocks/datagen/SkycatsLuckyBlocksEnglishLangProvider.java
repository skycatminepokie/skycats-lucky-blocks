package com.skycatdev.skycatsluckyblocks.datagen;

import com.skycatdev.skycatsluckyblocks.LuckyBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class SkycatsLuckyBlocksEnglishLangProvider extends FabricLanguageProvider {
    protected SkycatsLuckyBlocksEnglishLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder tb) {
        tb.add(LuckyBlocks.LUCKY_BLOCK, "Lucky Block"); // Also does item apparently
    }
}
