package ovh.corail.tombstone.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import ovh.corail.tombstone.api.cooldown.CooldownType;
import ovh.corail.tombstone.api.cooldown.ICooldownHandler;
import ovh.corail.tombstone.config.ConfigTombstone;
import ovh.corail.tombstone.config.SharedConfigTombstone;
import ovh.corail.tombstone.network.PacketHandler;
import ovh.corail.tombstone.network.SyncCooldownMessage;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class CooldownHandler implements ICooldownHandler {
    public static final CooldownHandler INSTANCE = new CooldownHandler();
    private final Map<UUID, EnumMap<CooldownType, Long>> cooldowns = new HashMap<>();
    private EnumMap<CooldownType, Long> clientCooldowns = new EnumMap<>(CooldownType.class);

    private CooldownHandler() {
        CooldownType.NEXT_PRAY.setMaxCooldown(player -> TimeHelper.tickFromHour(SharedConfigTombstone.decorative_grave.cooldownToPray.get()) / (Helper.isContributor(player) ? 2 : 1));
        CooldownType.RESET_PERKS.setMaxCooldown(player -> TimeHelper.tickFromMinute(SharedConfigTombstone.decorative_grave.cooldownResetPerk.get()));
        CooldownType.TELEPORT_DEATH.setMaxCooldown(player -> TimeHelper.tickFromMinute(ConfigTombstone.general.cooldownTeleportDeath.get()));
        CooldownType.TELEPORT_BIND.setMaxCooldown(player -> TimeHelper.tickFromMinute(ConfigTombstone.general.cooldownTeleportBind.get()));
        CooldownType.REQUEST_TELEPORT.setMaxCooldown(player -> TimeHelper.tickFromMinute(ConfigTombstone.general.cooldownRequestTeleport.get()));
    }

    @Override
    public int getCooldown(PlayerEntity player, CooldownType type) {
        if (player.world.isRemote) {
            return getCooldown(type);
        }
        EnumMap<CooldownType, Long> entry = this.cooldowns.computeIfAbsent(player.getUniqueID(), aId -> computePlayerCd((ServerPlayerEntity) player));
        long worldTimeCD = entry.getOrDefault(type, -1L);
        if (worldTimeCD > 0) {
            long cooldown = worldTimeCD - TimeHelper.worldTicks(player.world);
            if (cooldown <= 0 && entry.remove(type) != null) {
                getCooldownTagList(player).removeIf(inbt -> FIND_BY_TYPE.test(inbt, type));
                return 0;
            }
            if (cooldown > type.getMaxCooldown(player)) {
                return resetCooldown((ServerPlayerEntity) player, type);
            }
            return Math.max((int) cooldown, 0);
        }
        return 0;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getCooldown(CooldownType type) {
        long cd = this.clientCooldowns.getOrDefault(type, -1L);
        ClientPlayerEntity player = Minecraft.getInstance().player;
        return player != null && cd > 0 ? Math.max(0, (int) (cd - TimeHelper.worldTicks(player.world))) : 0;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasCooldown(CooldownType type) {
        return getCooldown(type) > 0;
    }

    @Override
    public boolean hasCooldown(PlayerEntity player, CooldownType type) {
        return getCooldown(player, type) > 0;
    }

    public boolean noCooldown(PlayerEntity player, CooldownType type) {
        return getCooldown(player, type) <= 0;
    }

    @Override
    public int resetCooldown(ServerPlayerEntity player, CooldownType type) {
        if (!EntityHelper.isValidPlayerMP(player)) {
            return 0;
        }
        int maxCD = type.getMaxCooldown(player);
        setWorldTimeCooldown(player, type, TimeHelper.worldTicks(player.world) + maxCD);
        return maxCD;
    }

    @Override
    public int setCooldown(ServerPlayerEntity player, CooldownType type, int time) {
        if (!EntityHelper.isValidPlayerMP(player)) {
            return 0;
        }
        int cappedTime = Math.min(time, type.getMaxCooldown(player));
        setWorldTimeCooldown(player, type, TimeHelper.worldTicks(player.world) + cappedTime);
        return cappedTime;
    }

    @OnlyIn(Dist.CLIENT)
    public void updateClientCooldown(CooldownType type, long worldTime) {
        this.clientCooldowns.put(type, worldTime);
    }

    @OnlyIn(Dist.CLIENT)
    public void updateAllClientCooldowns(ListNBT tagList) {
        this.clientCooldowns = computePlayerCd(tagList);
    }

    private void setWorldTimeCooldown(ServerPlayerEntity player, CooldownType type, long worldTime) {
        setWorldTimeCooldown(player, type, worldTime, true);
    }

    private void setWorldTimeCooldown(ServerPlayerEntity player, CooldownType type, long worldTime, boolean checkSync) {
        ListNBT tagList = getCooldownTagList(player);
        boolean isSet = tagList.stream().filter(inbt -> FIND_BY_TYPE.test(inbt, type)).findFirst().map(inbt -> {
            ((CompoundNBT) inbt).putLong(CD_NBT_LONG, worldTime);
            return true;
        }).orElse(false);
        if (!isSet) {
            writeCooldown(tagList, type, worldTime);
        }
        this.cooldowns.computeIfAbsent(player.getUniqueID(), aId -> computePlayerCd(player)).put(type, worldTime);
        if (checkSync && type.isSync()) {
            PacketHandler.sendToPlayer(new SyncCooldownMessage(type, worldTime), player);
        }
    }

    public void updateWorldTime(ServerPlayerEntity player, long modifier) {
        if (!EntityHelper.isValidPlayerMP(player)) {
            return;
        }
        boolean requirePacket = false;
        for (CooldownType type : CooldownType.values()) {
            int cd = getCooldown(player, type);
            if (cd > 0) {
                requirePacket = true;
                setWorldTimeCooldown(player, type, cd + modifier, false);
            }
        }
        if (requirePacket) {
            PacketHandler.sendToPlayer(getCooldownPacket(player), player);
        }
    }

    private EnumMap<CooldownType, Long> computePlayerCd(ServerPlayerEntity player) {
        return computePlayerCd(getCooldownTagList(player));
    }

    private EnumMap<CooldownType, Long> computePlayerCd(ListNBT tagList) {
        EnumMap<CooldownType, Long> playerCooldowns = new EnumMap<>(CooldownType.class);
        tagList.forEach(inbt -> {
            CompoundNBT nbt = (CompoundNBT) inbt;
            CooldownType id = FIND_BY_ID.apply(nbt.getByte(ID_NBT_BYTE));
            if (id != null) {
                playerCooldowns.put(id, nbt.getLong(CD_NBT_LONG));
            }
        });
        return playerCooldowns;
    }

    private ListNBT getCooldownTagList(PlayerEntity player) {
        CompoundNBT persistentTag = EntityHelper.getPersistentTag(player);
        final ListNBT tagList = persistentTag.getList(COOLDOWNS_NBT_LIST, Constants.NBT.TAG_COMPOUND);
        persistentTag.put(COOLDOWNS_NBT_LIST, tagList);
        return tagList;
    }

    public SyncCooldownMessage getCooldownPacket(ServerPlayerEntity player) {
        ListNBT initTagList = getCooldownTagList(player);
        EnumMap<CooldownType, Long> entry = this.cooldowns.computeIfAbsent(player.getUniqueID(), aId -> computePlayerCd(initTagList));
        ListNBT tagList = new ListNBT();

        Arrays.stream(CooldownType.values()).filter(CooldownType::isSync).forEach(type -> {
            long worldTimeCD = entry.getOrDefault(type, -1L);
            if (worldTimeCD > 0) {
                long cooldown = worldTimeCD - TimeHelper.worldTicks(player.world);
                int maxCD = type.getMaxCooldown(player);
                if (cooldown <= 0 && entry.remove(type) != null) {
                    initTagList.removeIf(inbt -> FIND_BY_TYPE.test(inbt, type));
                    worldTimeCD = -1L;
                }
                if (cooldown > maxCD) {
                    worldTimeCD = TimeHelper.worldTicks(player.world) + maxCD;
                    setWorldTimeCooldown(player, type, worldTimeCD);
                }
            }
            writeCooldown(tagList, type, worldTimeCD);
        });
        return new SyncCooldownMessage(tagList);
    }

    private void writeCooldown(ListNBT tagList, CooldownType type, long worldTime) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putByte(ID_NBT_BYTE, (byte) type.ordinal());
        nbt.putLong(CD_NBT_LONG, worldTime);
        tagList.add(nbt);
    }

    public void clear() {
        this.cooldowns.clear();
    }

    public static final String COOLDOWNS_NBT_LIST = "tb_cooldowns";
    private static final String ID_NBT_BYTE = "id";
    private static final String CD_NBT_LONG = "cd";

    private static BiPredicate<INBT, CooldownType> FIND_BY_TYPE = (nbt, type) -> ((CompoundNBT) nbt).getByte(ID_NBT_BYTE) == (byte) type.ordinal();

    private static Function<Byte, CooldownType> FIND_BY_ID = id -> id >= 0 && id < CooldownType.values().length ? CooldownType.values()[id] : null;
}
