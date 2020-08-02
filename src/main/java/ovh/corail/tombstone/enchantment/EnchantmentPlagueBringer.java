package ovh.corail.tombstone.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import ovh.corail.tombstone.config.ConfigTombstone;
import ovh.corail.tombstone.config.SharedConfigTombstone;
import ovh.corail.tombstone.helper.EffectHelper;
import ovh.corail.tombstone.helper.EntityHelper;
import ovh.corail.tombstone.helper.Helper;
import ovh.corail.tombstone.helper.TimeHelper;
import ovh.corail.tombstone.registry.ModEnchantments;

public class EnchantmentPlagueBringer extends WeaponEnchantment {
    public EnchantmentPlagueBringer() {
        super("plague_bringer", Rarity.RARE, EnchantmentType.WEAPON, new EquipmentSlotType[] { EquipmentSlotType.MAINHAND });
    }

    @Override
    protected boolean isEnabled() {
        return SharedConfigTombstone.enchantments.enableEnchantmentPlagueBringer.get();
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return super.canApplyTogether(ench) && (SharedConfigTombstone.enchantments.allowPlagueBringerCombiningMagicSiphon.get() || ench != ModEnchantments.magic_siphon);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return SharedConfigTombstone.enchantments.plagueBringerEnchantingTable.get() && super.canApplyAtEnchantingTable(stack);
    }

    @Override
    public int getMaxLevel() {
        return SharedConfigTombstone.enchantments.maxLevelPlagueBringer.get();
    }

    @Override
    public void onProc(LivingEntity attacker, LivingEntity target, int level) {
        EffectInstance effect = EffectHelper.getRandomEffect(TimeHelper.tickFromSecond(ConfigTombstone.enchantments.durationPlagueBringer.get()), true);
        if (effect != null) {
            target.addPotionEffect(effect);
            boolean canAffectAttacker = ConfigTombstone.enchantments.nerfPlagueBringer.get() && (!EntityHelper.isValidPlayerMP(attacker) || (!Helper.isContributor((ServerPlayerEntity) attacker) && !EntityHelper.isBadAlignment((ServerPlayerEntity) attacker)));
            if (canAffectAttacker) {
                attacker.addPotionEffect(new EffectInstance(effect));
            }
        }
    }
}
