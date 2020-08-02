package ovh.corail.tombstone.helper;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.TieredItem;
import net.minecraftforge.items.ItemHandlerHelper;
import ovh.corail.tombstone.compatibility.CompatibilityCurio;
import ovh.corail.tombstone.compatibility.SupportMods;
import ovh.corail.tombstone.config.ConfigTombstone;

import java.util.function.Predicate;

public class InventoryHelper {
    /**
     * try to equip a stack in any empty special slot from the player inventory
     */
    public static boolean autoequip(ItemStack stack, PlayerEntity player) {
        if (stack.isEmpty() || stack.getMaxStackSize() != 1) {
            return false;
        }
        /* skip items with curse of binding */
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.BINDING_CURSE, stack) > 0) {
            return false;
        }
        if (ConfigTombstone.compatibility.allowCurioAutoEquip.get() && SupportMods.CURIOS.isLoaded() && CompatibilityCurio.instance.autoEquip(stack, player)) {
            return true;
        }
        if (stack.getItem().isShield(stack, player) && player.getHeldItemOffhand().isEmpty() && player.replaceItemInInventory(99, stack.copy())) {
            return true;
        }
        EquipmentSlotType slot = stack.getItem().getEquipmentSlot(stack);
        boolean isElytra = false;
        if (slot == null) {
            if (stack.getItem() instanceof ArmorItem) {
                slot = ((ArmorItem) stack.getItem()).getEquipmentSlot();
            } else if (stack.getItem() instanceof ElytraItem) {
                slot = EquipmentSlotType.CHEST;
                isElytra = true;
            } else {
                return false;
            }
        } else if (slot == EquipmentSlotType.CHEST) {
            isElytra = stack.getItem() instanceof ElytraItem;
        }
        int slotId = slot.getIndex();
        ItemStack stackInSlot = player.inventory.armorInventory.get(slotId);
        if (stackInSlot.isEmpty()) {
            player.inventory.armorInventory.set(slotId, stack.copy());
            return true;
        }
        if (slot != EquipmentSlotType.CHEST) {
            return false;
        }
        boolean equipElytraInPriority = DeathHandler.INSTANCE.getOptionEquipElytraInPriority(player.getUniqueID());
        boolean canEquip = stackInSlot.getItem() instanceof ElytraItem ? !isElytra && !equipElytraInPriority : isElytra && equipElytraInPriority;
        if (canEquip) {
            ItemHandlerHelper.giveItemToPlayer(player, stackInSlot.copy());
            player.inventory.armorInventory.set(slotId, stack.copy());
            return true;
        }
        return false;
    }

    public static ItemStack findItemInMainInventory(PlayerEntity player, Predicate<ItemStack> predic) {
        return player.inventory.mainInventory.stream().filter(predic).findFirst().orElse(ItemStack.EMPTY);
    }

    public static boolean isTool(ItemStack stack) {
        // !stack.getToolTypes().isEmpty();
        return !stack.isEmpty() && stack.getMaxStackSize() == 1 && (stack.getItem() instanceof TieredItem || stack.getItem() instanceof FishingRodItem || stack.getItem() instanceof ShearsItem);
    }
}
