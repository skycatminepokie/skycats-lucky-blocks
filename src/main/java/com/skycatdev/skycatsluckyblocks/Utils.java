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
    public static <T> T chooseRandom(Random random, T option1, T option2) {
        return random.nextBoolean() ? option1 : option2;
    }

    public static <T> T chooseRandom(Random random, T option1, T option2, T option3) {
        return switch (random.nextBetween(1, 3)) {
            case 1 -> option1;
            case 2 -> option2;
            case 3 -> option3;
            default -> throw new RuntimeException();
        };
    }

    public static <T> T chooseRandom(Random random, T option1, T option2, T option3, T option4) {
        return switch (random.nextBetween(1, 4)) {
            case 1 -> option1;
            case 2 -> option2;
            case 3 -> option3;
            case 4 -> option4;
            default -> throw new RuntimeException();
        };
    }

    public static <T> T chooseRandom(Random random, T option1, T option2, T option3, T option4, T option5) {
        return switch (random.nextBetween(1, 5)) {
            case 1 -> option1;
            case 2 -> option2;
            case 3 -> option3;
            case 4 -> option4;
            case 5 -> option5;
            default -> throw new RuntimeException();
        };
    }

    @SafeVarargs // If you can convince me whether this is right or wrong, I'll give you a virtual high-five
    public static <T> T chooseRandom(Random random, T... options) {
        return options[random.nextInt(options.length - 1)];
    }
}
