package com.skycatdev.skycatsluckyblocks;

import com.skycatdev.skycatsluckyblocks.impl.SimpleLuckyEffect;
import com.skycatdev.skycatsluckyblocks.mixin.SaplingGeneratorMixin;
import com.skycatdev.skycatsluckyblocks.mixin.SlimeEntityMixin;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureStart;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.structure.Structure;
import org.apache.commons.lang3.function.Consumers;

import java.util.Optional;

import static com.skycatdev.skycatsluckyblocks.SkycatsLuckyBlocks.LOGGER;
import static com.skycatdev.skycatsluckyblocks.SkycatsLuckyBlocks.MOD_ID;

public class LuckyEffects {
    @SuppressWarnings("unused") public static final SimpleLuckyEffect SAY_HI = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "say_hi"), (world, pos, state, player) -> {
        player.sendMessage(Text.of("[Lucky Block] Hi")); // TODO: Localize
        return true;
    })
            .addPool(LuckyEffectPools.DEFAULT, 0.1)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect SPAWN_IRON_GOLEM = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "spawn_iron_golem"), (world, pos, state, player) -> spawnIronGolem(world, pos, player, player.getRandom().nextBoolean()))
            .addPool(LuckyEffectPools.DEFAULT, 0.3)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect RANDOM_TREE = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "random_tree"), ((world, pos, state, player) -> {
        pos = pos.up();
        SaplingGenerator[] generators = SaplingGeneratorMixin.getGENERATORS().values().toArray(new SaplingGenerator[0]);
        Random random = player.getRandom();
        return generators[random.nextInt(generators.length)].generate(world, world.getChunkManager().getChunkGenerator(), pos, state, random);
    }))
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect PLACE_STRUCTURE = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "place_structure"), LuckyEffects::placeRandomStructure)
            .addPool(LuckyEffectPools.DEFAULT, 0.1)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect SPAWN_WITHER = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "spawn_wither"), (world, pos, state, player) -> spawnWither(world, pos))
            .addPool(LuckyEffectPools.WITHER, 1)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect DROP_DIAMOND = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "drop_diamond"), (world, pos, state, player) -> {
        ItemStack diamond = new ItemStack(Items.DIAMOND);
        diamond.set(DataComponentTypes.CUSTOM_NAME, Text.of("Not a diamund"));
        return world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), diamond));
    })
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect DROP_KB_STICK = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "drop_kb_stick"), (world, pos, state, player) -> {
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
    })
            .addPool(LuckyEffectPools.WEAPON, 1)
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect DROP_WOOD_SWORD = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "drop_wood_sword"), ((world, pos, state, player) -> {
        ItemStack wood_sword = new ItemStack(Items.WOODEN_SWORD);
        ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
        Optional<RegistryEntry.Reference<Enchantment>> optSharpness = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.SHARPNESS);
        if (optSharpness.isEmpty()) {
            LOGGER.warn("Your blade is dull (._.), skipping DROP_WOOD_SWORD");
            return false;
        }
        builder.add(optSharpness.get(), 10);
        wood_sword.set(DataComponentTypes.ENCHANTMENTS,
                builder.build());
        return world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), wood_sword));
    }))
            .addPool(LuckyEffectPools.WEAPON, 3)
            .addPool(LuckyEffectPools.DEFAULT, 0.1)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect DROP_DIAMOND_SWORD = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "drop_diamond_sword"), ((world, pos, state, player) -> {
        ItemStack diamond_sword = new ItemStack(Items.DIAMOND_SWORD);
        ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
        Optional<RegistryEntry.Reference<Enchantment>> optSilkTouch = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.SILK_TOUCH);
        if (optSilkTouch.isEmpty()) {
            LOGGER.warn("Silk touch has been deleted, send help, couldn't give you the garbage. Failed DROP_DIAMOND_SWORD");
            return false;
        }
        builder.add(optSilkTouch.get(), 2);

        diamond_sword.set(DataComponentTypes.CUSTOM_NAME, Text.of("It's Garbage"));
        diamond_sword.set(DataComponentTypes.ENCHANTMENTS,
                builder.build());
        return world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), diamond_sword));
    }))
            .addPool(LuckyEffectPools.WEAPON, 30)
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect SPAWN_SCALED_MOB = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "spawn_scaled_mob"), LuckyEffects::spawnScaledMob)
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect PLACE_CAGE = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "place_cage"), (world, pos, state, player) -> {
        BlockPos structurePos = player.getBlockPos().west().north();
        return placeStructure(world, structurePos, structurePos, player, Identifier.of(MOD_ID, "cage"), false);
    })
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect PLACE_LAVA_CAGE = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "place_lava_cage"), LuckyEffects::placeLavaCage)
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect PLACE_CAKE = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "place_cake"), (world, pos, state, player) -> world.setBlockState(pos, Blocks.CAKE.getDefaultState()))
            .addPool(LuckyEffectPools.DEFAULT, 0.3)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect PLACE_LIE_CAKE = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "place_lie_cake"), (world, pos, state, player) -> placeStructure(world, pos, pos, player, Identifier.of(MOD_ID, "lie_cake"), true))
            .addPool(LuckyEffectPools.DEFAULT, 0.3)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect PLACE_TRUTH_CAKE = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "place_truth_cake"), (world, pos, state, player) -> placeStructure(world, pos, pos, player, Identifier.of(MOD_ID, "truth_cake"), true))
            .addPool(LuckyEffectPools.DEFAULT, 0.3)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect PLACE_PARADOX_CAKE = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "place_paradox_cake"), (world, pos, state, player) -> placeStructure(world, pos, pos, player, Identifier.of(MOD_ID, "paradox_cake"), true))
            .addPool(LuckyEffectPools.DEFAULT, 0.3)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect SPAWN_CHARGED_CREEPER = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "spawn_charged_creeper"), (world, pos, state, player) -> {
        EntityType.CREEPER.spawn(world, pos, SpawnReason.COMMAND);
        EntityType.LIGHTNING_BOLT.spawn(world, pos.up(), SpawnReason.COMMAND);
        return true;
    })
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect SPAWN_SLIME_STACK = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "spawn_slime_stack"), (world, pos, state, player) -> spawnSlimeStack(world, pos, player.getRandom().nextBoolean(), player.getRandom().nextBoolean()))
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect PLACE_TNT_CUBE = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "place_tnt_cube"), (world, pos, state, player) -> {
        placeStructure(world, pos, pos, player, Identifier.of(MOD_ID, "hollow_tnt_cube"), false);
        if (player.getRandom().nextBoolean()) {
            TntEntity tnt = EntityType.TNT.create(world, Consumers.nop(), pos.south().east().up(), SpawnReason.COMMAND, true, false);
            if (tnt == null) {
                return false;
            }
            tnt.setFuse(80);
            world.spawnNewEntityAndPassengers(tnt);
            world.playSound(null, tnt.getX(), tnt.getY(), tnt.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1, 1);
        } else {
            world.setBlockState(pos.south().east().up(), Blocks.TNT.getDefaultState());
        }
        return true;
    })
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect SPAWN_LONG_FUSE_TNT = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "spawn_long_fuse_tnt"), (world, pos, state, player) -> {
        TntEntity tnt = EntityType.TNT.create(world, Consumers.nop(), pos, SpawnReason.COMMAND, false, false);
        if (tnt == null) {
            return false;
        }
        tnt.setFuse(player.getRandom().nextBetween(100, 800));
        world.spawnNewEntityAndPassengers(tnt);
        world.playSound(null, tnt.getX(), tnt.getY(), tnt.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1, 1);
        return true;
    })
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect SPAWN_CREEPERS_ON_BATS = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "spawn_creepers_on_bats"), (world, pos, state, player) -> {
        for (int i = 0; i < 3; i++) {
            CreeperEntity creeper = EntityType.CREEPER.create(world, Consumers.nop(), pos, SpawnReason.COMMAND, false, false);
            BatEntity bat = EntityType.BAT.create(world, Consumers.nop(), pos, SpawnReason.COMMAND, false, false);
            if (creeper == null || bat == null) {
                return i != 0; // If we haven't spawned yet, then whatever, try again. If we have, then just go with what we have
            }
            creeper.startRiding(bat);
            world.spawnNewEntityAndPassengers(bat);
        }
        return true;
    })
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect DROP_RANDOM_ENCHANTABLE = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "drop_random_enchantable"), (world, pos, state, player) -> {
        Item sword = Utils.getRandomFromTag(Registries.ITEM, ConventionalItemTags.ENCHANTABLES, player.getRandom());
        if (sword == null) {
            LOGGER.warn("Couldn't find any swords, are tags broken?");
            return false;
        }
        ItemStack itemStack = new ItemStack(sword);
        if (enchantRandomly(itemStack, world, player.getRandom(), 10, player.getRandom().nextBetween(1, 15))) {
            dropItemStack(itemStack, pos, world);
        } else {
            return false;
        }
        return true;
    })
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect DROP_RANDOM_ENCHANTED_BOOK = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "drop_random_enchanted_book"), (world, pos, state, player) -> {
        ItemStack itemStack = new ItemStack(Items.ENCHANTED_BOOK);
        if (!enchantRandomly(itemStack, world, player.getRandom(), 10)) {
            return false;
        }
        dropItemStack(itemStack, pos, world);
        return true;
    })
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    @SuppressWarnings("unused") public static final SimpleLuckyEffect SPAWN_BOB = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "spawn_bob"), (world, pos, state, player) -> {
        ItemStack helmet = new ItemStack(Items.DIAMOND_HELMET);
        if (!enchantRandomly(helmet, world, player.getRandom(), 10, 10)) return false;
        ItemStack chestplate = new ItemStack(Items.DIAMOND_CHESTPLATE);
        if (!enchantRandomly(chestplate, world, player.getRandom(), 10, 10)) return false;
        ItemStack leggings = new ItemStack(Items.DIAMOND_LEGGINGS);
        if (!enchantRandomly(leggings, world, player.getRandom(), 10, 10)) return false;
        ItemStack boots = new ItemStack(Items.DIAMOND_BOOTS);
        if (!enchantRandomly(boots, world, player.getRandom(), 10, 10)) return false;
        ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
        if (!enchantRandomly(sword, world, player.getRandom(), 10, 10)) return false;

        ZombieEntity bob = EntityType.ZOMBIE.create(world, Consumers.nop(), pos, SpawnReason.COMMAND, false, false);
        if (bob == null) return false;
        bob.equipStack(EquipmentSlot.HEAD, helmet);
        bob.equipStack(EquipmentSlot.CHEST, chestplate);
        bob.equipStack(EquipmentSlot.LEGS, leggings);
        bob.equipStack(EquipmentSlot.FEET, boots);
        bob.equipStack(EquipmentSlot.MAINHAND, sword);
        bob.setCustomName(Text.of("Bob"));
        return world.spawnNewEntityAndPassengers(bob);
    })
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();

    private static void dropItemStack(ItemStack itemStack, BlockPos pos, ServerWorld world) {
        ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
    }

    /**
     * @return {@code true} on success
     */
    private static boolean enchantRandomly(ItemStack itemStack, ServerWorld world, Random random, int maxLevel) {
        Registry<Enchantment> enchantRegistry = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
        var optEnchant = enchantRegistry.getRandom(random);
        if (optEnchant.isEmpty()) {
            LOGGER.warn("Couldn't get a random enchant. Are there no enchants?!");
            return false;
        }
        itemStack.addEnchantment(optEnchant.get(), random.nextBetween(1, maxLevel));
        return true;
    }

    /**
     * @return {code true} iff all enchants were applied. Note that even if this returns {@code false}, some enchants may have been applied.
     */
    private static boolean enchantRandomly(ItemStack itemStack, ServerWorld world, Random random, int maxLevel, int times) {
        for (int i = 0; i < times; i++) {
            if (!enchantRandomly(itemStack, world, random, maxLevel)) {
                return false;
            }
        }
        return true;
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
            livingEntity.setCustomName(Text.of("Uncomfortably Large " + entityTypeName));
        }
        if (scale <= 1.1) {
            livingEntity.setCustomName(Text.of("Slightly Large " + entityTypeName));
            return;
        }
        if (scale <= 1.4) {
            livingEntity.setCustomName(Text.of("Large " + entityTypeName));
            return;
        }
        livingEntity.setCustomName(Text.of("Chonky " + entityTypeName));
    }

    public static void init() {

    }

    private static boolean placeLavaCage(ServerWorld world, BlockPos pos, BlockState state, ServerPlayerEntity player) {
        BlockPos structurePos = player.getBlockPos().offset(player.getHorizontalFacing(), 3).offset(player.getHorizontalFacing().rotateYCounterclockwise()).down(1);
        boolean success = placeStructure(world, structurePos, structurePos, player, Identifier.of(MOD_ID, "lava_cage"), true);
        if (success) {
            BlockPos signPos = structurePos.up().offset(player.getHorizontalFacing().rotateYClockwise(), 1);
            world.setBlockState(signPos,
                    Blocks.OAK_SIGN.getDefaultState().rotate(rotationFromNorth(player.getHorizontalFacing()))
                            .with(Properties.ROTATION, RotationPropertyHelper.fromYaw(player.getYaw() + 180)));
            BlockEntity blockEntity = world.getBlockEntity(signPos);
            if (blockEntity instanceof SignBlockEntity signBlockEntity) {
                signBlockEntity.changeText(text -> new SignText().withMessage(1, Text.of("Look up")), true); // TODO: test
                world.updateListeners(signBlockEntity.getPos(), signBlockEntity.getCachedState(), signBlockEntity.getCachedState(), Block.NOTIFY_ALL);
            } else {
                LOGGER.warn("Couldn't set the text of the sign, as the block entity was either null or not a sign block entity :thinking:. Can't skip it at this point, so we'll go with it.");
            }
        }
        return success;
    }

    private static boolean placeRandomStructure(ServerWorld world, BlockPos pos, BlockState state, ServerPlayerEntity player) {
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

    /**
     * Places a structure template (from a lucky block).
     *
     * @param world     The world to place it in.
     * @param pos       The position to place it at.
     * @param pivot     The position to pivot on.
     * @param player    The player that opened the lucky block.
     * @param structure The id of the structure to place.
     * @param rotate    Whether to rotate to match the player. This assumes the structure is built as if the player is facing north.
     * @return {@code true} on success
     */
    private static boolean placeStructure(ServerWorld world, BlockPos pos, BlockPos pivot, ServerPlayerEntity player, Identifier structure, boolean rotate) {
        Optional<StructureTemplate> optTemplate = world.getStructureTemplateManager().getTemplate(structure);
        if (optTemplate.isEmpty()) {
            LOGGER.warn("Couldn't find structure {}. Skipping.", structure);
            return false;
        }
        StructurePlacementData placementData = new StructurePlacementData();
        if (rotate) {
            placementData.setRotation(rotationFromNorth(player.getHorizontalFacing()));
        }
        optTemplate.get().place(world, pos, pivot, placementData, player.getRandom(), 2);
        return true;
    }

    private static BlockRotation rotationFromNorth(Direction direction) {
        return switch (direction) {
            case EAST -> BlockRotation.CLOCKWISE_90;
            case SOUTH -> BlockRotation.CLOCKWISE_180;
            case WEST -> BlockRotation.COUNTERCLOCKWISE_90;
            case null, default -> BlockRotation.NONE;
        };
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

    private static boolean spawnSlimeStack(ServerWorld world, BlockPos pos, boolean inverted, boolean magma) {
        int numberInStack = 5;
        EntityType<?> entityType = magma ? EntityType.MAGMA_CUBE : EntityType.SLIME;

        LivingEntity base = (LivingEntity) entityType.create(world, (e) -> {
        }, pos, SpawnReason.COMMAND, false, false);
        if (base == null) {
            return false;
        }
        base.getDataTracker().set(SlimeEntityMixin.getSLIME_SIZE(), inverted ? 1 : numberInStack);
        base.setHealth(inverted ? 1 : numberInStack * 2);
        LivingEntity top = base;
        for (int i = 0; i < numberInStack - 1; i++) {
            LivingEntity current = (LivingEntity) entityType.create(world, (e) -> {
            }, pos, SpawnReason.COMMAND, false, false);
            if (current == null) {
                return false;
            }
            int size = inverted ? i + 2 : numberInStack - (i + 1);
            current.getDataTracker().set(SlimeEntityMixin.getSLIME_SIZE(), size);
            current.setHealth((size) * 2); // Doesn't follow vanilla conventions, but it would get quite high quite fast if it did
            current.startRiding(top);
            top = current;
        }
        return world.spawnNewEntityAndPassengers(base);
    }

    private static boolean spawnWither(ServerWorld world, BlockPos pos) {
        WitherEntity wither = EntityType.WITHER.spawn(world, pos, SpawnReason.CONVERSION);
        if (wither != null) {
            wither.playSound(SoundEvents.ENTITY_WITHER_SPAWN);
            wither.setHealth(500f);
            wither.setCustomName(Text.of("Flying Warden that can see"));
            LOGGER.info("Spawned a wither");
            return true;
        } else {
            LOGGER.warn("Couldn't spawn a wither for spawn_wither effect");
            return false;
        }

    }

}
