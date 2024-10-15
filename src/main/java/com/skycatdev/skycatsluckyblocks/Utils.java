package com.skycatdev.skycatsluckyblocks;

import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class Utils {
    public static <T> @Nullable T getRandomFromTag(Registry<T> registry, TagKey<T> tag, Random random) {
        var optEntryList = registry.getEntryList(tag);
        if (optEntryList.isEmpty()) return null;
        var entryList = optEntryList.get();
        var optEntry = entryList.getRandom(random);
        //noinspection OptionalIsPresent
        if (optEntry.isEmpty()) return null;
        return optEntry.get().value();
    }
}
