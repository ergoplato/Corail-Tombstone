package ovh.corail.tombstone.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.DamageSource;
import org.apache.commons.lang3.tuple.Triple;
import ovh.corail.tombstone.helper.EntityHelper;
import ovh.corail.tombstone.helper.Helper;

import java.util.UUID;

public abstract class WeaponEnchantment extends TombstoneEnchantment {
    WeaponEnchantment(String name, Rarity rarity, EnchantmentType type, EquipmentSlotType[] slots) {
        super(name, rarity, type, slots);
    }

    private Triple<UUID, UUID, Long> lastEntityUUID = null;

    @Override
    public void onEntityDamaged(LivingEntity attacker, Entity entity, int level) {
        if (this.lastEntityUUID != null && this.lastEntityUUID.equals(Triple.of(attacker.getUniqueID(), entity.getUniqueID(), attacker.world.getGameTime()))) {
            return;
        }
        this.lastEntityUUID = Triple.of(attacker.getUniqueID(), entity.getUniqueID(), attacker.world.getGameTime());
        if (entity instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) entity;
            if (target.isAlive() && isMainHand(attacker, target)) {
                int lvl = EnchantmentHelper.getEnchantmentLevel(this, attacker.getHeldItemMainhand());
                if (lvl > 0 && testProc(attacker, target, lvl)) {
                    onProc(attacker, target, lvl);
                }
            }
        }
    }

    @SuppressWarnings({ "unused", "WeakerAccess" })
    protected boolean testProc(LivingEntity attacker, LivingEntity target, int level) {
        return Helper.random.nextInt(10) < level;
    }

    private boolean isMainHand(LivingEntity attacker, LivingEntity target) {
        DamageSource lastDamage;
        return EntityHelper.isValidPlayer(attacker) ? attacker.ticksSinceLastSwing == 0 : (lastDamage = target.getLastDamageSource()) != null && !lastDamage.isProjectile();
    }

    protected abstract void onProc(LivingEntity attacker, LivingEntity target, int level);
}
