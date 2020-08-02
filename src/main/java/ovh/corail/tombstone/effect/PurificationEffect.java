package ovh.corail.tombstone.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import ovh.corail.tombstone.helper.EffectHelper;
import ovh.corail.tombstone.helper.TimeHelper;

public class PurificationEffect extends Effect {

    public PurificationEffect() {
        super(EffectType.BENEFICIAL, 16777113);
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {
        EffectHelper.clearBadEffects(entity);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return TimeHelper.atInterval(duration, 10);
    }
}
