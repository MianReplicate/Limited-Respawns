package mc.mian.templatemod.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.storage.loot.LootTable;

public class TemplateUtil {
    public static ResourceLocation modLoc(String name) {
        return ResourceLocation.fromNamespaceAndPath(TemplateConstants.MOD_ID, name);
    }
    public static ResourceKey<JukeboxSong> createJukeboxSong(String domain, String name){
        return ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(domain, name));
    }
    public static ResourceKey<PlacedFeature> createPlacedFeature(String domain, String name){
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(domain, name));
    }
    public static ResourceKey<ConfiguredFeature<?, ?>> createConfiguredFeature(String domain, String name){
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(domain, name));
    }

    public static ResourceKey<LootTable> createLootTable(String domain, String name){
        return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(domain, name));
    }

    public static ResourceKey<StructureTemplatePool> createTemplatePool(String domain, String name) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.fromNamespaceAndPath(domain, name));
    }

    public static ResourceKey<StructureSet> createStructureSet(String domain, String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, ResourceLocation.fromNamespaceAndPath(domain, name));
    }

    public static ResourceKey<Structure> createStructure(String domain, String name) {
        return ResourceKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(domain, name));
    }
}
