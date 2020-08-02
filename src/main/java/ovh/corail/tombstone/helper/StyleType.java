package ovh.corail.tombstone.helper;

import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class StyleType {
    public static final Style MESSAGE_NORMAL = new Style().setColor(TextFormatting.WHITE).setItalic(false).setBold(false);
    public static final Style MESSAGE_SPECIAL = new Style().setColor(TextFormatting.GOLD).setItalic(false).setBold(false);
    public static final Style MESSAGE_SPELL = new Style().setColor(TextFormatting.BLUE).setItalic(true).setBold(false);
    public static final Style TOOLTIP_DESC = new Style().setColor(TextFormatting.GRAY).setItalic(true).setBold(false);
    public static final Style TOOLTIP_USE = new Style().setColor(TextFormatting.DARK_PURPLE).setItalic(true).setBold(false);
    public static final Style TOOLTIP_IN_BETA = MESSAGE_SPELL;
    public static final Style TOOLTIP_ENCHANT = new Style().setColor(TextFormatting.DARK_GRAY).setItalic(false).setBold(false);
    public static final Style TOOLTIP_ITEM = new Style().setColor(TextFormatting.YELLOW).setItalic(false).setBold(false);
    public static final Style COLOR_ON = new Style().setColor(TextFormatting.GREEN).setItalic(false).setBold(false);
    public static final Style COLOR_OFF = new Style().setColor(TextFormatting.RED).setItalic(false).setBold(false);
}
