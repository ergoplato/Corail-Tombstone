package ovh.corail.tombstone.item;

public class ItemAdvancement extends ItemGeneric {
    public enum IconType {FIRST_KNOWLEDGE, CANCEL_GHOSTLY_SHAPE, TELEPORT_TO_GRAVE, FIRST_PRAY, EXORCISM, ACTIVATE_MAGIC_ITEM, GHOST, REVIVE, FAKE_FOG}

    public ItemAdvancement(IconType iconType) {
        super("advancement_" + iconType.ordinal(), getBuilder(false).maxStackSize(1));
    }
}
