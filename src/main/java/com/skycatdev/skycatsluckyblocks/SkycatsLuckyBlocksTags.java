package com.skycatdev.skycatsluckyblocks;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class SkycatsLuckyBlocksTags {
    public static final TagKey<EntityType<?>> SPAWN_SCALED_MOB_MOBS = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SkycatsLuckyBlocks.MOD_ID, "spawn_scaled_mob_mobs"));

}
