package ovh.corail.tombstone.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import ovh.corail.tombstone.api.recipe.IDisableable;
import ovh.corail.tombstone.registry.ModSerializers;

import java.util.Objects;

public class TombstoneShapedRecipe extends ShapedRecipe {

    private TombstoneShapedRecipe(ShapedRecipe recipe) {
        super(recipe.getId(), recipe.getGroup(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getRecipeOutput());
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        return ((IDisableable) getRecipeOutput().getItem()).isEnabled() && super.matches(inv, world);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModSerializers.TOMBSTONE_SHAPED;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<TombstoneShapedRecipe> {

        @Override
        public TombstoneShapedRecipe read(ResourceLocation recipeId, JsonObject json) {
            return new TombstoneShapedRecipe(IRecipeSerializer.CRAFTING_SHAPED.read(recipeId, json));
        }

        @Override
        public TombstoneShapedRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            return new TombstoneShapedRecipe(Objects.requireNonNull(IRecipeSerializer.CRAFTING_SHAPED.read(recipeId, buffer)));
        }

        @Override
        public void write(PacketBuffer buffer, TombstoneShapedRecipe recipe) {
            buffer.writeVarInt(recipe.getWidth());
            buffer.writeVarInt(recipe.getHeight());
            buffer.writeString(recipe.getGroup());
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.write(buffer);
            }
            buffer.writeItemStack(recipe.getRecipeOutput());
        }
    }
}
