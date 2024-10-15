package com.skycatdev.skycatsluckyblocks;

import com.skycatdev.skycatsluckyblocks.impl.SimpleLuckyEffect;
import com.skycatdev.skycatsluckyblocks.mixin.SaplingGeneratorMixin;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.structure.StructureStart;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.structure.Structure;

import java.util.Optional;

import static com.skycatdev.skycatsluckyblocks.SkycatsLuckyBlocks.LOGGER;
import static com.skycatdev.skycatsluckyblocks.SkycatsLuckyBlocks.MOD_ID;

public class LuckyEffects {
    public static final SimpleLuckyEffect SAY_HI = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "say_hi"), (world, pos, state, player) -> {
        player.sendMessage(Text.of("[Lucky Block] Hi")); // TODO: Localize
        return true;
    })
            .addPool(LuckyEffectPools.DEFAULT, 0.1)
            .build();
    public static final SimpleLuckyEffect SPAWN_IRON_GOLEM = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "spawn_iron_golem"), (world, pos, state, player) -> spawnIronGolem(world, pos, player, player.getRandom().nextBoolean()))
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    public static final SimpleLuckyEffect RANDOM_TREE = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "random_tree"), ((world, pos, state, player) -> {
        pos = pos.up();
        SaplingGenerator[] generators = SaplingGeneratorMixin.getGENERATORS().values().toArray(new SaplingGenerator[0]);
        Random random = player.getRandom();
        return generators[random.nextInt(generators.length)].generate(world, world.getChunkManager().getChunkGenerator(), pos, state, random);
    }))
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    public static final SimpleLuckyEffect PLACE_STRUCTURE = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "place_structure"), LuckyEffects::placeStructure)
            .addPool(LuckyEffectPools.DEFAULT, 0.1)
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
        Optional<RegistryEntry.Reference<Enchantment>> optKnockback = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.KNOCKBACK);
        if (optKnockback.isEmpty()) {
            LOGGER.warn("Knockback enchantment didn't exist? Skipping DROP_KB_STICK.");
            return false;
        }
        builder.add(optKnockback.get(), 4);
        kb_stick.set(DataComponentTypes.ENCHANTMENTS,
                builder.build());
        return world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), kb_stick));
    }))
            .addPool(LuckyEffectPools.WEAPON, 1)
            .build();
    public static final SimpleLuckyEffect SPAWN_SCALED_MOB = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "spawn_scaled_mob"), LuckyEffects::spawnScaledMob)
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();

    private static boolean spawnScaledMob(ServerWorld world, BlockPos pos, BlockState state, ServerPlayerEntity player) {
        EntityType<?> entityType = Utils.getRandomFromTag(Registries.ENTITY_TYPE, SkycatsLuckyBlocksTags.SPAWN_SCALED_MOB_MOBS, player.getRandom());
        if (entityType == null) {
            LOGGER.warn("Couldn't get a random mob from SPAWN_SCALED_MOB_MOBS, is it empty?");
            return false;
        }
        Entity entity = entityType.create(world, e -> {
        }, pos, SpawnReason.COMMAND, true, false);
        if (entity instanceof LivingEntity livingEntity) {
            EntityAttributeInstance attribute = livingEntity.getAttributeInstance(EntityAttributes.GENERIC_SCALE);
            if (attribute == null) {
                LOGGER.warn("Random mob from SPAWN_SCALED_MOB_MOBS \"{}\" has no scale attribute!", entity.getName().getString());
                entity.discard();
                return false;
            }
            double scale = new java.util.Random(player.getRandom().nextLong()).nextDouble(0.5, 1.5); // TODO: possibly could be optimized by not creating the random
            attribute.setBaseValue(scale);
            giveScaledName(livingEntity, scale);
            world.spawnNewEntityAndPassengers(livingEntity);
            return true;
        } else {
            LOGGER.warn("Random mob from SPAWN_SCALED_MOB_MOBS \"{}\" was not a LivingEntity and it must be!", entity.getName().getString());
            entity.discard();
            return false;
        }
    }

    private static void giveScaledName(LivingEntity livingEntity, double scale) {
        String entityTypeName = livingEntity.getType().getName().getString();
        if (scale <= 0.6) {
            livingEntity.setCustomName(Text.of("Smol " + entityTypeName));
            return;
        }
        if (scale <= 0.75) {
            livingEntity.setCustomName(Text.of("Small " + entityTypeName));
            return;
        }
        if (scale < 0.99) {
            livingEntity.setCustomName(Text.of("Slightly Small " + entityTypeName));
            return;
        }
        if (scale < 1) {
            livingEntity.setCustomName(Text.of("Uncomfortably Small " + entityTypeName));
            return;
        }
        if (scale == 1) {
            livingEntity.setCustomName(Text.of("Incredibly Uncommon Regular-Sized " + entityTypeName));
            return;
        }
        if (scale <= 1.01) {
            livingEntity.setCustomName(Text.of("Uncomfortably Big " + entityTypeName));
        }
        if (scale <= 1.1) {
            livingEntity.setCustomName(Text.of("Slightly Big " + entityTypeName));
            return;
        }
        if (scale <= 1.4) {
            livingEntity.setCustomName(Text.of("Big " + entityTypeName));
            return;
        }
        livingEntity.setCustomName(Text.of("Chonky " + entityTypeName));
    }

    private static boolean placeStructure(ServerWorld world, BlockPos pos, BlockState state, ServerPlayerEntity player) {
        // A lot of this comes from PlaceCommand#executePlaceStructure
        var optStructure = world.getRegistryManager().get(RegistryKeys.STRUCTURE).getRandom(player.getRandom());
        if (optStructure.isEmpty()) {
            LOGGER.warn("Couldn't place a structure, there are no structures!");
            return false;
        }
        Structure structure = optStructure.get().value();
        ChunkGenerator chunkGenerator = world.getChunkManager().getChunkGenerator();
        StructureStart structureStart = structure.createStructureStart(
                world.getRegistryManager(),
                chunkGenerator,
                chunkGenerator.getBiomeSource(),
                world.getChunkManager().getNoiseConfig(),
                world.getStructureTemplateManager(),
                world.getSeed(),
                new ChunkPos(pos),
                0,
                world,
                (biome) -> true
        );
        if (!structureStart.hasChildren()) {
            LOGGER.info("Couldn't place a structure, the start doesn't have any children to place.");
            return false;
        }
        BlockBox boundingBox = structureStart.getBoundingBox();
        ChunkPos minCorner = new ChunkPos(ChunkSectionPos.getSectionCoord(boundingBox.getMinX()), ChunkSectionPos.getSectionCoord(boundingBox.getMinZ()));
        ChunkPos maxCorner = new ChunkPos(ChunkSectionPos.getSectionCoord(boundingBox.getMaxX()), ChunkSectionPos.getSectionCoord(boundingBox.getMaxZ()));

        if (ChunkPos.stream(minCorner, maxCorner).anyMatch(chunk -> !world.canSetBlock(chunk.getStartPos()))) {
            LOGGER.info("Couldn't place a structure, not everything was loaded.");
            return false;
        }

        ChunkPos.stream(minCorner, maxCorner).forEach(
                chunk -> structureStart.place(
                        world,
                        world.getStructureAccessor(),
                        chunkGenerator,
                        world.getRandom(),
                        new BlockBox(chunk.getStartX(), world.getBottomY(), chunk.getStartZ(), chunk.getEndX(), world.getTopY(), chunk.getEndZ()),
                        chunk)
        );
        player.sendMessage(Text.of("[").copy().append(state.getBlock().getName()).append("] I built something for you! Take a look around.")); // TODO: Localize
        return true;
    }

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
