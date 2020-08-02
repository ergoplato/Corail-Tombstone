package ovh.corail.tombstone.network;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import ovh.corail.tombstone.ModTombstone;
import ovh.corail.tombstone.api.TombstoneAPIProps;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = ModTombstone.MOD_ID + "-" + TombstoneAPIProps.OWNER_VERSION;
    private static final SimpleChannel HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(ModTombstone.MOD_ID, "tombstone_channel"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    static {
        int id = -1;
        HANDLER.registerMessage(++id, TombstoneActivatedMessage.class, TombstoneActivatedMessage::toBytes, TombstoneActivatedMessage::fromBytes, TombstoneActivatedMessage.Handler::handle);
        HANDLER.registerMessage(++id, UpdateServerMessage.class, UpdateServerMessage::toBytes, UpdateServerMessage::fromBytes, UpdateServerMessage.Handler::handle);
        HANDLER.registerMessage(++id, UpdateClientMessage.class, UpdateClientMessage::toBytes, UpdateClientMessage::fromBytes, UpdateClientMessage.Handler::handle);
        HANDLER.registerMessage(++id, SyncCapClientMessage.class, SyncCapClientMessage::toBytes, SyncCapClientMessage::fromBytes, SyncCapClientMessage.Handler::handle);
        HANDLER.registerMessage(++id, UpgradePerkServerMessage.class, UpgradePerkServerMessage::toBytes, UpgradePerkServerMessage::fromBytes, UpgradePerkServerMessage.Handler::handle);
        HANDLER.registerMessage(++id, SmokeColumnMessage.class, SmokeColumnMessage::toBytes, SmokeColumnMessage::fromBytes, SmokeColumnMessage.Handler::handle);
        HANDLER.registerMessage(++id, UpdateConfigMessage.class, UpdateConfigMessage::toBytes, UpdateConfigMessage::fromBytes, UpdateConfigMessage.Handler::handle);
        HANDLER.registerMessage(++id, SyncCooldownMessage.class, SyncCooldownMessage::toBytes, SyncCooldownMessage::fromBytes, SyncCooldownMessage.Handler::handle);
        HANDLER.registerMessage(++id, EffectMessage.class, EffectMessage::toBytes, EffectMessage::fromBytes, EffectMessage.Handler::handle);
    }

    public static <T> void sendTo(PacketDistributor.PacketTarget target, T message) {
        HANDLER.send(target, message);
    }

    public static <T> void sendToServer(T message) {
        HANDLER.sendToServer(message);
    }

    public static <T> void sendToPlayer(T message, ServerPlayerEntity playerMP) {
        HANDLER.send(PacketDistributor.PLAYER.with(() -> playerMP), message);
    }

    public static <T> void sendToAllTrackingPlayers(T message, LivingEntity entity) {
        HANDLER.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }

    public static <T> void sendToAllPlayers(T message) {
        HANDLER.send(PacketDistributor.ALL.noArg(), message);
    }
}
