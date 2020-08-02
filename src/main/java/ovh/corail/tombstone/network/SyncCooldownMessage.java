package ovh.corail.tombstone.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;
import ovh.corail.tombstone.api.cooldown.CooldownType;
import ovh.corail.tombstone.helper.CooldownHandler;
import ovh.corail.tombstone.helper.Helper;

import java.util.function.Supplier;

import static ovh.corail.tombstone.helper.CooldownHandler.COOLDOWNS_NBT_LIST;

public class SyncCooldownMessage {
    private boolean isFullSync;
    private CooldownType type;
    private long worldTime;
    private ListNBT cooldownTagList;

    public SyncCooldownMessage(CooldownType type, long worldTime) {
        this.isFullSync = false;
        this.type = type;
        this.worldTime = worldTime;
    }

    public SyncCooldownMessage(ListNBT cooldownTagList) {
        this.isFullSync = true;
        this.cooldownTagList = cooldownTagList;
    }

    static SyncCooldownMessage fromBytes(PacketBuffer buf) {
        if (buf.readBoolean()) {
            CompoundNBT compound = buf.readCompoundTag();
            return new SyncCooldownMessage(compound != null ? compound.getList(COOLDOWNS_NBT_LIST, Constants.NBT.TAG_COMPOUND) : new ListNBT());
        }
        return new SyncCooldownMessage(CooldownType.values()[buf.readByte()], buf.readLong());
    }

    static void toBytes(SyncCooldownMessage msg, PacketBuffer buf) {
        buf.writeBoolean(msg.isFullSync);
        if (msg.isFullSync) {
            CompoundNBT compound = new CompoundNBT();
            compound.put(COOLDOWNS_NBT_LIST, msg.cooldownTagList);
            buf.writeCompoundTag(compound);
        } else {
            buf.writeByte(msg.type.ordinal());
            buf.writeLong(msg.worldTime);
        }
    }

    public static class Handler {
        static void handle(final SyncCooldownMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context ctx = contextSupplier.get();
            if (Helper.isPacketToClient(ctx)) {
                ctx.enqueueWork(() -> {
                    if (message.isFullSync) {
                        CooldownHandler.INSTANCE.updateAllClientCooldowns(message.cooldownTagList);
                    } else {
                        CooldownHandler.INSTANCE.updateClientCooldown(message.type, message.worldTime);
                    }
                });
            }
            ctx.setPacketHandled(true);
        }
    }
}