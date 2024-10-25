package com.skycatdev.skycatsluckyblocks.mixin;

import net.minecraft.block.SaplingGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(SaplingGenerator.class)
public interface SaplingGeneratorMixin {
    @Accessor
    static Map<String, SaplingGenerator> getGENERATORS() {
        throw new AssertionError("Mixin does the work here");
    }
}
