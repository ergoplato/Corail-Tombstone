package ovh.corail.tombstone.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import ovh.corail.tombstone.registry.ModItems;

public class RecipeImpregnatedDiamond extends ShapelessRecipe {
    public RecipeImpregnatedDiamond(ResourceLocation rl) {
        super(rl, "impregnated_diamond_with_needle", ModItems.impregnated_diamond.impregnate(new ItemStack(ModItems.impregnated_diamond), "unknown"), NonNullList.from(Ingredient.EMPTY, Ingredient.fromStacks(new ItemStack(ModItems.impregnated_diamond)), Ingredient.fromStacks(ModItems.bone_needle.impregnate(new ItemStack(ModItems.bone_needle), "unknown"))));
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        boolean impregnatedDiamondFound = false;
        boolean boneNeedleFound = false;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (!impregnatedDiamondFound && stack.getItem() == ModItems.impregnated_diamond) {
                    if (ModItems.bone_needle.getEntityType(stack).isEmpty()) {
                        impregnatedDiamondFound = true;
                        continue;
                    }
                } else if (!boneNeedleFound && stack.getItem() == ModItems.bone_needle) {
                    if (!ModItems.bone_needle.getEntityType(stack).isEmpty()) {
                        boneNeedleFound = true;
                        continue;
                    }
                }
                return false;
            }
        }
        return impregnatedDiamondFound && boneNeedleFound;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack impregnatedDiamond = ItemStack.EMPTY;
        ItemStack boneNeedle = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() == ModItems.impregnated_diamond) {
                impregnatedDiamond = stack;
            } else if (stack.getItem() == ModItems.bone_needle) {
                boneNeedle = stack;
            }
        }
        return ModItems.impregnated_diamond.impregnate(impregnatedDiamond.copy(), ModItems.bone_needle.getEntityType(boneNeedle));
    }
}
