package ovh.corail.tombstone.compatibility;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

interface IEquipableCompat {
    boolean autoEquip(ItemStack stack, PlayerEntity player);
}
