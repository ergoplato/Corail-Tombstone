package ovh.corail.tombstone.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import static ovh.corail.tombstone.ModTombstone.MOD_ID;

abstract class Registrable {
    static <T extends IForgeRegistryEntry<T>> T registerForgeEntry(IForgeRegistry<T> registry, T entry, String name) {
        return registerForgeEntry(registry, entry, new ResourceLocation(MOD_ID, name));
    }

    static <T extends IForgeRegistryEntry<T>> T registerForgeEntry(IForgeRegistry<T> registry, T entry, ResourceLocation locName) {
        entry.setRegistryName(locName);
        registry.register(entry);
        return entry;
    }
}
