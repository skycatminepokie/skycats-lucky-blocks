package com.skycatdev.skycatsluckyblocks.api;

import com.skycatdev.skycatsluckyblocks.SkycatsLuckyBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public abstract class AbstractLuckyBlock extends Block {
    public static final int MAX_ATTEMPTS = 10;
    public AbstractLuckyBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world instanceof ServerWorld serverWorld) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                Random random = player.getRandom();
                LuckyEffect effect = getEffectPool().getRandom(random);
                if (effect != null) {
                    // This is just a compact way of trying effects over and over again if they fail.
                    int attempts = 0;
                    while (!effect.execute(serverWorld, pos, state, serverPlayer) && attempts++ < MAX_ATTEMPTS) {
                        SkycatsLuckyBlocks.LOGGER.warn("Failed a LuckyEffect, trying again.");
                    }
                } else {
                    SkycatsLuckyBlocks.LOGGER.warn("Effect pool for {} was empty, no effect will be executed.", getName().getString());
                }
            } else {
                SkycatsLuckyBlocks.LOGGER.error("world was a ServerWorld, but player was not a ServerPlayerEntity? This is a bug, please report it at https://github.com/skycatminepokie/skycats-lucky-blocks/issues");
            }
        }
        return super.onBreak(world, pos, state, player);
    }

    public abstract LuckyEffectPool getEffectPool();

}
