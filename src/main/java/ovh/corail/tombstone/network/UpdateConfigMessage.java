package ovh.corail.tombstone.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import ovh.corail.tombstone.config.SharedConfigTombstone;
import ovh.corail.tombstone.helper.Helper;

import java.util.BitSet;
import java.util.function.Supplier;

public class UpdateConfigMessage {
    private BitSet boolConfigs;
    private int[] intConfigs;

    public UpdateConfigMessage(BitSet boolConfigs, int[] intConfigs) {
        this.boolConfigs = boolConfigs;
        this.intConfigs = intConfigs;
    }

    static UpdateConfigMessage fromBytes(PacketBuffer buf) {
        return new UpdateConfigMessage(BitSet.valueOf(buf.readByteArray()), buf.readVarIntArray());
    }

    static void toBytes(UpdateConfigMessage msg, PacketBuffer buf) {
        buf.writeByteArray(msg.boolConfigs.toByteArray());
        buf.writeVarIntArray(msg.intConfigs);
    }

    public static class Handler {
        static void handle(final UpdateConfigMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context ctx = contextSupplier.get();
            if (Helper.isPacketToClient(ctx)) {
                ctx.enqueueWork(() -> SharedConfigTombstone.updateConfig(message.boolConfigs, message.intConfigs));
            }
            ctx.setPacketHandled(true);
        }
    }
}
