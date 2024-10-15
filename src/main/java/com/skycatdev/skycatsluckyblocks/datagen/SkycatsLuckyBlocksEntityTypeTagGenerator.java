package com.skycatdev.skycatsluckyblocks.datagen;

import com.skycatdev.skycatsluckyblocks.SkycatsLuckyBlocksTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class SkycatsLuckyBlocksEntityTypeTagGenerator extends FabricTagProvider<EntityType<?>> {

    /**
     * Constructs a new {@link FabricTagProvider} with the default computed path.
     *
     * <p>Common implementations of this class are provided.
     *
     * @param output           the {@link FabricDataOutput} instance
     * @param registriesFuture the backing registry for the tag type
     */
    public SkycatsLuckyBlocksEntityTypeTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ENTITY_TYPE, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        getOrCreateTagBuilder(SkycatsLuckyBlocksTags.SPAWN_SCALED_MOB_MOBS)
                .add(EntityType.BLAZE)
                .add(EntityType.CREEPER)
                .add(EntityType.ENDERMAN)
                .add(EntityType.SKELETON)
                .add(EntityType.ZOMBIE)

                .add(EntityType.CAMEL)
                .add(EntityType.CAT)
                .add(EntityType.COW)
                .add(EntityType.FOX)
                .add(EntityType.IRON_GOLEM);
    }
}
