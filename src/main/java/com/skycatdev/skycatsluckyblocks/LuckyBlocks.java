package com.skycatdev.skycatsluckyblocks;

import com.skycatdev.skycatsluckyblocks.impl.SimpleLuckyBlock;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LuckyBlocks {
    public static final RegistryKey<ItemGroup> LUCKY_BLOCKS_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(SkycatsLuckyBlocks.MOD_ID, "lucky_blocks"));
    public static final SimpleLuckyBlock LUCKY_BLOCK = register(new SimpleLuckyBlock(AbstractBlock.Settings.copy(Blocks.GLASS).dropsNothing(),
            LuckyEffectPools.DEFAULT), "lucky_block", true);
    public static final SimpleLuckyBlock WITHER_BLOCK = register(new SimpleLuckyBlock(AbstractBlock.Settings.copy(Blocks.GLASS).dropsNothing().sounds(BlockSoundGroup.SOUL_SAND),
            LuckyEffectPools.WITHER), "wither_lucky_block", true);
    public static final SimpleLuckyBlock WEAPON_BLOCK = register(new SimpleLuckyBlock(AbstractBlock.Settings.copy(Blocks.GLASS).dropsNothing(),
            LuckyEffectPools.WEAPON), "weapon_lucky_block", true);
    public static final ItemGroup LUCKY_BLOCKS_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(LUCKY_BLOCK.asItem()))
            .displayName(Text.translatable("itemGroup." + SkycatsLuckyBlocks.MOD_ID + ".lucky_blocks"))
            .build();

    public static void init() {
        Registry.register(Registries.ITEM_GROUP, LUCKY_BLOCKS_ITEM_GROUP_KEY, LUCKY_BLOCKS_ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(LUCKY_BLOCKS_ITEM_GROUP_KEY).register(group -> {
                    group.add(LUCKY_BLOCK);
                    group.add(WITHER_BLOCK);
                    group.add(WEAPON_BLOCK);
                }
        );
    }

    public static <T extends Block> T register(T block, String name, boolean shouldRegisterItem) {
        Identifier id = Identifier.of(SkycatsLuckyBlocks.MOD_ID, name);

        if (shouldRegisterItem) {
            Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings()));
        }

        return Registry.register(Registries.BLOCK, id, block);
    }
}
