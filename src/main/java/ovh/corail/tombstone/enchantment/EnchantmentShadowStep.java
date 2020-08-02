package ovh.corail.tombstone.enchantment;

import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.DamageSource;
import ovh.corail.tombstone.config.SharedConfigTombstone;

public class EnchantmentShadowStep extends TombstoneEnchantment {

    public EnchantmentShadowStep() {
        super("shadow_step", Rarity.RARE, EnchantmentType.ARMOR_FEET, new EquipmentSlotType[] { EquipmentSlotType.FEET });
    }

    @Override
    protected boolean isEnabled() {
        return SharedConfigTombstone.enchantments.enableEnchantmentShadowStep.get();
    }

    @Override
    public int getMinEnchantability(int lvl) {
        return 1 + ((lvl - 1) * 10);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int calcModifierDamage(int lvl, DamageSource source) {
        if (!source.canHarmInCreative() && source == DamageSource.FALL) {
            return lvl * 3;
        }
        return 0;
    }
}
