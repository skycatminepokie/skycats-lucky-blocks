package com.skycatdev.skycatsluckyblocks;

import com.skycatdev.skycatsluckyblocks.impl.SimpleLuckyBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class LuckyBlocks {
    public static final SimpleLuckyBlock LUCKY_BLOCK = register(new SimpleLuckyBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.GLASS).solid(),
            LuckyEffectPools.DEFAULT), "lucky_block", true);
    public static final SimpleLuckyBlock WITHER_BLOCK = register(new SimpleLuckyBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.SOUL_SAND).solid(),
            LuckyEffectPools.WITHER), "wither_lucky_block", true);
    public static final SimpleLuckyBlock WEAPON_BLOCK = register(new SimpleLuckyBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.GLASS).solid(),
            LuckyEffectPools.WEAPON), "weapon_lucky_block", true);

    public static void init() {

    }

    public static <T extends Block> T register(T block, String name, boolean shouldRegisterItem) {
        Identifier id = Identifier.of(SkycatsLuckyBlocks.MOD_ID, name);

        if (shouldRegisterItem) {
            Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings()));
        }

        return Registry.register(Registries.BLOCK, id, block);
    }
}
