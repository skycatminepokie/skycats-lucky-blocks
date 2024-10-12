package com.skycatdev.skycatsluckyblocks.impl;

import com.skycatdev.skycatsluckyblocks.SkycatsLuckyBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class LuckyBlocks {
    public static void init() {

    }

    public static Block register(Block block, String name, boolean shouldRegisterItem) {
        Identifier id = Identifier.of(SkycatsLuckyBlocks.MOD_ID, name);

        if (shouldRegisterItem) {
            Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings()));
        }

        return Registry.register(Registries.BLOCK, id, block);
    }
}
