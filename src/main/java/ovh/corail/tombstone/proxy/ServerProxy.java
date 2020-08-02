package ovh.corail.tombstone.proxy;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ovh.corail.tombstone.config.SharedConfigTombstone;
import ovh.corail.tombstone.helper.Helper;
import ovh.corail.tombstone.network.PacketHandler;

import java.net.Proxy;
import java.util.function.Predicate;

import static ovh.corail.tombstone.ModTombstone.LOGGER;

public class ServerProxy implements IProxy {
    private boolean isConfigDirty = false;

    @Override
    public void preInit() {
        // only register the event on dedicated server
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void produceGraveSmoke(World world, double x, double y, double z) {
    }

    @Override
    public void produceShadowStep(LivingEntity entity) {
    }

    @Override
    public void produceGraveSoul(World world, BlockPos pos) {
    }

    @Override
    public void produceParticleCasting(LivingEntity caster, Predicate<LivingEntity> predic) {
    }

    @Override
    public void produceSmokeColumn(World world, double x, double y, double z) {
    }

    @Override
    public Proxy getNetProxy() {
        return Proxy.NO_PROXY;
    }

    @Override
    public void markConfigDirty() {
        this.isConfigDirty = true;
    }

    @SuppressWarnings("unused")
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (this.isConfigDirty && event.phase == TickEvent.Phase.END) {
            this.isConfigDirty = false;
            MinecraftServer server = Helper.getServer();
            if (server != null && !server.getPlayerList().getPlayers().isEmpty()) {
                LOGGER.info("Syncing Config on Client");
                PacketHandler.sendToAllPlayers(SharedConfigTombstone.getUpdatePacket());
            }
        }
    }
}
