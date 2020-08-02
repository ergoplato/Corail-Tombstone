package ovh.corail.tombstone.registry;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import ovh.corail.tombstone.item.ItemAdvancement;
import ovh.corail.tombstone.item.ItemAnkhOfPray;
import ovh.corail.tombstone.item.ItemBoneNeedle;
import ovh.corail.tombstone.item.ItemBookOfDisenchantment;
import ovh.corail.tombstone.item.ItemDustOfVanishing;
import ovh.corail.tombstone.item.ItemFamiliarReceptacle;
import ovh.corail.tombstone.item.ItemFishingRodOfMisadventure;
import ovh.corail.tombstone.item.ItemGeneric;
import ovh.corail.tombstone.item.ItemGraveKey;
import ovh.corail.tombstone.item.ItemImpregnatedDiamond;
import ovh.corail.tombstone.item.ItemLollipop;
import ovh.corail.tombstone.item.ItemLostTablet;
import ovh.corail.tombstone.item.ItemScrollBuff;
import ovh.corail.tombstone.item.ItemScrollOfKnowledge;
import ovh.corail.tombstone.item.ItemSoulReceptacle;
import ovh.corail.tombstone.item.ItemTabletOfAssistance;
import ovh.corail.tombstone.item.ItemTabletOfCupidity;
import ovh.corail.tombstone.item.ItemTabletOfHome;
import ovh.corail.tombstone.item.ItemTabletOfRecall;
import ovh.corail.tombstone.item.ItemVoodooPoppet;

import static ovh.corail.tombstone.ModTombstone.MOD_ID;

@ObjectHolder(MOD_ID)
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems extends Registrable {
    public static final ItemGraveKey grave_key = new ItemGraveKey();
    public static final ItemAnkhOfPray ankh_of_pray = new ItemAnkhOfPray();
    public static final ItemDustOfVanishing dust_of_vanishing = new ItemDustOfVanishing();
    public static final ItemSoulReceptacle soul_receptacle = new ItemSoulReceptacle();
    public static final ItemFamiliarReceptacle familiar_receptacle = new ItemFamiliarReceptacle();
    public static final ItemScrollOfKnowledge scroll_of_knowledge = new ItemScrollOfKnowledge();
    public static final ItemTabletOfRecall tablet_of_recall = new ItemTabletOfRecall();
    public static final ItemTabletOfHome tablet_of_home = new ItemTabletOfHome();
    public static final ItemVoodooPoppet voodoo_poppet = new ItemVoodooPoppet();
    public static final ItemTabletOfAssistance tablet_of_assistance = new ItemTabletOfAssistance();
    public static final ItemTabletOfCupidity tablet_of_cupidity = new ItemTabletOfCupidity();
    public static final ItemBookOfDisenchantment book_of_disenchantment = new ItemBookOfDisenchantment();
    public static final ItemFishingRodOfMisadventure fishing_rod_of_misadventure = new ItemFishingRodOfMisadventure();
    public static final ItemLostTablet lost_tablet = new ItemLostTablet();

    public static final ItemScrollBuff[] scroll_buff = new ItemScrollBuff[ItemScrollBuff.SpellBuff.values().length];

    static {
        for (ItemScrollBuff.SpellBuff spellBuff : ItemScrollBuff.SpellBuff.values()) {
            scroll_buff[spellBuff.ordinal()] = new ItemScrollBuff(spellBuff);
        }
    }

    public static final ItemLollipop[] lollipop = new ItemLollipop[ItemLollipop.ModelColor.values().length];

    static {
        for (ItemLollipop.ModelColor modelColor : ItemLollipop.ModelColor.values()) {
            lollipop[modelColor.ordinal()] = new ItemLollipop(modelColor);
        }
    }

    public static final ItemAdvancement[] advancement = new ItemAdvancement[ItemAdvancement.IconType.values().length];

    static {
        for (ItemAdvancement.IconType iconType : ItemAdvancement.IconType.values()) {
            advancement[iconType.ordinal()] = new ItemAdvancement(iconType);
        }
    }

    public static final ItemGeneric grave_dust = new ItemGeneric("grave_dust").withCraftingInfo();
    public static final ItemGeneric strange_scroll = new ItemGeneric("strange_scroll").withCraftingInfo();
    public static final ItemGeneric strange_tablet = new ItemGeneric("strange_tablet").withCraftingInfo();
    public static final ItemImpregnatedDiamond impregnated_diamond = new ItemImpregnatedDiamond();
    public static final ItemBoneNeedle bone_needle = new ItemBoneNeedle();

    @SubscribeEvent
    public static void registerItemBlock(final RegistryEvent.Register<Item> event) {
        registerForgeEntry(event.getRegistry(), grave_key, grave_key.getSimpleName());
        registerForgeEntry(event.getRegistry(), ankh_of_pray, ankh_of_pray.getSimpleName());
        registerForgeEntry(event.getRegistry(), dust_of_vanishing, dust_of_vanishing.getSimpleName());
        registerForgeEntry(event.getRegistry(), soul_receptacle, soul_receptacle.getSimpleName());
        registerForgeEntry(event.getRegistry(), familiar_receptacle, familiar_receptacle.getSimpleName());
        registerForgeEntry(event.getRegistry(), scroll_of_knowledge, scroll_of_knowledge.getSimpleName());
        registerForgeEntry(event.getRegistry(), tablet_of_recall, tablet_of_recall.getSimpleName());
        registerForgeEntry(event.getRegistry(), tablet_of_home, tablet_of_home.getSimpleName());
        registerForgeEntry(event.getRegistry(), voodoo_poppet, voodoo_poppet.getSimpleName());
        registerForgeEntry(event.getRegistry(), tablet_of_assistance, tablet_of_assistance.getSimpleName());
        registerForgeEntry(event.getRegistry(), tablet_of_cupidity, tablet_of_cupidity.getSimpleName());
        registerForgeEntry(event.getRegistry(), book_of_disenchantment, book_of_disenchantment.getSimpleName());
        registerForgeEntry(event.getRegistry(), fishing_rod_of_misadventure, fishing_rod_of_misadventure.getSimpleName());
        registerForgeEntry(event.getRegistry(), lost_tablet, lost_tablet.getSimpleName());

        for (ItemScrollBuff.SpellBuff buff : ItemScrollBuff.SpellBuff.values()) {
            registerForgeEntry(event.getRegistry(), scroll_buff[buff.ordinal()], scroll_buff[buff.ordinal()].getSimpleName());
        }

        for (ItemLollipop.ModelColor color : ItemLollipop.ModelColor.values()) {
            registerForgeEntry(event.getRegistry(), lollipop[color.ordinal()], lollipop[color.ordinal()].getSimpleName());
        }

        for (ItemAdvancement.IconType type : ItemAdvancement.IconType.values()) {
            registerForgeEntry(event.getRegistry(), advancement[type.ordinal()], advancement[type.ordinal()].getSimpleName());
        }

        registerForgeEntry(event.getRegistry(), grave_dust, grave_dust.getSimpleName());
        registerForgeEntry(event.getRegistry(), strange_scroll, strange_scroll.getSimpleName());
        registerForgeEntry(event.getRegistry(), strange_tablet, strange_tablet.getSimpleName());
        registerForgeEntry(event.getRegistry(), impregnated_diamond, impregnated_diamond.getSimpleName());
        registerForgeEntry(event.getRegistry(), bone_needle, bone_needle.getSimpleName());
    }
}
