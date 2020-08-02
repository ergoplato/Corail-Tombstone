package ovh.corail.tombstone.network;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import ovh.corail.tombstone.block.BlockGrave;
import ovh.corail.tombstone.helper.Helper;

import java.util.function.Supplier;

public class TombstoneActivatedMessage {
    private BlockPos currentPos;

    public TombstoneActivatedMessage(BlockPos currentPos) {
        this.currentPos = currentPos;
    }

    static TombstoneActivatedMessage fromBytes(PacketBuffer buf) {
        return new TombstoneActivatedMessage(BlockPos.fromLong(buf.readLong()));
    }

    static void toBytes(TombstoneActivatedMessage msg, PacketBuffer buf) {
        buf.writeLong(msg.currentPos.toLong());
    }

    @SuppressWarnings("deprecation")
    public static class Handler {
        static void handle(final TombstoneActivatedMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context ctx = contextSupplier.get();
            if (Helper.isPacketToServer(ctx)) {
                ctx.enqueueWork(() -> {
                    ServerPlayerEntity player = ctx.getSender();
                    if (player != null) {
                        World world = player.world;
                        BlockState state = world.getBlockState(message.currentPos);
                        Block block = state.getBlock();
                        if (block instanceof BlockGrave) {
                            if (player.getServer() != null && player.getServer().isDedicatedServer()) {
                                DedicatedServer server = (DedicatedServer) player.getServer();
                                if (server.isBlockProtected(world, message.currentPos, player)) {
                                    block.onBlockActivated(state, world, message.currentPos, player, Hand.MAIN_HAND, new BlockRayTraceResult(new Vec3d(message.currentPos), Direction.DOWN, message.currentPos, true));
                                }
                            }
                        }
                    }
                });
            }
            ctx.setPacketHandled(true);
        }
    }
}
