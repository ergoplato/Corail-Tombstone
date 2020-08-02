package ovh.corail.tombstone.perk;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import ovh.corail.tombstone.api.capability.Perk;
import ovh.corail.tombstone.capability.TBCapabilityProvider;
import ovh.corail.tombstone.config.SharedConfigTombstone;

import javax.annotation.Nullable;

public class PerkTombRaider extends Perk {
    public PerkTombRaider() {
        super("tomb_raider", new ResourceLocation("textures/item/wooden_shovel.png"));
    }

    @Override
    public int getLevelMax() {
        return 1;
    }

    @Override
    public boolean isDisabled(@Nullable PlayerEntity player) {
        return !SharedConfigTombstone.player_death.allowTombRaiding.get() || (player != null && player.getCapability(TBCapabilityProvider.TB_CAPABILITY, null).map(cap -> cap.getAlignmentLevel() >= 0).orElse(true));
    }

    @Override
    public String getTooltip(int level, int actualLevel, int levelWithBonus) {
        if (level == 1) {
            return I18n.format("tombstone.perk.tomb_raider.desc");
        }
        return "";
    }

    @Override
    public int getCost(int level) {
        return 3;
    }
}
