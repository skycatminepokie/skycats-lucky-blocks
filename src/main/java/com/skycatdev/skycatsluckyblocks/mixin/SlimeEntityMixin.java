package com.skycatdev.skycatsluckyblocks.mixin;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.SlimeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SlimeEntity.class)
public interface SlimeEntityMixin {
    @Accessor
    static TrackedData<Integer> getSLIME_SIZE() {
        throw new AssertionError("Mixin does the work here");
    }
}
