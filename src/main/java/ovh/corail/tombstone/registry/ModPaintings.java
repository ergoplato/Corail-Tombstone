package ovh.corail.tombstone.registry;

import net.minecraft.entity.item.PaintingType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import static ovh.corail.tombstone.ModTombstone.MOD_ID;

@ObjectHolder(MOD_ID)
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModPaintings extends Registrable {

    @SubscribeEvent
    public static void registerPaintings(final RegistryEvent.Register<PaintingType> event) {
        registerForgeEntry(event.getRegistry(), new PaintingType(64, 64), "crow");
    }
}
