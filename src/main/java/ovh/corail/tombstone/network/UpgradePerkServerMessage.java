package ovh.corail.tombstone.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import ovh.corail.tombstone.api.capability.Perk;
import ovh.corail.tombstone.capability.TBCapabilityProvider;
import ovh.corail.tombstone.helper.Helper;
import ovh.corail.tombstone.perk.PerkRegistry;
import ovh.corail.tombstone.registry.ModTriggers;

import java.util.function.Supplier;

public class UpgradePerkServerMessage {
    public enum SyncType {
        UPGRADE_PERK, DOWNGRADE_PERK
    }

    private SyncType syncType;
    private Perk perk;

    public UpgradePerkServerMessage(SyncType syncType, Perk perk) {
        this.syncType = syncType;
        this.perk = perk;
    }

    static UpgradePerkServerMessage fromBytes(PacketBuffer buf) {
        return new UpgradePerkServerMessage(SyncType.values()[buf.readShort()], PerkRegistry.perkRegistry.getValue(buf.readInt()));
    }

    static void toBytes(UpgradePerkServerMessage msg, PacketBuffer buf) {
        buf.writeShort(msg.syncType.ordinal());
        buf.writeInt(PerkRegistry.perkRegistry.getID(msg.perk));
    }

    public static class Handler {
        static void handle(final UpgradePerkServerMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context ctx = contextSupplier.get();
            if (Helper.isPacketToServer(ctx)) {
                ctx.enqueueWork(() -> {
                    ServerPlayerEntity player = ctx.getSender();
                    if (player != null) {
                        player.getCapability(TBCapabilityProvider.TB_CAPABILITY, null).ifPresent(cap -> {
                            int levelPerk = cap.getPerkLevel(player, message.perk);
                            if (message.syncType == SyncType.UPGRADE_PERK && levelPerk < message.perk.getLevelMax() && (cap.getTotalPerkPoints() - cap.getUsedPerkPoints(player)) >= message.perk.getCost(levelPerk + 1)) {
                                cap.setPerk(message.perk, levelPerk + 1);
                                if (levelPerk == 0) {
                                    ModTriggers.CHOOSE_KNOWLEDGE.trigger(player);
                                }
                                PacketHandler.sendTo(PacketDistributor.PLAYER.with(() -> player), new SyncCapClientMessage(SyncCapClientMessage.SyncType.SET_PERK, message.perk, levelPerk + 1));
                            } else if (message.syncType == SyncType.DOWNGRADE_PERK && levelPerk > 0) {
                                cap.setPerk(message.perk, levelPerk - 1);
                                PacketHandler.sendTo(PacketDistributor.PLAYER.with(() -> player), new SyncCapClientMessage(SyncCapClientMessage.SyncType.SET_PERK, message.perk, levelPerk - 1));
                            }
                        });
                    }
                });
            }
            ctx.setPacketHandled(true);
        }
    }
}
