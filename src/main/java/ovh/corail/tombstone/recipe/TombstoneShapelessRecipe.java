package ovh.corail.tombstone.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import ovh.corail.tombstone.api.recipe.IDisableable;
import ovh.corail.tombstone.registry.ModSerializers;

import java.util.Objects;

public class TombstoneShapelessRecipe extends ShapelessRecipe {

    private TombstoneShapelessRecipe(ShapelessRecipe recipe) {
        super(recipe.getId(), recipe.getGroup(), recipe.getRecipeOutput(), recipe.getIngredients());
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        return ((IDisableable) getRecipeOutput().getItem()).isEnabled() && super.matches(inv, world);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModSerializers.TOMBSTONE_SHAPELESS;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<TombstoneShapelessRecipe> {

        @Override
        public TombstoneShapelessRecipe read(ResourceLocation recipeId, JsonObject json) {
            return new TombstoneShapelessRecipe(IRecipeSerializer.CRAFTING_SHAPELESS.read(recipeId, json));
        }

        @Override
        public TombstoneShapelessRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            return new TombstoneShapelessRecipe(Objects.requireNonNull(IRecipeSerializer.CRAFTING_SHAPELESS.read(recipeId, buffer)));
        }

        @Override
        public void write(PacketBuffer buffer, TombstoneShapelessRecipe recipe) {
            buffer.writeString(recipe.getGroup());
            buffer.writeVarInt(recipe.getIngredients().size());
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.write(buffer);
            }
            buffer.writeItemStack(recipe.getRecipeOutput());
        }
    }
}
