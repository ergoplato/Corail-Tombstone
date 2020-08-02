package ovh.corail.tombstone.registry;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ovh.corail.tombstone.api.recipe.RecipeEnchantedGraveKey;
import ovh.corail.tombstone.recipe.RecipeFamiliarReceptacle;
import ovh.corail.tombstone.recipe.RecipeImpregnatedDiamond;
import ovh.corail.tombstone.recipe.RecipeVoodooPoppetProtection;
import ovh.corail.tombstone.recipe.TombstoneShapedRecipe;
import ovh.corail.tombstone.recipe.TombstoneShapelessRecipe;

import static ovh.corail.tombstone.ModTombstone.MOD_ID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSerializers {
    public static final IRecipeSerializer<TombstoneShapelessRecipe> TOMBSTONE_SHAPELESS = new TombstoneShapelessRecipe.Serializer();
    public static final IRecipeSerializer<TombstoneShapedRecipe> TOMBSTONE_SHAPED = new TombstoneShapedRecipe.Serializer();

    @SubscribeEvent
    public static void onRegisterSerializers(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
        event.getRegistry().register(new SpecialRecipeSerializer<>(RecipeEnchantedGraveKey::new).setRegistryName(MOD_ID, "enchanted_grave_key"));
        event.getRegistry().register(new SpecialRecipeSerializer<>(RecipeVoodooPoppetProtection::new).setRegistryName(MOD_ID, "voodoo_poppet_protection"));
        event.getRegistry().register(new SpecialRecipeSerializer<>(RecipeFamiliarReceptacle::new).setRegistryName(MOD_ID, "familiar_receptacle"));
        event.getRegistry().register(new SpecialRecipeSerializer<>(RecipeImpregnatedDiamond::new).setRegistryName(MOD_ID, "impregnated_diamond_with_needle"));
        event.getRegistry().register(TOMBSTONE_SHAPELESS.setRegistryName(new ResourceLocation(MOD_ID, "disableable_shapeless")));
        event.getRegistry().register(TOMBSTONE_SHAPED.setRegistryName(new ResourceLocation(MOD_ID, "disableable_shaped")));
    }
}
