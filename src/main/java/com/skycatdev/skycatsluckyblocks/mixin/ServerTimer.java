package com.skycatdev.skycatsluckyblocks.mixin;

import com.skycatdev.skycatsluckyblocks.ServerTimerAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;
import java.util.ListIterator;

@Mixin(MinecraftServer.class)
public class ServerTimer implements ServerTimerAccess {
    private static LinkedList<Pair<Long, Runnable>> timers = new LinkedList<>();
    @Override
    public void skycats_lucky_blocks$addTimer(long ticks, Runnable runnable) {
        timers.add(new Pair<>(ticks, runnable));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void skycats_lucky_blocks$tickTimers(CallbackInfo ci) {
        ListIterator<Pair<Long, Runnable>> it = timers.listIterator();
        while (it.hasNext()) {
            Pair<Long, Runnable> timer = it.next();
            long tick = timer.getLeft();
            if (tick <= 1) {
                timer.getRight().run();
                it.remove();
            } else {
                timer.setLeft(tick - 1);
            }
        }
    }
}
