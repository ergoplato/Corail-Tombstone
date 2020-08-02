package ovh.corail.tombstone.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemImpregnatedDiamond extends ItemGeneric implements IImpregnable {
    public ItemImpregnatedDiamond() {
        super("impregnated_diamond", getBuilder(true).maxStackSize(1));
        withEffect().withCraftingInfo();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        ITextComponent impregnated = getTooltipDisplay(stack);
        if (impregnated != null) {
            list.add(impregnated);
        }
        super.addInformation(stack, world, list, flag);
    }
}
