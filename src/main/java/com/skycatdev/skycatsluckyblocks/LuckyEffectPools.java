package com.skycatdev.skycatsluckyblocks;

import com.skycatdev.skycatsluckyblocks.impl.SimpleLuckyEffectPool;
import net.minecraft.util.Identifier;

import static com.skycatdev.skycatsluckyblocks.SkycatsLuckyBlocks.MOD_ID;

public class LuckyEffectPools {
    public static final SimpleLuckyEffectPool DEFAULT = new SimpleLuckyEffectPool(Identifier.of(MOD_ID, "default"));
    public static final SimpleLuckyEffectPool WITHER = new SimpleLuckyEffectPool(Identifier.of(MOD_ID, "boss"));
    public static final SimpleLuckyEffectPool WEAPON = new SimpleLuckyEffectPool(Identifier.of(MOD_ID, "weapons"));
}
