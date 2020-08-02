package ovh.corail.tombstone.registry;

import net.minecraft.client.Minecraft;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ovh.corail.tombstone.particle.ParticleBlinkingAura;
import ovh.corail.tombstone.particle.ParticleGhost;
import ovh.corail.tombstone.particle.ParticleGraveSmoke;
import ovh.corail.tombstone.particle.ParticleGraveSoul;
import ovh.corail.tombstone.particle.ParticleRotatingSmoke;
import ovh.corail.tombstone.particle.ParticleSmokeColumn;

import static ovh.corail.tombstone.ModTombstone.MOD_ID;

public class RegistryHandler {
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Common {
        @SubscribeEvent
        public static void registerParticleTypes(RegistryEvent.Register<ParticleType<?>> event) {
            ModParticleTypes.GHOST.setRegistryName(MOD_ID, "ghost");
            event.getRegistry().register(ModParticleTypes.GHOST);
            ModParticleTypes.GRAVE_SMOKE.setRegistryName(MOD_ID, "grave_smoke");
            event.getRegistry().register(ModParticleTypes.GRAVE_SMOKE);
            ModParticleTypes.ROTATING_SMOKE.setRegistryName(MOD_ID, "rotating_smoke");
            event.getRegistry().register(ModParticleTypes.ROTATING_SMOKE);
            ModParticleTypes.SMOKE_COLUMN.setRegistryName(MOD_ID, "smoke_column");
            event.getRegistry().register(ModParticleTypes.SMOKE_COLUMN);
            ModParticleTypes.BLINKING_SMOKE.setRegistryName(MOD_ID, "blinking_smoke");
            event.getRegistry().register(ModParticleTypes.BLINKING_SMOKE);
            ModParticleTypes.GRAVE_SOUL.setRegistryName(MOD_ID, "grave_soul");
            event.getRegistry().register(ModParticleTypes.GRAVE_SOUL);
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Client {
        @SubscribeEvent(priority = EventPriority.LOW)
        public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
            Minecraft.getInstance().particles.registerFactory(ModParticleTypes.GHOST, ParticleGhost.Factory::new);
            Minecraft.getInstance().particles.registerFactory(ModParticleTypes.GRAVE_SMOKE, ParticleGraveSmoke.Factory::new);
            Minecraft.getInstance().particles.registerFactory(ModParticleTypes.ROTATING_SMOKE, ParticleRotatingSmoke.Factory::new);
            Minecraft.getInstance().particles.registerFactory(ModParticleTypes.SMOKE_COLUMN, new ParticleSmokeColumn.Factory());
            Minecraft.getInstance().particles.registerFactory(ModParticleTypes.BLINKING_SMOKE, ParticleBlinkingAura.Factory::new);
            Minecraft.getInstance().particles.registerFactory(ModParticleTypes.GRAVE_SOUL, ParticleGraveSoul.Factory::new);
        }
    }
}
