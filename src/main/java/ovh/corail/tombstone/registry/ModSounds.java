package ovh.corail.tombstone.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nullable;

import static ovh.corail.tombstone.ModTombstone.MOD_ID;

@ObjectHolder(MOD_ID)
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSounds {
    public static final SoundEvent MAGIC_USE01 = SoundEvents.BLOCK_CONDUIT_ACTIVATE;
    public static final SoundEvent GHOST_LAUGH = SoundEvents.BLOCK_CONDUIT_ACTIVATE;
    public static final SoundEvent GHOST_HOWL = SoundEvents.BLOCK_CONDUIT_ACTIVATE;

    public static void playSoundAllAround(@Nullable SoundEvent sound, SoundCategory cat, World world, BlockPos pos) {
        playSoundAllAround(sound, cat, world, pos, 1f, 1f);
    }

    public static void playSoundAllAround(@Nullable SoundEvent sound, SoundCategory cat, World world, BlockPos pos, float volume, float pitch) {
        if (world.isRemote || sound == null) {
            return;
        }
        world.playSound(null, pos, sound, cat, volume, pitch);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        registerForgeEntry(event.getRegistry(), "magic_use01");
        registerForgeEntry(event.getRegistry(), "ghost_laugh");
        registerForgeEntry(event.getRegistry(), "ghost_howl");
    }

    private static void registerForgeEntry(IForgeRegistry<SoundEvent> registry, String name) {
        ResourceLocation rl = new ResourceLocation(MOD_ID, name);
        registry.register(new SoundEvent(rl).setRegistryName(rl));
    }
}
