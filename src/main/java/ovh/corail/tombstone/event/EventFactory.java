package ovh.corail.tombstone.event;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import ovh.corail.tombstone.api.event.CaptureSoulEvent;
import ovh.corail.tombstone.api.event.RestoreInventoryEvent;
import ovh.corail.tombstone.api.event.VillageSiegeEvent;
import ovh.corail.tombstone.tileentity.TileEntityGrave;

public class EventFactory {
    public static boolean onVillageSiegeStart(BlockPos spawnPos) {
        return MinecraftForge.EVENT_BUS.post(new VillageSiegeEvent.Start(spawnPos));
    }

    public static boolean onVillageSiegeZombieSpawn(ZombieEntity entity) {
        return MinecraftForge.EVENT_BUS.post(new VillageSiegeEvent.SpawnZombie(entity));
    }

    public static void onRestoreInventory(PlayerEntity player, TileEntityGrave tileGrave) {
        MinecraftForge.EVENT_BUS.post(new RestoreInventoryEvent(player, tileGrave.getPos(), tileGrave.getInventory(), tileGrave.getOwnerName(), tileGrave.getOwnerDeathTime()));
    }

    public static boolean onCaptureSoul(PlayerEntity player, LivingEntity capturedEntity) {
        return MinecraftForge.EVENT_BUS.post(new CaptureSoulEvent(player, capturedEntity));
    }
}
