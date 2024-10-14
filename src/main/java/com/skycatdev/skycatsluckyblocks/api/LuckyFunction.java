package com.skycatdev.skycatsluckyblocks.api;

import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@FunctionalInterface
public interface LuckyFunction {
    /**
     *
     * @return {@code true} if anything was done, {@code false} if something failed.
     * @implNote Logging and returning {@code false} for failures rather than throwing exceptions allows players to continue playing, even if something goes wrong.
     */
    boolean execute(ServerWorld world, BlockPos pos, BlockState state, ServerPlayerEntity player);
}
