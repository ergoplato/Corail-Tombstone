package ovh.corail.tombstone.compatibility;

import lain.mods.cos.api.event.CosArmorDeathDrops;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ovh.corail.tombstone.config.ConfigTombstone;

@SuppressWarnings("unused")
public class CompatibilityCosmeticArmor {
    public static final CompatibilityCosmeticArmor instance = new CompatibilityCosmeticArmor();

    private CompatibilityCosmeticArmor() {
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void keepSkinArmorEvent(CosArmorDeathDrops event) {
        if (ConfigTombstone.compatibility.keepCosmeticArmor.get()) {
            event.setCanceled(true);
        }
    }
}
