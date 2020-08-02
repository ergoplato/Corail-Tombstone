package ovh.corail.tombstone.registry;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import ovh.corail.tombstone.particle.data.ParticleDataTwoInt;

public class ModParticleTypes {
    public static final BasicParticleType GHOST = new BasicParticleType(false);
    public static final BasicParticleType GRAVE_SMOKE = new BasicParticleType(false);
    public static final BasicParticleType ROTATING_SMOKE = new BasicParticleType(false);
    public static final BasicParticleType SMOKE_COLUMN = new BasicParticleType(false);
    public static final ParticleType<ParticleDataTwoInt> BLINKING_SMOKE = new ParticleType<>(false, ParticleDataTwoInt.DESERIALIZER);
    public static final BasicParticleType GRAVE_SOUL = new BasicParticleType(false);
}
