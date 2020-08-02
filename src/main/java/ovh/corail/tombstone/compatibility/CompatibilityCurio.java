package ovh.corail.tombstone.compatibility;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.capability.ICurio;
import top.theillusivec4.curios.api.capability.ICurioItemHandler;
import top.theillusivec4.curios.api.inventory.CurioStackHandler;

import java.util.Set;
import java.util.SortedMap;

public class CompatibilityCurio implements IEquipableCompat {
    public static final CompatibilityCurio instance = new CompatibilityCurio();

    @CapabilityInject(ICurioItemHandler.class)
    public static final Capability<ICurioItemHandler> INVENTORY = null;
    @CapabilityInject(ICurio.class)
    public static final Capability<ICurio> ITEM = null;

    private CompatibilityCurio() {
    }

    @Override
    public boolean autoEquip(ItemStack stack, PlayerEntity player) {
        if (INVENTORY == null || ITEM == null) {
            return false;
        }
        boolean isToolbelt = isToolbelt(stack);
        LazyOptional<ICurio> lazyOptionalItem = LazyOptional.empty();
        if (!isToolbelt && !lazyOptionalItem.isPresent()) {
            return false;
        }
        SortedMap<String, CurioStackHandler> map = player.getCapability(INVENTORY, null).map(ICurioItemHandler::getCurioMap).orElse(null);
        if (map == null) {
            return false;
        }
        Set<String> tags = CuriosAPI.getCurioTags(stack.getItem());
        for (String tag : tags) {
            if (isToolbelt || lazyOptionalItem.map(capItem -> capItem.canEquip(tag, player)).orElse(false)) {
                CurioStackHandler stackHandler = map.get(tag);
                if (stackHandler != null) {
                    for (int i = 0; i < stackHandler.getSlots(); i++) {
                        if (stackHandler.getStackInSlot(i).isEmpty()) {
                            stackHandler.setStackInSlot(i, stack.copy());
                            lazyOptionalItem.ifPresent(capItem -> capItem.playEquipSound(player));
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isToolbelt(ItemStack stack) {
        ResourceLocation registryName;
        return SupportMods.TOOLBELT.isLoaded() && (registryName = stack.getItem().getRegistryName()) != null && registryName.getNamespace().equals(SupportMods.TOOLBELT.getName()) && registryName.getPath().equals("belt");
    }
}
