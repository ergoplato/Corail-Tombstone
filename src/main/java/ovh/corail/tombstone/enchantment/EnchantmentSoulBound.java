package ovh.corail.tombstone.enchantment;

import net.minecraft.inventory.EquipmentSlotType;
import ovh.corail.tombstone.ModTombstone;
import ovh.corail.tombstone.config.SharedConfigTombstone;

public class EnchantmentSoulBound extends TombstoneEnchantment {

    public EnchantmentSoulBound() {
        super("soulbound", Rarity.RARE, ModTombstone.TYPE_TOMBSTONE_ALL, EquipmentSlotType.values());
    }

    @Override
    protected boolean isEnabled() {
        return SharedConfigTombstone.enchantments.enableEnchantmentSoulbound.get();
    }
}
