package ovh.corail.tombstone.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.network.NetworkEvent;
import ovh.corail.tombstone.helper.EffectHelper;
import ovh.corail.tombstone.helper.Helper;

import java.util.function.Supplier;

public class EffectMessage {
    private int entityId;
    private EffectInstance effectInstance;

    public EffectMessage(int entityId, EffectInstance effectInstance) {
        this.entityId = entityId;
        this.effectInstance = effectInstance;
    }

    static EffectMessage fromBytes(PacketBuffer buf) {
        int id = buf.readInt();
        CompoundNBT effect = buf.readCompoundTag();
        return new EffectMessage(id, effect == null ? new EffectInstance(Effects.SATURATION) : EffectInstance.read(effect));
    }

    static void toBytes(EffectMessage msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
        buf.writeCompoundTag(msg.effectInstance.write(new CompoundNBT()));
    }

    public static class Handler {
        static void handle(final EffectMessage msg, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context ctx = contextSupplier.get();
            if (Helper.isPacketToClient(ctx)) {
                ctx.enqueueWork(new Runnable() {
                    @Override
                    public void run() {
                        Entity entity = Minecraft.getInstance().world != null ? Minecraft.getInstance().world.getEntityByID(msg.entityId) : null;
                        if (entity instanceof LivingEntity) {
                            EffectHelper.addEffect((LivingEntity) entity, msg.effectInstance);
                        }
                    }
                });
            }
            ctx.setPacketHandled(true);
        }
    }
}
