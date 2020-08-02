package ovh.corail.tombstone.item;

import java.util.function.Supplier;

public abstract class ItemScroll extends ItemGraveMagic {

    protected ItemScroll(String name, Supplier<Boolean> supplierBoolean) {
        super(name, supplierBoolean);
    }

    @Override
    public int getUseMax() {
        return 1;
    }

    @Override
    public int getCastingCooldown() {
        return 1200;
    }

    @Override
    public boolean canConsumeOnUse() {
        return true;
    }
}
