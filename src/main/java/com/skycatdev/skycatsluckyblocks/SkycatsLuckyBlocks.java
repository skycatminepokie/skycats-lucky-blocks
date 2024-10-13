package com.skycatdev.skycatsluckyblocks;

import com.skycatdev.skycatsluckyblocks.impl.LuckyBlocks;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkycatsLuckyBlocks implements ModInitializer {
	public static final String MOD_ID = "skycats-lucky-blocks";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LuckyBlocks.init();
		LuckyEffects.init();
	}
}