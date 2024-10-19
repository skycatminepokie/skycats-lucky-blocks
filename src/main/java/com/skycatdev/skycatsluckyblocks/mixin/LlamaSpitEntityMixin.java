package com.skycatdev.skycatsluckyblocks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.skycatdev.skycatsluckyblocks.SkycatsLuckyBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.LlamaSpitEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LlamaSpitEntity.class)
public abstract class LlamaSpitEntityMixin extends ProjectileEntity {
    @Contract("_,_->fail")
    private LlamaSpitEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
        throw new AssertionError("Yeeted by mixin.");
    }

    @WrapOperation(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean skycats_lucky_blocks$superSpitDamage(Entity instance, DamageSource source, float amount, Operation<Boolean> original) {
        //noinspection UnstableApiUsage
        if (getOwner() != null && getOwner().getAttachedOrElse(SkycatsLuckyBlocks.SUPER_LLAMA_ATTACHMENT, Boolean.FALSE)) {
            return original.call(instance, source, amount * 10);
        }
        return original.call(instance, source, amount);
    }
}
