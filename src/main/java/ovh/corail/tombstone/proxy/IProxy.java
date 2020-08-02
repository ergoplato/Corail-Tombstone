package ovh.corail.tombstone.proxy;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.net.Proxy;
import java.util.function.Predicate;

public interface IProxy {
    void preInit();

    void produceGraveSmoke(World world, double x, double y, double z);

    void produceShadowStep(LivingEntity entity);

    void produceGraveSoul(World world, BlockPos pos);

    void produceParticleCasting(LivingEntity caster, Predicate<LivingEntity> predic);

    void produceSmokeColumn(World world, double x, double y, double z);

    Proxy getNetProxy();

    void markConfigDirty();
}
