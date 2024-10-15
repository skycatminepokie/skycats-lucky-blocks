package com.skycatdev.skycatsluckyblocks;

import com.skycatdev.skycatsluckyblocks.impl.SimpleLuckyEffect;
import com.skycatdev.skycatsluckyblocks.mixin.SaplingGeneratorMixin;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.registry.RegistryKeys;
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

import static com.skycatdev.skycatsluckyblocks.SkycatsLuckyBlocks.LOGGER;
import static com.skycatdev.skycatsluckyblocks.SkycatsLuckyBlocks.MOD_ID;

public class LuckyEffects {
    public static final SimpleLuckyEffect SAY_HI = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "say_hi"), (world, pos, state, player) -> {
        player.sendMessage(Text.of("[Lucky Block] Hi"));
        return true;
    })
            .addPool(LuckyEffectPools.DEFAULT, 0.1)
            .build();
    public static final SimpleLuckyEffect SPAWN_IRON_GOLEM = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "spawn_iron_golem"), (world, pos, state, player) -> spawnIronGolem(world, pos, player, false))
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    public static final SimpleLuckyEffect SPAWN_ANGRY_IRON_GOLEM = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "spawn_angry_iron_golem"), (world, pos, state, player) -> spawnIronGolem(world, pos, player, true))
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    public static final SimpleLuckyEffect RANDOM_TREE = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "random_tree"), ((world, pos, state, player) -> {
        SaplingGenerator[] generators = SaplingGeneratorMixin.getGENERATORS().values().toArray(new SaplingGenerator[0]);
        Random random = player.getRandom();
        return generators[random.nextInt(generators.length)].generate(world, world.getChunkManager().getChunkGenerator(), pos, state, random);
    }))
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();
    public static final SimpleLuckyEffect PLACE_STRUCTURE = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "place_structure"), ((world, pos, state, player) -> {
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
            LOGGER.debug("Couldn't place a structure, the start doesn't have any children to place.");
            return false;
        }
        BlockBox boundingBox = structureStart.getBoundingBox();
        ChunkPos minCorner = new ChunkPos(ChunkSectionPos.getSectionCoord(boundingBox.getMinX()), ChunkSectionPos.getSectionCoord(boundingBox.getMinZ()));
        ChunkPos maxCorner = new ChunkPos(ChunkSectionPos.getSectionCoord(boundingBox.getMaxX()), ChunkSectionPos.getSectionCoord(boundingBox.getMaxZ()));

        if (ChunkPos.stream(minCorner, maxCorner).anyMatch(chunk -> !world.canSetBlock(chunk.getStartPos()))) {
            LOGGER.debug("Couldn't place a structure, not everything was loaded.");
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
    }))
            .addPool(LuckyEffectPools.DEFAULT, 0.1)
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

    public static void init() {

    }

}
