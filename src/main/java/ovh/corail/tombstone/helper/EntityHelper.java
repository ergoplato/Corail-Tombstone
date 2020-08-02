package ovh.corail.tombstone.helper;

import net.minecraft.command.ICommandSource;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import ovh.corail.tombstone.api.capability.ITBCapability;
import ovh.corail.tombstone.api.capability.Perk;
import ovh.corail.tombstone.capability.TBCapabilityProvider;

import javax.annotation.Nullable;

public class EntityHelper {

    public static boolean hasEnchantment(LivingEntity entity, Enchantment ench) {
        return hasEnchantment(entity, ench, 1);
    }

    public static boolean hasEnchantment(LivingEntity entity, Enchantment ench, int lvl) {
        for (ItemStack stack : ench.getEntityEquipment(entity).values()) {
            if (EnchantmentHelper.getEnchantmentLevel(ench, stack) >= lvl) {
                return true;
            }
        }
        return false;
    }

    public static int getEnchantmentLevel(LivingEntity entity, Enchantment ench) {
        int lvl = 0;
        for (ItemStack stack : ench.getEntityEquipment(entity).values()) {
            int currentLvl = EnchantmentHelper.getEnchantmentLevel(ench, stack);
            if (currentLvl > lvl) {
                lvl = currentLvl;
            }
        }
        return lvl;
    }

    public static boolean isValidPlayer(@Nullable ICommandSource sender) {
        return sender instanceof PlayerEntity && !(sender instanceof FakePlayer);
    }

    public static boolean isValidPlayer(@Nullable Entity entity) {
        return entity instanceof PlayerEntity && !(entity instanceof FakePlayer);
    }

    public static boolean isValidPlayerMP(@Nullable Entity entity) {
        return isValidPlayer(entity) && entity.world != null && !entity.world.isRemote;
    }

    public static boolean isValidPlayerMP(@Nullable PlayerEntity entity) {
        return isValidPlayer(entity) && entity.world != null && !entity.world.isRemote;
    }

    public static boolean isValidPlayer(@Nullable PlayerEntity player) {
        return player != null && !(player instanceof FakePlayer);
    }

    public static boolean isSurvivalPlayer(@Nullable PlayerEntity player) {
        return isValidPlayer(player) && !player.isCreative() && !player.isSpectator();
    }

    public static void setPlayerXp(PlayerEntity player, int amount) {
        if (amount < 0) {
            return;
        }
        player.experienceTotal = player.experienceLevel = 0;
        player.experience = 0f;
        player.experience += (float) amount / (float) player.xpBarCap();
        for (player.experienceTotal += amount; player.experience >= 1.0F; player.experience /= (float) player.xpBarCap()) {
            player.experience = (player.experience - 1.0F) * (float) player.xpBarCap();
            player.experienceLevel++;
        }
    }

    /**
     * calculate the total experience of the player based on the level and the experience bar
     */
    public static int getPlayerTotalXp(PlayerEntity player) {
        return getPlayerTotalXp(player.experienceLevel, player.experience);
    }

    public static int getPlayerTotalXp(int level, float bar) {
        int experienceTotal;
        if (level < 17) {
            experienceTotal = (level * level) + (6 * level);
            experienceTotal += ((2 * level) + 7) * bar;
        } else if (level < 32) {
            experienceTotal = (int) (level * level * 2.5 - 40.5 * level + 360);
            experienceTotal += ((5 * level) - 38) * bar;
        } else {
            experienceTotal = (int) (level * level * 4.5 - 162.5 * level + 2220);
            experienceTotal += ((9 * level) - 158) * bar;
        }
        return experienceTotal;
    }

    public static int xpBarCap(int level) {
        if (level >= 30) {
            return 112 + (level - 30) * 9;
        } else {
            return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
        }
    }

    public static boolean hasCooldown(@Nullable PlayerEntity player, Item item) {
        return getCooldown(player, item) != 0f;
    }

    public static boolean hasCooldown(@Nullable PlayerEntity player, ItemStack stack) {
        return hasCooldown(player, stack.getItem());
    }

    public static void setCooldown(PlayerEntity player, Item item, int ticks) {
        player.getCooldownTracker().setCooldown(item, ticks);
    }

    public static void setCooldown(PlayerEntity player, ItemStack stack, int ticks) {
        setCooldown(player, stack.getItem(), ticks);
    }

    public static float getCooldown(@Nullable PlayerEntity player, Item item) {
        return player != null ? player.getCooldownTracker().getCooldown(item, 0f) : 0f;
    }

    public static long getKnowledge(PlayerEntity player) {
        return player.getCapability(TBCapabilityProvider.TB_CAPABILITY, null).map(ITBCapability::getKnowledge).orElse(0L);
    }

    public static void addKnowledge(ServerPlayerEntity player, long points) {
        player.getCapability(TBCapabilityProvider.TB_CAPABILITY, null).ifPresent(cap -> cap.addKnowledgeAndSync(player, points));
    }

    public static void removeKnowledge(ServerPlayerEntity player, long points) {
        player.getCapability(TBCapabilityProvider.TB_CAPABILITY, null).ifPresent(cap -> cap.removeKnowledgeAndSync(player, points));
    }

    public static void addAlignment(ServerPlayerEntity player, int amount, int chanceOn100) {
        chanceOn100 = MathHelper.clamp(chanceOn100, 0, 100);
        if (chanceOn100 > 0 && Helper.random.nextInt(100) < chanceOn100) {
            addAlignment(player, amount);
        }
    }

    public static void addAlignment(ServerPlayerEntity player, int amount) {
        player.getCapability(TBCapabilityProvider.TB_CAPABILITY, null).ifPresent(cap -> cap.addAlignmentAndSync(player, amount));
    }

    public static boolean isBadAlignment(ServerPlayerEntity player) {
        return player.getCapability(TBCapabilityProvider.TB_CAPABILITY).map(cap -> cap.getAlignmentLevel() < 0).orElse(false);
    }

    public static int getPerkLevelWithBonus(@Nullable PlayerEntity player, @Nullable Perk perk) {
        return player == null ? 0 : player.getCapability(TBCapabilityProvider.TB_CAPABILITY, null).map(cap -> cap.getPerkLevelWithBonus(player, perk)).orElse(0);
    }

    public static void syncTBCapability(ServerPlayerEntity player) {
        player.getCapability(TBCapabilityProvider.TB_CAPABILITY, null).ifPresent(cap -> cap.syncAll(player));
    }

    public static CompoundNBT getPersistentTag(PlayerEntity player) {
        CompoundNBT persistantData = player.getPersistentData();
        if (persistantData.contains(PlayerEntity.PERSISTED_NBT_TAG, Constants.NBT.TAG_COMPOUND)) {
            return persistantData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
        }
        CompoundNBT persistantTag = new CompoundNBT();
        persistantData.put(PlayerEntity.PERSISTED_NBT_TAG, persistantTag);
        return persistantTag;
    }
}
