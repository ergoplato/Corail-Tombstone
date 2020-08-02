package ovh.corail.tombstone.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import ovh.corail.tombstone.config.ConfigTombstone;
import ovh.corail.tombstone.helper.Helper;
import ovh.corail.tombstone.helper.SpawnProtectionHandler;

import java.util.function.Supplier;

public class UpdateClientMessage {
    private BlockPos spawnPos;
    private int range;
    private boolean dateAroundHalloween, isContributor;

    public UpdateClientMessage(BlockPos spawnPos, int range, boolean dateAroundHalloween, boolean isContributor) {
        this.spawnPos = spawnPos;
        this.range = range;
        this.dateAroundHalloween = dateAroundHalloween;
        this.isContributor = isContributor;
    }

    static UpdateClientMessage fromBytes(PacketBuffer buf) {
        return new UpdateClientMessage(BlockPos.fromLong(buf.readLong()), buf.readInt(), buf.readBoolean(), buf.readBoolean());
    }

    static void toBytes(UpdateClientMessage msg, PacketBuffer buf) {
        buf.writeLong(msg.spawnPos.toLong());
        buf.writeInt(msg.range);
        buf.writeBoolean(msg.dateAroundHalloween);
        buf.writeBoolean(msg.isContributor);
    }

    public static class Handler {
        static void handle(final UpdateClientMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context ctx = contextSupplier.get();
            if (Helper.isPacketToClient(ctx)) {
                ctx.enqueueWork(() -> {
                    SpawnProtectionHandler.getInstance().setSpawnProtection(message.spawnPos, message.range);
                    Helper.isHalloween = message.dateAroundHalloween;
                    Helper.isContributor = message.isContributor;
                    PacketHandler.sendToServer(new UpdateServerMessage(ConfigTombstone.client.favoriteGrave.get(), ConfigTombstone.client.favoriteGraveMarble.get(), ConfigTombstone.client.equipElytraInPriority.get(), ConfigTombstone.client.displayKnowledgeMessage.get(), ConfigTombstone.client.priorizeToolOnHotbar.get(), ConfigTombstone.client.activateGraveBySneaking.get(), true));
                });
            }
            ctx.setPacketHandled(true);
        }
    }
}
