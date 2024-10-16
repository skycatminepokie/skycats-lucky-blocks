package com.skycatdev.skycatsluckyblocks.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SkycatsLuckyBlocksDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(SkycatsLuckyBlocksEnglishLangProvider::new);
		pack.addProvider(SkycatsLuckyBlocksModelProvider::new);
		pack.addProvider(SkycatsLuckyBlocksEntityTypeTagGenerator::new);
		pack.addProvider(SkycatsLuckyBlocksRecipeGenerator::new);
	}
}
