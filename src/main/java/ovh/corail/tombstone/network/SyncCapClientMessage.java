package ovh.corail.tombstone.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;
import ovh.corail.tombstone.api.capability.Perk;
import ovh.corail.tombstone.capability.TBCapabilityProvider;
import ovh.corail.tombstone.helper.Helper;
import ovh.corail.tombstone.perk.PerkRegistry;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Supplier;

public class SyncCapClientMessage {
    public enum SyncType {
        ALL, KNOWLEDGE, PERKS, SET_PERK, REMOVE_PERK, ALIGNMENT
    }

    private SyncType syncType;
    private long knowledge;
    private HashMap<Perk, Integer> perks;
    private Perk perk;
    private int level;
    private int alignment;

    @SuppressWarnings("unchecked")
    public SyncCapClientMessage(SyncType syncType, Object... params) {
        this.syncType = syncType;
        if (syncType == SyncType.ALL) {
            this.knowledge = params.length > 0 ? (long) params[0] : 0;
            this.alignment = params.length > 1 ? (int) params[1] : 0;
            this.perks = params.length > 2 ? (HashMap<Perk, Integer>) params[2] : new HashMap<>();
        } else if (syncType == SyncType.KNOWLEDGE) {
            this.knowledge = params.length > 0 ? (long) params[0] : 0;
        } else if (syncType == SyncType.ALIGNMENT) {
            this.alignment = params.length > 0 ? (int) params[0] : 0;
        } else if (syncType == SyncType.PERKS) {
            this.perks = params.length > 0 ? (HashMap<Perk, Integer>) params[0] : new HashMap<>();
        } else if (syncType == SyncType.SET_PERK) {
            this.perk = params.length > 0 ? (Perk) params[0] : null;
            this.level = params.length > 1 ? (int) params[1] : 1;
        } else if (syncType == SyncType.REMOVE_PERK) {
            this.perk = params.length > 0 ? (Perk) params[0] : null;
        }
    }

    static SyncCapClientMessage fromBytes(PacketBuffer buf) {
        SyncType syncType = SyncType.values()[buf.readShort()];
        switch (syncType) {
            case KNOWLEDGE:
                return new SyncCapClientMessage(syncType, buf.readLong());
            case ALIGNMENT:
                return new SyncCapClientMessage(syncType, buf.readInt());
            case ALL:
                return new SyncCapClientMessage(syncType, buf.readLong(), buf.readInt(), readPerks(buf.readCompoundTag()));
            case PERKS:
                return new SyncCapClientMessage(syncType, readPerks(buf.readCompoundTag()));
            case SET_PERK:
                return new SyncCapClientMessage(syncType, PerkRegistry.perkRegistry.getValue(buf.readInt()), buf.readInt());
            case REMOVE_PERK:
                return new SyncCapClientMessage(syncType, PerkRegistry.perkRegistry.getValue(buf.readInt()));
            default:
                return new SyncCapClientMessage(syncType, buf.readLong(), buf.readLong());
        }
    }

    private static HashMap<Perk, Integer> readPerks(CompoundNBT tag) {
        HashMap<Perk, Integer> perks = new HashMap<>();
        if (tag != null) {
            if (tag.contains("perks", Constants.NBT.TAG_LIST)) {
                ListNBT tagPerks = tag.getList("perks", Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < tagPerks.size(); i++) {
                    CompoundNBT tagPerk = tagPerks.getCompound(i);
                    if (tagPerk.contains("id", Constants.NBT.TAG_INT) && tagPerk.contains("level", Constants.NBT.TAG_INT)) {
                        Perk perk = PerkRegistry.perkRegistry.getValue(tagPerk.getInt("id"));
                        if (perk != null) {
                            perks.put(perk, tagPerk.getInt("level"));
                        }
                    }
                }
            }
        }
        return perks;
    }

    static void toBytes(SyncCapClientMessage msg, PacketBuffer buf) {
        buf.writeShort(msg.syncType.ordinal());
        boolean isSyncAll = msg.syncType == SyncType.ALL;
        if (isSyncAll || msg.syncType == SyncType.KNOWLEDGE) {
            buf.writeLong(msg.knowledge);
        }
        if (isSyncAll || msg.syncType == SyncType.ALIGNMENT) {
            buf.writeInt(msg.alignment);
        }
        if (isSyncAll || msg.syncType == SyncType.PERKS) {
            CompoundNBT tag = new CompoundNBT();
            ListNBT tagPerks = new ListNBT();
            for (Entry<Perk, Integer> entry : msg.perks.entrySet()) {
                CompoundNBT tagPerk = new CompoundNBT();
                tagPerk.putInt("id", PerkRegistry.perkRegistry.getID(entry.getKey()));
                tagPerk.putInt("level", entry.getValue());
                tagPerks.add(tagPerk);
            }
            tag.put("perks", tagPerks);
            buf.writeCompoundTag(tag);
        }
        if (msg.syncType == SyncType.SET_PERK) {
            buf.writeInt(PerkRegistry.perkRegistry.getID(msg.perk));
            buf.writeInt(msg.level);
        }
        if (msg.syncType == SyncType.REMOVE_PERK) {
            buf.writeInt(PerkRegistry.perkRegistry.getID(msg.perk));
        }
    }

    public static class Handler {
        static void handle(final SyncCapClientMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context ctx = contextSupplier.get();
            if (Helper.isPacketToClient(ctx)) {
                ctx.enqueueWork(new Runnable() {
                    @Override
                    public void run() {
                        ClientPlayerEntity player = Minecraft.getInstance().player;
                        if (player != null) {
                            player.getCapability(TBCapabilityProvider.TB_CAPABILITY, null).ifPresent(cap -> {
                                switch (message.syncType) {
                                    case ALL:
                                        cap.setKnowledge(message.knowledge);
                                        cap.setAlignment(message.alignment);
                                        cap.setPerks(message.perks);
                                        break;
                                    case KNOWLEDGE:
                                        cap.setKnowledge(message.knowledge);
                                        break;
                                    case PERKS:
                                        cap.setPerks(message.perks);
                                        break;
                                    case SET_PERK:
                                        cap.setPerk(message.perk, message.level);
                                        break;
                                    case REMOVE_PERK:
                                        cap.removePerk(message.perk);
                                        break;
                                    case ALIGNMENT:
                                        cap.setAlignment(message.alignment);
                                        break;
                                    default:
                                        break;
                                }
                            });
                        }
                    }
                });
            }
            ctx.setPacketHandled(true);
        }
    }
}
