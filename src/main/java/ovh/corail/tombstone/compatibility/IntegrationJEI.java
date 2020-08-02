package ovh.corail.tombstone.compatibility;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import ovh.corail.tombstone.ModTombstone;
import ovh.corail.tombstone.block.GraveModel;
import ovh.corail.tombstone.block.ItemBlockGrave;
import ovh.corail.tombstone.helper.NBTStackHelper;
import ovh.corail.tombstone.item.ItemTablet;
import ovh.corail.tombstone.registry.ModBlocks;
import ovh.corail.tombstone.registry.ModItems;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@JeiPlugin
public class IntegrationJEI implements IModPlugin {
    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ModTombstone.MOD_ID);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        jeiRuntime.getIngredientManager().addIngredientsAtRuntime(VanillaTypes.ITEM, Collections.singleton(new ItemStack(ModItems.grave_key)));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        ImmutableList.Builder<Object> builder = ImmutableList.builder();
        IVanillaRecipeFactory factory = registry.getVanillaRecipeFactory();
        addAnvilRecipeToGrave(builder, factory, GraveModel.GRAVE_SIMPLE, "Corail31");
        addAnvilRecipeToGrave(builder, factory, GraveModel.GRAVE_NORMAL, "Gegy1000");
        addAnvilRecipeToGrave(builder, factory, GraveModel.GRAVE_CROSS, "Paul Fulham");
        addAnvilRecipeToGrave(builder, factory, GraveModel.TOMBSTONE, "Runemoro");
        addAnvilRecipeToGrave(builder, factory, GraveModel.SUBARAKI_GRAVE, "Subaraki");
        addAnvilRecipeToGrave(builder, factory, GraveModel.GRAVE_ORIGINAL, "Barteks2x");
        ItemStack tablet = NBTStackHelper.setBoolean(new ItemStack(ModItems.tablet_of_assistance), "enchant", true);
        builder.add(registry.getVanillaRecipeFactory().createAnvilRecipe(tablet, Collections.singletonList(new ItemStack(ModItems.grave_dust)), Collections.singletonList(NBTStackHelper.setString(tablet.copy(), "engraved_name", "Goshen"))));
        registry.addRecipes(builder.build(), VanillaRecipeCategoryUid.ANVIL);
    }

    private void addAnvilRecipeToGrave(ImmutableList.Builder<Object> builder, IVanillaRecipeFactory factory, GraveModel model, String engravedName) {
        builder.addAll(IntStream.rangeClosed(0, ItemBlockGrave.MAX_MODEL_TEXTURE).mapToObj(modelId -> factory.createAnvilRecipe(ItemBlockGrave.createDecorativeStack(model, modelId), Collections.singletonList(new ItemStack(Items.IRON_INGOT)), Collections.singletonList(ItemBlockGrave.createDecorativeStack(model, modelId, engravedName)))).collect(Collectors.toList()));
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        for (Block block : ModBlocks.decorative_graves.values()) {
            registration.registerSubtypeInterpreter(block.asItem(), new DecorativeGraveInterpreter());
        }
        registration.registerSubtypeInterpreter(ModItems.tablet_of_home, new AncientTabletInterpreter());
        registration.registerSubtypeInterpreter(ModItems.tablet_of_recall, new AncientTabletInterpreter());
    }

    private static class DecorativeGraveInterpreter implements ISubtypeInterpreter {
        @Override
        public String apply(ItemStack stack) {
            return String.valueOf(ItemBlockGrave.getModelTexture(stack));
        }
    }

    private static class AncientTabletInterpreter implements ISubtypeInterpreter {
        @Override
        public String apply(ItemStack stack) {
            if (stack.getItem() == ModItems.tablet_of_home || stack.getItem() == ModItems.tablet_of_recall) {
                return String.valueOf(((ItemTablet) stack.getItem()).isAncient(stack));
            }
            return "0";
        }
    }
}
