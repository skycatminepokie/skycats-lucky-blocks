package com.skycatdev.skycatsluckyblocks.impl;

import com.skycatdev.skycatsluckyblocks.api.LuckyEffect;
import com.skycatdev.skycatsluckyblocks.api.LuckyEffectPool;
import com.skycatdev.skycatsluckyblocks.api.LuckyFunction;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class SimpleLuckyEffect implements LuckyEffect {
    protected Identifier id;
    protected LuckyFunction function;

    public SimpleLuckyEffect(Identifier id, LuckyFunction function) {
        this.id = id;
        this.function = function;
    }

    @Override
    public void execute(ServerWorld world, BlockPos pos, BlockState state, ServerPlayerEntity player) {
        function.execute(world, pos, state, player);
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public static class Builder {
        protected Identifier id;
        protected LuckyFunction function;
        protected ArrayList<LuckyEffectPool> pools = new ArrayList<>();
        protected ArrayList<Double> weights = new ArrayList<>();

        public Builder(Identifier id, LuckyFunction function) {
            this.id = id;
            this.function = function;
        }

        public Builder setFunction(LuckyFunction function) {
            this.function = function;
            return this;
        }

        public Builder setId(Identifier id) {
            this.id = id;
            return this;
        }

        public Builder addPool(LuckyEffectPool pool, double weight) {
            pools.add(pool);
            weights.add(weight);
            return this;
        }

        public SimpleLuckyEffect build() {
            SimpleLuckyEffect effect = new SimpleLuckyEffect(id, function);
            for (int i = 0; i < pools.size(); i++) {
                LuckyEffectPool pool = pools.get(i);
                pool.add(effect, weights.get(i));
            }
            return effect;
        }
    }
}
