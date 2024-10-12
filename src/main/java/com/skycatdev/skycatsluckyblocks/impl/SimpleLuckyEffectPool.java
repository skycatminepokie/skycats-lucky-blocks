package com.skycatdev.skycatsluckyblocks.impl;

import com.skycatdev.skycatsluckyblocks.SkycatsLuckyBlocks;
import com.skycatdev.skycatsluckyblocks.api.LuckyEffect;
import com.skycatdev.skycatsluckyblocks.api.LuckyEffectPool;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SimpleLuckyEffectPool implements LuckyEffectPool {
    private final ArrayList<LuckyEffect> effects = new ArrayList<>();
    private final ArrayList<Double> weights = new ArrayList<>();
    private double totalWeight = 0;
    protected Identifier id;

    public SimpleLuckyEffectPool(Identifier id) {
        this.id = id;
    }

    @Override
    public boolean add(LuckyEffect effect, double weight) {
        if (effects.contains(effect)) {
            return false;
        }
        effects.add(effect);
        weights.add(weight);
        totalWeight += weight;
        return true;
    }

    @Override
    public @Nullable LuckyEffect getRandom(Random random) {
        if (effects.isEmpty()) {
            return null;
        }

        // Think of all the weights as end-to-end ranges on a number line
        // rand is a point on the number line
        double rand = new java.util.Random(random.nextLong()).nextDouble(totalWeight); // TODO: This could be made faster by doing it manually (probably)
        for (int i = 0; i < effects.size(); i++) {
            double weight = weights.get(i);
            if (weight > rand) { // The point rand is in the range weight
                return effects.get(i);
            } else {
                rand -= weight; // We're cutting off this effect from the number line (by pretending it doesn't exist), so move the point down too
            }
        }
        SkycatsLuckyBlocks.LOGGER.error("Impossible random point selection while generating a random effect. This is a bug! Please report this at https://github.com/skycatminepokie/skycats-lucky-blocks/issues.");
        return effects.getLast();
    }

    @Override
    public Identifier getId() {
        return id;
    }
}
