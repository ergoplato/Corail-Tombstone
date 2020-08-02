package ovh.corail.tombstone.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.SoundCategory;
import ovh.corail.tombstone.ModTombstone;
import ovh.corail.tombstone.helper.DummyTargetEntity;
import ovh.corail.tombstone.registry.ModSounds;

public class ExorcismEffect extends Effect {

    public ExorcismEffect() {
        super(EffectType.NEUTRAL, 526871);
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof ZombieVillagerEntity) {
            entity.setPosition(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ);
            ((ZombieVillagerEntity) entity).attackTarget = null;
            entity.revengeTarget = new DummyTargetEntity(entity.world);
            entity.setHealth(entity.getMaxHealth());
            entity.setMotion(entity.getMotion().x, 0.01d, entity.getMotion().z);
            ModTombstone.PROXY.produceShadowStep(entity);
            ModTombstone.PROXY.produceParticleCasting(entity, p -> true);
            if (entity.isBurning()) {
                entity.extinguish();
            }
            if (!entity.world.isRemote && entity.ticksExisted % 40 == 0) {
                ModSounds.playSoundAllAround(ModSounds.MAGIC_USE01, SoundCategory.PLAYERS, entity.world, entity.getPosition(), 0.5f, 0.5f + entity.world.rand.nextFloat() * 0.5f);
            }
        } else {
            entity.removePotionEffect(this);
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }
}
