package com.skycatdev.skycatsluckyblocks;

import com.skycatdev.skycatsluckyblocks.impl.SimpleLuckyEffect;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.skycatdev.skycatsluckyblocks.SkycatsLuckyBlocks.MOD_ID;

public class LuckyEffects {
    // TODO: Testing
    public static final SimpleLuckyEffect SAY_HI = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "say_hi"), (world, pos, state, player) -> player.sendMessage(Text.of("[Lucky Block] Hi")))
            .addPool(LuckyEffectPools.DEFAULT, 0.1)
            .build();
}
