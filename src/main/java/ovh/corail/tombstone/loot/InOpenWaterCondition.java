package ovh.corail.tombstone.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import ovh.corail.tombstone.config.ConfigTombstone;
import ovh.corail.tombstone.helper.SpawnHelper;

import static ovh.corail.tombstone.ModTombstone.MOD_ID;

public class InOpenWaterCondition implements ILootCondition {
    private static final ResourceLocation RL = new ResourceLocation(MOD_ID, "in_open_water");
    private static final InOpenWaterCondition INSTANCE = new InOpenWaterCondition();
    public static final Serializer SERIALIZER = new Serializer();

    private InOpenWaterCondition() {
    }

    @Override
    public boolean test(LootContext context) {
        if (!ConfigTombstone.general.fishingTreasureInOpenWater.get()) {
            return true;
        }
        Entity hook = context.get(LootParameters.THIS_ENTITY);
        return hook != null && isOpenWaterAround(hook.world, hook.getPosition());
    }

    @SuppressWarnings("deprecation")
    private boolean isOpenWater(World world, BlockPos pos, boolean inStillWater) {
        BlockState state = world.getBlockState(pos);
        IFluidState fluidState = state.getFluidState();
        if (inStillWater) {
            return fluidState.isTagged(FluidTags.WATER) && fluidState.isSource() && (state.getBlock() != Blocks.BUBBLE_COLUMN && state.getCollisionShape(world, pos).isEmpty());
        }
        return state.isAir(world, pos) || state.getBlock() == Blocks.LILY_PAD || state.getCollisionShape(world, pos).isEmpty();
    }

    private boolean isOpenWaterAround(World world, BlockPos pos) {
        final int inStillWater = pos.getY();
        return SpawnHelper.getAllInBox(pos.add(-2, -2, -2), pos.add(2, 1, 2)).allMatch(aPos -> isOpenWater(world, aPos, aPos.getY() <= inStillWater));
    }

    public static ILootCondition.IBuilder builder() {
        return () -> INSTANCE;
    }

    private static class Serializer extends ILootCondition.AbstractSerializer<InOpenWaterCondition> {

        private Serializer() {
            super(RL, InOpenWaterCondition.class);
        }

        @Override
        public void serialize(JsonObject json, InOpenWaterCondition value, JsonSerializationContext context) {
        }

        @Override
        public InOpenWaterCondition deserialize(JsonObject json, JsonDeserializationContext context) {
            return INSTANCE;
        }
    }
}
