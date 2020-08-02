package ovh.corail.tombstone.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import ovh.corail.tombstone.api.capability.ITBCapability;
import ovh.corail.tombstone.helper.Helper;

import javax.annotation.Nullable;

import static ovh.corail.tombstone.ModTombstone.MOD_ID;

public class TBCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {
    public static final ResourceLocation RL = new ResourceLocation(MOD_ID, "cap_tombstone");
    @CapabilityInject(ITBCapability.class)
    public static final Capability<ITBCapability> TB_CAPABILITY = Helper.unsafeNullCast();
    private final LazyOptional<ITBCapability> holderCap = LazyOptional.of(TBCapabilityDefault::new);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return TB_CAPABILITY.orEmpty(cap, this.holderCap);
    }

    @Override
    public CompoundNBT serializeNBT() {
        return this.holderCap.map(cap -> {
            INBT nbt = TB_CAPABILITY.writeNBT(cap, null);
            return nbt != null ? (CompoundNBT) nbt : new CompoundNBT();
        }).orElse(new CompoundNBT());
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.holderCap.ifPresent(cap -> TB_CAPABILITY.readNBT(cap, null, nbt));
    }
}
