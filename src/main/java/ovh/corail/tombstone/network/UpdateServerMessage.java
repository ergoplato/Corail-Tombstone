package ovh.corail.tombstone.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import ovh.corail.tombstone.block.BlockGraveMarble.MarbleType;
import ovh.corail.tombstone.block.GraveModel;
import ovh.corail.tombstone.helper.DeathHandler;
import ovh.corail.tombstone.helper.Helper;
import ovh.corail.tombstone.registry.ModTriggers;

import java.util.function.Supplier;

public class UpdateServerMessage {
    private GraveModel graveModel;
    private MarbleType marbleType;
    private boolean equipElytraInPriority, displayKnowledgeMessage, priorizeToolOnHotbar, activateGraveBySneaking, isLogin;

    public UpdateServerMessage(GraveModel graveModel, MarbleType marbleType, boolean equipElytraInPriority, boolean displayKnowledgeMessage, boolean priorizeToolOnHotbar, boolean activateGraveBySneaking, boolean isLogin) {
        this.graveModel = graveModel;
        this.marbleType = marbleType;
        this.equipElytraInPriority = equipElytraInPriority;
        this.displayKnowledgeMessage = displayKnowledgeMessage;
        this.priorizeToolOnHotbar = priorizeToolOnHotbar;
        this.activateGraveBySneaking = activateGraveBySneaking;
        this.isLogin = isLogin;
    }

    static UpdateServerMessage fromBytes(PacketBuffer buf) {
        return new UpdateServerMessage(GraveModel.byId(buf.readInt()), MarbleType.byId(buf.readInt()), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }

    static void toBytes(UpdateServerMessage msg, PacketBuffer buf) {
        buf.writeInt(msg.graveModel.ordinal());
        buf.writeInt(msg.marbleType.ordinal());
        buf.writeBoolean(msg.equipElytraInPriority);
        buf.writeBoolean(msg.displayKnowledgeMessage);
        buf.writeBoolean(msg.priorizeToolOnHotbar);
        buf.writeBoolean(msg.activateGraveBySneaking);
        buf.writeBoolean(msg.isLogin);
    }

    public static class Handler {
        static void handle(final UpdateServerMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context ctx = contextSupplier.get();
            if (Helper.isPacketToServer(ctx)) {
                ctx.enqueueWork(() -> {
                    ServerPlayerEntity player = ctx.getSender();
                    if (player != null) {
                        DeathHandler.INSTANCE
                                .setFavoriteGrave(player.getUniqueID(), !message.graveModel.isOnlyContributor() || Helper.isContributor(player) ? message.graveModel : GraveModel.getDefault(), message.marbleType)
                                .setOptionEquipElytraInPriority(player.getUniqueID(), message.equipElytraInPriority)
                                .setOptionKnowledgeMessage(player.getUniqueID(), message.displayKnowledgeMessage)
                                .setOptionPriorizeToolOnHotbar(player.getUniqueID(), message.priorizeToolOnHotbar)
                                .setOptionActivateGraveBySneaking(player.getUniqueID(), message.activateGraveBySneaking)
                        ;
                        if (!message.isLogin) {
                            ModTriggers.CHOOSE_GRAVE_TYPE.trigger(player);
                        }
                    }
                });
            }
            ctx.setPacketHandled(true);
        }
    }
}
