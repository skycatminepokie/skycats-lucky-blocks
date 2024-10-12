package com.skycatdev.skycatsluckyblocks.api;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public interface LuckyEffectPool {
    /**
     * Adds an effect to this pool.
     * @param effect The effect to add to this pool.
     * @param weight The weight to give this effect.
     * @return {@code false} if the pool already contained this effect (and this new one was ignored).
     */
    boolean add(LuckyEffect effect, double weight);

    /**
     * Gets a random {@link LuckyEffect} from this pool.
     * @param random The RNG to use.
     * @return A random LuckyEffect, or {@code null} if the pool is empty.
     */
    @Nullable LuckyEffect getRandom(Random random);
    Identifier getId();
}
