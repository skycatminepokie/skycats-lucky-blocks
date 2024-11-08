package com.skycatdev.skycatsluckyblocks;

import com.mojang.serialization.Codec;
import com.skycatdev.skycatsluckyblocks.api.AbstractLuckyBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkycatsLuckyBlocks implements ModInitializer, PlayerBlockBreakEvents.After {
	public static final String MOD_ID = "skycats-lucky-blocks";
	@SuppressWarnings("UnstableApiUsage") public static final AttachmentType<Boolean> SUPER_LLAMA_ATTACHMENT = AttachmentRegistry.<Boolean>builder()
			.persistent(Codec.BOOL)
			.buildAndRegister(Identifier.of(MOD_ID, "super_llama"));
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
		if (state.getBlock() instanceof AbstractLuckyBlock luckyBlock) {
			luckyBlock.afterBlockBreak(world, player, pos, state, blockEntity);
		}
	}

	@Override
	public void onInitialize() {
		LuckyBlocks.init();
		LuckyEffects.init();
		PlayerBlockBreakEvents.AFTER.register(this);
	}
}