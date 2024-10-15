package com.skycatdev.skycatsluckyblocks;


import com.skycatdev.skycatsluckyblocks.impl.SimpleLuckyEffect;
import com.skycatdev.skycatsluckyblocks.mixin.SaplingGeneratorMixin;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import static com.skycatdev.skycatsluckyblocks.SkycatsLuckyBlocks.LOGGER;
import static com.skycatdev.skycatsluckyblocks.SkycatsLuckyBlocks.MOD_ID;

public class LuckyEffects {
    public static final SimpleLuckyEffect SAY_HI = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "say_hi"), (world, pos, state, player) -> {
        player.sendMessage(Text.of("[Lucky Block] Hi"));
        return true;
    })
            .addPool(LuckyEffectPools.DEFAULT, 0.1)
            .build();
    public static final SimpleLuckyEffect SPAWN_IRON_GOLEM = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "spawn_iron_golem"),
            (world, pos, state, player) -> spawnIronGolem(world, pos, player, false))
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    public static final SimpleLuckyEffect SPAWN_ANGRY_IRON_GOLEM = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "spawn_angry_iron_golem"),
            (world, pos, state, player) -> spawnIronGolem(world, pos, player, true))
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    public static final SimpleLuckyEffect RANDOM_TREE = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "random_tree"), ((world, pos, state, player) -> {
        SaplingGenerator[] generators = SaplingGeneratorMixin.getGENERATORS().values().toArray(new SaplingGenerator[0]);
        Random random = player.getRandom();
        return generators[random.nextInt(generators.length)].generate(world, world.getChunkManager().getChunkGenerator(), pos, state, random);
    }))
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    public static final SimpleLuckyEffect SPAWN_WITHER = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "spawn_wither"),
            (world, pos, state, player) -> {
        spawnWither(world, pos, player, false);
        return true;
    })
            .addPool(LuckyEffectPools.WITHER, 1)
            .build();
    public static final SimpleLuckyEffect DROP_DIAMOND = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "drop_diamond"), ((world, pos, state, player) -> {
        ItemStack diamond = new ItemStack(Items.DIAMOND);
        diamond.set(DataComponentTypes.CUSTOM_NAME, Text.of("Not a diamund"));
        return world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), diamond));
    }))
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    public static final SimpleLuckyEffect DROP_KB_STICK = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID,"drop_kb_stick"), ((world, pos, state, player) -> {
        ItemStack kb_stick = new ItemStack(Items.STICK);
        ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
        builder.add(world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.KNOCKBACK).get(), 200);
        kb_stick.set(DataComponentTypes.ENCHANTMENTS,
                builder.build());
        return world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), kb_stick));
    }))
            .addPool(LuckyEffectPools.WEAPON, 1)
            .build();

    private static boolean spawnIronGolem(ServerWorld world, BlockPos pos, ServerPlayerEntity player, boolean angry) {
        IronGolemEntity golem = EntityType.IRON_GOLEM.spawn(world, pos, SpawnReason.COMMAND);
        if (golem != null) {
            golem.playSound(SoundEvents.ENTITY_IRON_GOLEM_REPAIR);
            if (angry) {
                golem.setAngryAt(player.getUuid());
            }
            return true;
        } else {
            LOGGER.warn("Couldn't spawn a golem for spawn_iron_golem effect");
            return false;
        }
    }
    private static boolean spawnWither(ServerWorld world, BlockPos pos, ServerPlayerEntity player, boolean angry) {
        WitherEntity wither = EntityType.WITHER.spawn(world, pos, SpawnReason.CONVERSION);
        if (wither != null) {
            wither.playSound(SoundEvents.ENTITY_WITHER_SPAWN);
            wither.setHealth(0.1f);
            return true;
        } else {
            LOGGER.warn("Couldn't spawn a wither for spawn_wither effect");
            return false;
        }

    }

    public static void init() {

    }

}
