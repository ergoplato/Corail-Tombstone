package ovh.corail.tombstone.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import ovh.corail.tombstone.enchantment.EnchantmentMagicSiphon;
import ovh.corail.tombstone.enchantment.EnchantmentPlagueBringer;
import ovh.corail.tombstone.enchantment.EnchantmentShadowStep;
import ovh.corail.tombstone.enchantment.EnchantmentSoulBound;

import static ovh.corail.tombstone.ModTombstone.MOD_ID;

@ObjectHolder(MOD_ID)
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEnchantments extends Registrable {
    public static final Enchantment shadow_step = new EnchantmentShadowStep();
    public static final Enchantment soulbound = new EnchantmentSoulBound();
    public static final Enchantment magic_siphon = new EnchantmentMagicSiphon();
    public static final Enchantment plague_bringer = new EnchantmentPlagueBringer();

    @SubscribeEvent
    public static void registerEnchantment(final RegistryEvent.Register<Enchantment> event) {
        registerForgeEntry(event.getRegistry(), shadow_step, "shadow_step");
        registerForgeEntry(event.getRegistry(), soulbound, "soulbound");
        registerForgeEntry(event.getRegistry(), magic_siphon, "magic_siphon");
        registerForgeEntry(event.getRegistry(), plague_bringer, "plague_bringer");
    }
}
