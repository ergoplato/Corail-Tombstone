package ovh.corail.tombstone.config;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import ovh.corail.tombstone.network.UpdateConfigMessage;

import java.util.BitSet;

import static ovh.corail.tombstone.ModTombstone.MOD_ID;

public class SharedConfigTombstone {

    public static class PlayerDeath {
        public final ConfigValue<Boolean> handlePlayerXp;
        public final ConfigValue<Integer> xpLoss;
        public final ConfigValue<Integer> decayTime;
        public final ConfigValue<Boolean> allowTombRaiding;

        PlayerDeath(BuilderHandler builder) {
            builder.comment("Options related to player's death").push("player_death");
            handlePlayerXp = builder.add("handle_player_xp", "Whether to handle player experience [false/true|default:true]", true);
            xpLoss = builder.add("xp_loss", "Experience loss on death (%) [0..100|default:0]", 0, 0, 100);
            decayTime = builder.add("decay_time", "The time in minutes before a Grave's Key isn't required to open a grave (-1 = disabled) [-1..MAX|default:-1]", -1, -1, Integer.MAX_VALUE);
            allowTombRaiding = builder.add("allow_tomb_raiding", "Allows players with bad alignment and the perk tomb raider to steal items on abandoned graves [false/true|default:false]", false);
            builder.pop();
        }
    }

    public static class General {

        public final ConfigValue<Integer> ghostlyShapeDuration;
        public final ConfigValue<Integer> chanceEnchantedGraveKey;
        public final ConfigValue<Integer> familiarReceptacleRequiredLevel;
        public final ConfigValue<Integer> scrollDuration;

        General(BuilderHandler builder) {
            builder.comment("Miscellaneous options").push("general");
            ghostlyShapeDuration = builder.add("ghostly_shape_duration", "The duration of the Ghostly Shape effect in seconds [0..MAX|default:120]", 120, 0, Integer.MAX_VALUE);
            chanceEnchantedGraveKey = builder.add("chance_enchanted_grave_key", "The chance that a player receives an already enchanted Grave's Key on death [-1..100|default:0|disabled:-1]", 0, -1, 100);
            familiarReceptacleRequiredLevel = builder.add("familiar_receptacle_required_level", "Required level in Knowledge of Death to craft Familiar Receptacle [0..20|default:10]", 10, 0, 20);
            scrollDuration = builder.add("scroll_duration", "Scroll duration [1200..120000|default:12000]", 12000, 1200, 120000);
            builder.pop();
        }
    }

    public static class Enchantments {
        public final ConfigValue<Boolean> nerfShadowStep;
        public final ConfigValue<Boolean> enableEnchantmentShadowStep;
        public final ConfigValue<Boolean> enableEnchantmentSoulbound;
        public final ConfigValue<Boolean> enableEnchantmentMagicSiphon;
        public final ConfigValue<Boolean> enableEnchantmentPlagueBringer;
        public final ConfigValue<Boolean> magicSiphonEnchantingTable;
        public final ConfigValue<Boolean> plagueBringerEnchantingTable;
        public final ConfigValue<Boolean> allowPlagueBringerCombiningMagicSiphon;
        public final ConfigValue<Integer> maxLevelMagicSiphon;
        public final ConfigValue<Integer> maxLevelPlagueBringer;

        Enchantments(BuilderHandler builder) {
            builder.comment("Allows to customize or disable the enchantments").push("enchantments");
            nerfShadowStep = builder.add("nerf_shadow_step", "Shadow Step is only active while sneaking [false/true|default:false]", false);
            enableEnchantmentShadowStep = builder.add("enable_enchantment_shadow_step", "Enables the enchantment Shadow Step [false/true|default:true]", true);
            enableEnchantmentSoulbound = builder.add("enable_enchantment_soulbound", "Enables the enchantment Soulbound [false/true|default:true]", true);
            enableEnchantmentMagicSiphon = builder.add("enable_enchantment_magic_siphon", "Enables the enchantment Magic Siphon [false/true|default:true]", true);
            enableEnchantmentPlagueBringer = builder.add("enable_enchantment_plague_bringer", "Enables the enchantment Plague Bringer [false/true|default:true]", true);
            magicSiphonEnchantingTable = builder.add("magic_siphon_enchanting_table", "Allows Magic Siphon at enchanting table [false/true|default:true]", true);
            plagueBringerEnchantingTable = builder.add("plague_bringer_enchanting_table", "Allows Plague Bringer at enchanting table [false/true|default:false]", false);
            allowPlagueBringerCombiningMagicSiphon = builder.add("plague_bringer_combining_magic_siphon", "Allows Plague Bringer to combine with Magic Siphon [false/true|default:false]", false);
            maxLevelMagicSiphon = builder.add("max_level_magic_siphon", "Max level for Magic Siphon [1..10|default:5]", 5, 1, 10);
            maxLevelPlagueBringer = builder.add("max_level_plague_bringer", "Max level for Plague Bringer [1..10|default:3]", 3, 1, 10);
            builder.pop();
        }
    }

    public static class DecorativeGrave {
        public final ConfigValue<Integer> tabletMaxUse;
        public final ConfigValue<Boolean> unbreakableDecorativeGrave;
        public final ConfigValue<Integer> cooldownToPray;
        public final ConfigValue<Integer> cooldownResetPerk;

        DecorativeGrave(BuilderHandler builder) {
            builder.comment("For settings related to decorative tombs and magic items").push("decorative_grave");
            tabletMaxUse = builder.add("tablet_max_use", "Maximum uses of a tablet [1..20|default:10]", 10, 1, 20);
            unbreakableDecorativeGrave = builder.add("unbreakable_decorative_grave", "Sets the decorative graves unbreakable [false/true|default:false]", false);
            cooldownToPray = builder.add("cooldown_to_pray", "The cooldown in hours to pray with the Ânkh [1..10|default:3]", 3, 1, 10);
            cooldownResetPerk = builder.add("cooldown_reset_perk", "The cooldown in minutes to reset the perks with the ankh of Pray [0..60|default:20]", 20, 0, 60);
            builder.pop();
        }
    }

    public static class AllowedMagicItems {
        public final ConfigValue<Boolean> allowVoodooPoppet;
        public final ConfigValue<Boolean> allowBookOfDisenchantment;
        public final ConfigValue<Boolean> allowScrollOfPreservation;
        public final ConfigValue<Boolean> allowGraveKey;
        public final ConfigValue<Boolean> allowScrollOfKnowledge;
        public final ConfigValue<Boolean> allowTabletOfRecall;
        public final ConfigValue<Boolean> allowTabletOfHome;
        public final ConfigValue<Boolean> allowTabletOfAssistance;
        public final ConfigValue<Boolean> allowTabletOfCupidity;
        public final ConfigValue<Boolean> allowScrollOfUnstableIntangibleness;
        public final ConfigValue<Boolean> allowScrollOfFeatherFall;
        public final ConfigValue<Boolean> allowScrollOfPurification;
        public final ConfigValue<Boolean> allowScrollOfTrueSight;
        public final ConfigValue<Boolean> allowLostTablet;
        public final ConfigValue<Boolean> allowScrollOfReach;
        public final ConfigValue<Boolean> allowScrollOfLightningResistance;
        public final ConfigValue<Boolean> allowDustOfVanishing;

        AllowedMagicItems(BuilderHandler builder) {
            builder.comment("Allows to disable some magic items").push("allowedMagicItems");
            allowVoodooPoppet = builder.add("allow_voodoo_poppet", "Voodoo Poppet [false/true|default:true]", getItemTranslation("voodoo_poppet"), true);
            allowBookOfDisenchantment = builder.add("allow_book_of_disenchantment", "Book of Disenchantment [false/true|default:true]", getItemTranslation("book_of_disenchantment"), true);
            allowScrollOfPreservation = builder.add("allow_scroll_of_preservation", "Scroll of Preservation [false/true|default:true]", getItemTranslation("scroll_of_preservation"), true);
            allowGraveKey = builder.add("allow_grave_key", "Grave's Key [false/true|default:true]", getItemTranslation("grave_key"), true);
            allowScrollOfKnowledge = builder.add("allow_scroll_of_knowledge", "Scroll of Knowledge [false/true|default:true]", getItemTranslation("scroll_of_knowledge"), true);
            allowTabletOfRecall = builder.add("allow_tablet_of_recall", "Tablet of Recall [false/true|default:true]", getItemTranslation("tablet_of_recall"), true);
            allowTabletOfHome = builder.add("allow_tablet_of_home", "Tablet of Home [false/true|default:true]", getItemTranslation("tablet_of_home"), true);
            allowTabletOfAssistance = builder.add("allow_tablet_of_assistance", "Tablet of Assistance [false/true|default:true]", getItemTranslation("tablet_of_assistance"), true);
            allowTabletOfCupidity = builder.add("allow_tablet_of_cupidity", "Tablet of Cupidity [false/true|default:true]", getItemTranslation("tablet_of_cupidity"), true);
            allowScrollOfUnstableIntangibleness = builder.add("allow_scroll_of_unstable_intangibleness", "Scroll of Unstable Intangibleness [false/true|default:true]", getItemTranslation("scroll_of_unstable_intangibleness"), true);
            allowScrollOfFeatherFall = builder.add("allow_scroll_of_feather_fall", "Scroll of Feather Fall [false/true|default:true]", getItemTranslation("scroll_of_feather_fall"), true);
            allowScrollOfPurification = builder.add("allow_scroll_of_purification", "Scroll of Purification [false/true|default:true]", getItemTranslation("scroll_of_purification"), true);
            allowScrollOfTrueSight = builder.add("allow_scroll_of_true_sight", "Scroll of True Sight [false/true|default:true]", getItemTranslation("scroll_of_true_sight"), true);
            allowLostTablet = builder.add("allow_lost_tablet", "Lost Tablet [false/true|default:true]", getItemTranslation("lost_tablet"), true);
            allowScrollOfReach = builder.add("allow_scroll_of_reach", "Scroll of Reach [false/true|default:true]", getItemTranslation("scroll_of_reach"), true);
            allowScrollOfLightningResistance = builder.add("allow_scroll_of_lightning_resistance", "Scroll of Lightning Resistance [false/true|default:true]", getItemTranslation("scroll_of_lightning_resistance"), true);
            allowDustOfVanishing = builder.add("allow_dust_of_vanishing", "Dust of Vanishing [false/true|default:true]", getItemTranslation("dust_of_vanishing"), true);
            builder.pop();
        }
    }

    public static class Loot {
        public final ConfigValue<Integer> chanceLootLostTablet;

        Loot(BuilderHandler builder) {
            builder.comment("Allows to change the chance to drop some items").push("loot");
            chanceLootLostTablet = builder.add("chance_loot_lost_tablet", "Chance to loot a Lost Tablet by fishing [0..1000|default:100]", 100, 0, 1000);
            builder.pop();
        }
    }

    private static String getTranslation(String name) {
        return MOD_ID + ".config." + name;
    }

    private static String getItemTranslation(String name) {
        return MOD_ID + ".item." + name;
    }

    public static final PlayerDeath player_death;
    public static final General general;
    public static final Enchantments enchantments;
    public static final DecorativeGrave decorative_grave;
    public static final AllowedMagicItems allowed_magic_items;
    public static final Loot loot;

    public static final ForgeConfigSpec CONFIG_SPEC;

    private static final ImmutableList<ConfigValue<Boolean>> BOOL_CONFIGS;
    private static final ImmutableList<ConfigValue<Integer>> INT_CONFIGS;

    static {
        BuilderHandler builder = new BuilderHandler();
        player_death = new PlayerDeath(builder);
        general = new General(builder);
        enchantments = new Enchantments(builder);
        decorative_grave = new DecorativeGrave(builder);
        allowed_magic_items = new AllowedMagicItems(builder);
        loot = new Loot(builder);
        CONFIG_SPEC = builder.build();
        BOOL_CONFIGS = builder.getBoolBuilder().build();
        INT_CONFIGS = builder.getIntBuilder().build();
    }

    private static class BuilderHandler extends ForgeConfigSpec.Builder {
        private final ImmutableList.Builder<ConfigValue<Boolean>> boolBuilder = new ImmutableList.Builder<>();
        private final ImmutableList.Builder<ConfigValue<Integer>> intBuilder =  new ImmutableList.Builder<>();

        private ImmutableList.Builder<ConfigValue<Boolean>> getBoolBuilder() {
            return boolBuilder;
        }

        private ImmutableList.Builder<ConfigValue<Integer>> getIntBuilder() {
            return intBuilder;
        }

        private ConfigValue<Boolean> add(String name, String comment, boolean defaultVal) {
            return add(name, comment, getTranslation(name), defaultVal);
        }

        private ConfigValue<Boolean> add(String name, String comment, String translation, boolean defaultVal) {
            ConfigValue<Boolean> config = comment(comment).translation(translation).define(name, defaultVal);
            getBoolBuilder().add(config);
            return config;
        }

        private ConfigValue<Integer> add(String name, String comment, int defaultVal, int minVal, int maxVal) {
            ConfigValue<Integer> config = comment(comment).translation(getTranslation(name)).defineInRange(name, defaultVal, minVal, maxVal);
            getIntBuilder().add(config);
            return config;
        }
    }

    public static UpdateConfigMessage getUpdatePacket() {
        BitSet boolConfigs = new BitSet(BOOL_CONFIGS.size());
        for (int i = 0; i < BOOL_CONFIGS.size(); i++) {
            boolConfigs.set(i, BOOL_CONFIGS.get(i).get());
        }
        int[] intConfigs = new int[INT_CONFIGS.size()];
        for (int i = 0; i < INT_CONFIGS.size(); i++) {
            intConfigs[i] = INT_CONFIGS.get(i).get();
        }
        return new UpdateConfigMessage(boolConfigs, intConfigs);
    }

    public static void updateConfig(BitSet boolConfigs, int[] intConfigs) {
        for (int i = 0; i < BOOL_CONFIGS.size(); i++) {
            BOOL_CONFIGS.get(i).set(boolConfigs.get(i));
        }
        for (int i = 0; i < INT_CONFIGS.size(); i++) {
            INT_CONFIGS.get(i).set(intConfigs[i]);
        }
    }
}
