package ovh.corail.tombstone.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import ovh.corail.tombstone.api.capability.ITBCapability;

import javax.annotation.Nullable;

public class TBCapabilityStorage implements IStorage<ITBCapability> {

    @Override
    public void readNBT(Capability<ITBCapability> capability, ITBCapability instance, Direction side, INBT nbt) {
        instance.deserializeNBT((CompoundNBT) nbt);
    }

    @Override
    @Nullable
    public INBT writeNBT(Capability<ITBCapability> capability, ITBCapability instance, Direction side) {
        return instance.serializeNBT();
    }
}
