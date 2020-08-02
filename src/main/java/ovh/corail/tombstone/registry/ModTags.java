package ovh.corail.tombstone.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import static ovh.corail.tombstone.ModTombstone.MOD_ID;

public class ModTags {
    public static class Blocks {
        public static final Tag<Block> graves = tag("graves");
        public static final Tag<Block> player_graves = tag("player_graves");
        public static final Tag<Block> decorative_graves = tag("decorative_graves");
        public static final Tag<Block> grave_marbles = tag("grave_marbles");

        private static Tag<Block> tag(String name) {
            return new BlockTags.Wrapper(new ResourceLocation(MOD_ID, name));
        }
    }

    public static class Items {
        private static Tag<Item> tag(String name) {
            return new ItemTags.Wrapper(new ResourceLocation(MOD_ID, name));
        }
    }
}
