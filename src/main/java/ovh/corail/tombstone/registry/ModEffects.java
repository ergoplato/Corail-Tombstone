package ovh.corail.tombstone.registry;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import ovh.corail.tombstone.effect.ExorcismEffect;
import ovh.corail.tombstone.effect.FeatherFallEffect;
import ovh.corail.tombstone.effect.GhostlyShapeEffect;
import ovh.corail.tombstone.effect.PurificationEffect;
import ovh.corail.tombstone.effect.ReachEffect;

import static ovh.corail.tombstone.ModTombstone.MOD_ID;

@ObjectHolder(MOD_ID)
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEffects extends Registrable {
    public static final Effect ghostly_shape = new GhostlyShapeEffect();
    public static final Effect preservation = new Effect(EffectType.BENEFICIAL, 10066431) {};
    public static final Effect unstable_intangibleness = new Effect(EffectType.BENEFICIAL, 13095155) {};
    public static final Effect diversion = new Effect(EffectType.BENEFICIAL, 13095155) {};
    public static final Effect feather_fall = new FeatherFallEffect();
    public static final Effect purification = new PurificationEffect();
    public static final Effect true_sight = new Effect(EffectType.BENEFICIAL, 16381144) {};
    public static final Effect exorcism = new ExorcismEffect();
    public static final Effect reach = new ReachEffect();
    public static final Effect lightning_resistance = new Effect(EffectType.BENEFICIAL, 0x36369a) {};

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerPotion(final RegistryEvent.Register<Effect> event) {
        registerForgeEntry(event.getRegistry(), ghostly_shape, "ghostly_shape");
        registerForgeEntry(event.getRegistry(), preservation, "preservation");
        registerForgeEntry(event.getRegistry(), unstable_intangibleness, "unstable_intangibleness");
        registerForgeEntry(event.getRegistry(), diversion, "diversion");
        registerForgeEntry(event.getRegistry(), feather_fall, "feather_fall");
        registerForgeEntry(event.getRegistry(), purification, "purification");
        registerForgeEntry(event.getRegistry(), true_sight, "true_sight");
        registerForgeEntry(event.getRegistry(), exorcism, "exorcism");
        registerForgeEntry(event.getRegistry(), reach, "reach");
        registerForgeEntry(event.getRegistry(), lightning_resistance, "lightning_resistance");
    }
}
