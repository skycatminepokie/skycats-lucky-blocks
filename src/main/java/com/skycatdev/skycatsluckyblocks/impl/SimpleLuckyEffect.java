package com.skycatdev.skycatsluckyblocks.impl;

import com.skycatdev.skycatsluckyblocks.api.LuckyEffect;
import com.skycatdev.skycatsluckyblocks.api.LuckyFunction;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

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
}
