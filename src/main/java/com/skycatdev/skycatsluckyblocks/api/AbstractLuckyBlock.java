package com.skycatdev.skycatsluckyblocks.api;

import com.skycatdev.skycatsluckyblocks.SkycatsLuckyBlocks;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractLuckyBlock extends Block implements PlayerBlockBreakEvents.After {
    public static final int MAX_ATTEMPTS = 10;

    public AbstractLuckyBlock(Settings settings) {
        super(settings);
    }

    /**
     * Executes a {@link LuckyEffect} from this block's {@link LuckyEffectPool}.
     *
     * @param world       the world where the block was broken
     * @param player      the player who broke the block
     * @param pos         the position where the block was broken
     * @param state       the block state <strong>before</strong> the block was broken
     * @param blockEntity the block entity of the broken block, can be {@code null}
     * @implSpec Do not register this, skycats-lucky-blocks will make sure it is called.
     * @implNote {@link Block#onBroken}, {@link Block#onStateReplaced}, and {@link Block#onStacksDropped} aren't used because they don't provide a player.</br>
     * {@link Block#onBreak} isn't used because the block is destroyed afterward, making any block placed at {@code pos} by the effect disappear.</br>
     * This is filtered to only be on the server-side, since that's where all effects should be taking place. Anything client-side will need a packet - the server likely doesn't share the player's {@code Random}, so we can't rely on that.
     */
    @Override
    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
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
    }

    public abstract LuckyEffectPool getEffectPool();

}
