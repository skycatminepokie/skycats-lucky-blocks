package com.skycatdev.skycatsluckyblocks.api;

import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@FunctionalInterface
public interface LuckyFunction {
    void execute(ServerWorld world, BlockPos pos, BlockState state, ServerPlayerEntity player);
}
