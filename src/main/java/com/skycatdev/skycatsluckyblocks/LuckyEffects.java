package com.skycatdev.skycatsluckyblocks;

import com.skycatdev.skycatsluckyblocks.impl.SimpleLuckyEffect;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.skycatdev.skycatsluckyblocks.SkycatsLuckyBlocks.LOGGER;
import static com.skycatdev.skycatsluckyblocks.SkycatsLuckyBlocks.MOD_ID;

public class LuckyEffects {
    public static final SimpleLuckyEffect SAY_HI = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "say_hi"), (world, pos, state, player) -> {
        player.sendMessage(Text.of("[Lucky Block] Hi"));
        return true;
    })
            .addPool(LuckyEffectPools.DEFAULT, 0.1)
            .build();
    public static final SimpleLuckyEffect SPAWN_IRON_GOLEM = new SimpleLuckyEffect.Builder(Identifier.of(MOD_ID, "spawn_iron_golem"), (world, pos, state, player) -> {
        IronGolemEntity golem = EntityType.IRON_GOLEM.spawn(world, pos, SpawnReason.COMMAND);
        if (golem != null) {
            golem.playSound(SoundEvents.ENTITY_IRON_GOLEM_REPAIR);
            return true;
        } else {
            LOGGER.warn("Couldn't spawn a golem for spawn_iron_golem effect");
            return false;
        }
    })
            .addPool(LuckyEffectPools.DEFAULT, 1)
            .build();

    public static void init() {

    }

}
