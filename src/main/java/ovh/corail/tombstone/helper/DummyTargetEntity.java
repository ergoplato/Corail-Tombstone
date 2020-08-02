package ovh.corail.tombstone.helper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.world.World;

import java.util.Collections;

public final class DummyTargetEntity extends LivingEntity {
    @SuppressWarnings("deprecation")
    public DummyTargetEntity(World world) {
        super(EntityType.RABBIT, world);
        this.removed = true;
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {
    }

    @Override
    public HandSide getPrimaryHand() {
        return HandSide.LEFT;
    }

    @Override
    public boolean isOnSameTeam(Entity entity) {
        return true;
    }
}
