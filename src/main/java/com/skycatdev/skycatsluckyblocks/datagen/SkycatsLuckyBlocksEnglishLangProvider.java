package com.skycatdev.skycatsluckyblocks.datagen;

import com.skycatdev.skycatsluckyblocks.LuckyBlocks;
import com.skycatdev.skycatsluckyblocks.SkycatsLuckyBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class SkycatsLuckyBlocksEnglishLangProvider extends FabricLanguageProvider {
    protected SkycatsLuckyBlocksEnglishLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder tb) {
        tb.add(LuckyBlocks.LUCKY_BLOCK, "Lucky Block"); // Also does item apparently
        addText("effect.say_hi.hi", "[%s] Hi", tb);
        addText("effect.drop_diamond.name", "Not a diamund", tb);
        addText("effect.drop_kb_stick.no_kb", "Knockback enchantment didn't exist? Skipping DROP_KB_STICK." , tb);
        addText("effect.drop_wood_sword.no_sharpness", "Your blade is dull (._.), skipping DROP_WOOD_SWORD." , tb);
        addText("effect.drop_diamond_sword.no_silk_touch", "Silk touch has been deleted, send help, couldn't give you the garbage. Failed DROP_DIAMOND_SWORD", tb);
        addText("effect.drop_diamond_sword.name", "Garbage", tb);
        addText("effect.drop_random_enchantable.no_enchantables", "Couldn't find any enchantables, are tags broken?", tb);
        addText("effect.spawn_wandering_caravan_with_super_llamas.llama_name", "Super %s", tb);
        addText("enchant_randomly.no_enchants", "Couldn't get a random enchant. Are there no enchants?!", tb);
        addText("give_scaled_name.smallest", "Smol %s", tb);
        addText("give_scaled_name.small", "Small %s", tb);
        addText("give_scaled_name.slightly_small", "Slightly Small %s", tb);
        addText("give_scaled_name.barely_small", "Uncomfortably Small %s", tb);
        addText("give_scaled_name.regular", "Incredibly Uncommon Regular-Sized %s", tb);
        addText("give_scaled_name.barely_big", "Uncomfortably Large %s", tb);
        addText("give_scaled_name.slightly_big", "Slightly Large %s", tb);
        addText("give_scaled_name.big", "Large %s", tb);
        addText("give_scaled_name.biggest", "Chonky %s", tb);
        addText("place_lava_cage.sign", "Look up", tb);
        addText("place_lava_cage.failed_set_text", "Couldn't set the text of the sign, as the block entity was either null or not a sign block entity :thinking:. Can't skip it at this point, so we'll go with it.", tb);
        addText("effect.place_structure.no_structures", "Couldn't place a structure, there are no structures!", tb);
        addText("effect.place_structure.no_children", "Couldn't place a structure, the start doesn't have any children to place.", tb);
        addText("effect.place_structure.not_loaded", "Couldn't place a structure, not everything was loaded.", tb);
        addText("effect.place_structure.notification", "[%s] I built something for you! Take a look around.", tb);
        addText("place_structure.no_structure", "Couldn't find structure %s. Skipping.", tb);
        addText("effect.spawn_iron_golem.failed", "Couldn't spawn a golem for spawn_iron_golem effect", tb);
        addText("spawn_scaled_mob.empty", "Couldn't get a random mob from SPAWN_SCALED_MOB_MOBS, is it empty?", tb);
        addText("spawn_scaled_mob.no_attribute", "Random mob from SPAWN_SCALED_MOB_MOBS \"%s\" has no scale attribute!", tb);
        addText("spawn_scaled_mob.not_living_entity", "Random mob from SPAWN_SCALED_MOB_MOBS \"%s\" was not a LivingEntity and it must be!", tb);
        addText("effect.spawn_wither.name", "Flying Warden that can See", tb);
        addText("effect.spawn_wither.log", "Spawned a wither", tb);
        addText("effect.spawn_wither.failed", "Couldn't spawn a wither for spawn_wither effect", tb);
        addText("effect.spawn_bob.bob", "Bob", tb);
        tb.add("itemGroup." + SkycatsLuckyBlocks.MOD_ID + ".lucky_blocks", "Lucky Blocks");
        addText("effect.spawn_warden_with_warning.run", "run.", tb);
    }

    public void addText(String path, String text, TranslationBuilder tb) {
        tb.add(SkycatsLuckyBlocks.MOD_ID + "." + path, text);
    }
}
