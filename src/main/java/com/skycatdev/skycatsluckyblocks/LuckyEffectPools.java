package com.skycatdev.skycatsluckyblocks;

import com.skycatdev.skycatsluckyblocks.impl.SimpleLuckyEffectPool;
import net.minecraft.util.Identifier;

public class LuckyEffectPools {
    public static final SimpleLuckyEffectPool DEFAULT = new SimpleLuckyEffectPool(Identifier.of("default"));

    static {
        DEFAULT.add(LuckyEffects.SAY_HI, 1.0);
    }
}
