package com.skycatdev.skycatsluckyblocks.impl;

import com.skycatdev.skycatsluckyblocks.api.AbstractLuckyBlock;
import com.skycatdev.skycatsluckyblocks.api.LuckyEffectPool;

public class SimpleLuckyBlock extends AbstractLuckyBlock {
    protected LuckyEffectPool effectPool;
    public SimpleLuckyBlock(Settings settings, LuckyEffectPool effectPool) {
        super(settings);
        this.effectPool = effectPool;
    }

    @Override
    public LuckyEffectPool getEffectPool() {
        return effectPool;
    }
}
