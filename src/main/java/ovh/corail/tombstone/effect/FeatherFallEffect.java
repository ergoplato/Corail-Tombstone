package ovh.corail.tombstone.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class FeatherFallEffect extends Effect {

    public FeatherFallEffect() {
        super(EffectType.BENEFICIAL, 15594746);
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {
        performPotionLogic(entity);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    public static void performPotionLogic(LivingEntity entity) {
        if (!entity.isSneaking() && entity.getMotion().y < -0.079d) {
            entity.setMotion(entity.getMotion().mul(1d, 0.7d, 1d));
            entity.fallDistance = 0f;
        }
    }
}
