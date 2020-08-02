package ovh.corail.tombstone.perk;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import ovh.corail.tombstone.api.capability.Perk;

import static ovh.corail.tombstone.ModTombstone.MOD_ID;

public class PerkRegistry {
    public static final ForgeRegistry<Perk> perkRegistry = (ForgeRegistry<Perk>) new RegistryBuilder<Perk>()
            .setType(Perk.class)
            .setName(new ResourceLocation(MOD_ID, "perks"))
            .setIDRange(0, 512)
            .create();

    // TODO wait forge to be more stable
    /*@SubscribeEvent
    public void createRegistry(RegistryEvent.NewRegistry event) {
    }*/
}
