package ovh.corail.tombstone.registry;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import ovh.corail.tombstone.block.BlockDecorativeGrave;
import ovh.corail.tombstone.block.BlockGrave;
import ovh.corail.tombstone.block.BlockGraveMarble;
import ovh.corail.tombstone.block.BlockGraveMarble.MarbleType;
import ovh.corail.tombstone.block.GraveModel;
import ovh.corail.tombstone.block.ItemBlockGrave;
import ovh.corail.tombstone.tileentity.TileEntityDecorativeGrave;
import ovh.corail.tombstone.tileentity.TileEntityGrave;

import java.util.EnumMap;

import static ovh.corail.tombstone.ModTombstone.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks extends Registrable {
    public static final EnumMap<GraveModel, Block> graves = new EnumMap<>(GraveModel.class);
    public static final EnumMap<GraveModel, Block> decorative_graves = new EnumMap<>(GraveModel.class);
    @ObjectHolder(MOD_ID + ":tile_decorative_grave")
    public static TileEntityType<TileEntityDecorativeGrave> tile_decorative_grave;
    @ObjectHolder(MOD_ID + ":tile_grave")
    public static TileEntityType<TileEntityGrave> tile_grave;
    @ObjectHolder(MOD_ID + ":dark_marble")
    public static final Block dark_marble = Blocks.AIR;
    @ObjectHolder(MOD_ID + ":white_marble")
    public static final Block white_marble = Blocks.AIR;

    @SubscribeEvent
    public static void registerTileEntity(final RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().registerAll(
                TileEntityType.Builder.create(TileEntityDecorativeGrave::new, decorative_graves.values().toArray(new Block[0])).build(null).setRegistryName(MOD_ID, "tile_decorative_grave"),
                TileEntityType.Builder.create(TileEntityGrave::new, graves.values().toArray(new Block[0])).build(null).setRegistryName(MOD_ID, "tile_grave")
        );
    }

    @SubscribeEvent
    public static void registerBlock(final RegistryEvent.Register<Block> event) {
        for (GraveModel graveModel : GraveModel.values()) {
            graves.put(graveModel, registerForgeEntry(event.getRegistry(), new BlockGrave(graveModel), graveModel.getName()));
            decorative_graves.put(graveModel, registerForgeEntry(event.getRegistry(), new BlockDecorativeGrave(graveModel), "decorative_" + graveModel.getName()));
        }
        registerForgeEntry(event.getRegistry(), new BlockGraveMarble(MarbleType.DARK), "dark_marble");
        registerForgeEntry(event.getRegistry(), new BlockGraveMarble(MarbleType.WHITE), "white_marble");
    }

    @SubscribeEvent
    public static void registerItemBlock(final RegistryEvent.Register<Item> event) {
        for (GraveModel graveModel : GraveModel.values()) {
            Block decorativeGrave = decorative_graves.get(graveModel);
            registerForgeEntry(event.getRegistry(), new ItemBlockGrave(decorativeGrave), decorativeGrave.getRegistryName());
        }
        registerForgeEntry(event.getRegistry(), new BlockItem(dark_marble, new Item.Properties().group(ModTabs.mainTab)), dark_marble.getRegistryName());
        registerForgeEntry(event.getRegistry(), new BlockItem(white_marble, new Item.Properties().group(ModTabs.mainTab)), white_marble.getRegistryName());
    }
}
