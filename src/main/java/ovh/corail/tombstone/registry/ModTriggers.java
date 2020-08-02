package ovh.corail.tombstone.registry;

import net.minecraft.advancements.CriteriaTriggers;
import ovh.corail.tombstone.helper.StatelessTrigger;
import ovh.corail.tombstone.item.ItemScrollBuff;
import ovh.corail.tombstone.item.ItemVoodooPoppet;

import java.util.Arrays;
import java.util.EnumMap;

public class ModTriggers {
    public static final StatelessTrigger ACTIVATE_MAGIC_ITEM = register("activate_magic_item");
    public static final StatelessTrigger FIRST_KNOWLEDGE = register("first_knowledge");
    public static final StatelessTrigger MASTERY_1 = register("mastery_1");
    public static final StatelessTrigger ENGRAVE_DECORATIVE_GRAVE = register("engrave_decorative_grave");
    public static final StatelessTrigger CANCEL_GHOSTLY_SHAPE = register("cancel_ghostly_shape");
    public static final StatelessTrigger FIRST_GRAVE = register("first_grave");
    public static final StatelessTrigger CHOOSE_GRAVE_TYPE = register("choose_grave_type");
    public static final StatelessTrigger CHOOSE_KNOWLEDGE = register("choose_knowledge");
    public static final StatelessTrigger EXORCISM = register("exorcism");
    public static final StatelessTrigger REVIVE_FAMILIAR = register("revive_familiar");
    public static final StatelessTrigger FIRST_PRAY = register("first_pray");
    public static final StatelessTrigger USE_DISENCHANTMENT = register("use_disenchantment");
    public static final StatelessTrigger CAPTURE_SOUL = register("capture_soul");
    public static final StatelessTrigger TELEPORT_TO_GRAVE = register("teleport_to_grave");
    public static final StatelessTrigger USE_LOST_TABLET = register("use_lost_tablet");
    public static final StatelessTrigger FIND_LOST_TABLET = register("find_lost_tablet");
    public static final StatelessTrigger USE_KNOWLEDGE = register("use_knowledge");
    public static final StatelessTrigger USE_ASSISTANCE = register("use_assistance");
    public static final StatelessTrigger USE_CUPIDITY = register("use_cupidity");
    public static final StatelessTrigger USE_HOME = register("use_home");
    public static final StatelessTrigger USE_RECALL = register("use_recall");
    public static final StatelessTrigger PASS_APRIL_FOOL = register("pass_april_fool");
    public static final EnumMap<ItemScrollBuff.SpellBuff, StatelessTrigger> SPELL_BUFF = new EnumMap<>(ItemScrollBuff.SpellBuff.class);
    public static final EnumMap<ItemVoodooPoppet.PoppetProtections, StatelessTrigger> PREVENT_DEATH = new EnumMap<>(ItemVoodooPoppet.PoppetProtections.class);

    static {
        Arrays.stream(ItemScrollBuff.SpellBuff.values()).forEach(buff -> SPELL_BUFF.put(buff, register("use_" + buff.getName())));
    }

    static {
        Arrays.stream(ItemVoodooPoppet.PoppetProtections.values()).forEach(prot -> PREVENT_DEATH.put(prot, register("prevent_death_" + prot.getName())));
    }

    private static StatelessTrigger register(String name) {
        return CriteriaTriggers.register(new StatelessTrigger(name));
    }
}
