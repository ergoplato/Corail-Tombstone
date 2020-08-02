package ovh.corail.tombstone.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import ovh.corail.tombstone.helper.DeathHandler;
import ovh.corail.tombstone.helper.LangKey;
import ovh.corail.tombstone.helper.Location;
import ovh.corail.tombstone.helper.StyleType;
import ovh.corail.tombstone.registry.ModItems;
import ovh.corail.tombstone.tileentity.TileEntityGrave;

public class CommandTBRestoreInventory extends TombstoneCommand {

    public CommandTBRestoreInventory(CommandDispatcher<CommandSource> commandDispatcher) {
        super(commandDispatcher);
    }

    @Override
    public String getName() {
        return "tbrestoreinventory";
    }

    @Override
    LiteralArgumentBuilder<CommandSource> getBuilder(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(c -> restoreInventory(c.getSource(), c.getSource().asPlayer()));
        builder.then(Commands.argument(PLAYER_PARAM, EntityArgument.player())
                .executes(c -> restoreInventory(c.getSource(), EntityArgument.getPlayer(c, PLAYER_PARAM)))
        );
        return builder;
    }

    private int restoreInventory(CommandSource sender, ServerPlayerEntity target) {
        checkAlive(target);
        checkNotSpectator(target);

        Location lastGrave = DeathHandler.INSTANCE.getLastGrave(target.getGameProfile().getName());
        // check for grave's key on the player if no grave is found
        if (lastGrave.isOrigin()) {
            lastGrave = ModItems.grave_key.getTombPos(ModItems.grave_key.findFirstKeyInInventory(target));
            if (lastGrave.isOrigin()) {
                throw LangKey.MESSAGE_NO_GRAVE.asCommandException();
            }
        }

        DimensionType dimensionType = getOrThrowDimensionType(lastGrave.dim);
        ServerWorld world = sender.getServer().getWorld(dimensionType);
        checkValidPos(world, lastGrave.getPos());

        TileEntity tile = world.getTileEntity(lastGrave.getPos());
        if (tile instanceof TileEntityGrave) {
            ModItems.grave_key.removeKeyForGraveInInventory(target, lastGrave);
            ((TileEntityGrave) tile).giveInventory(target);
            target.sendMessage(LangKey.MESSAGE_RECOVER_LOST_ITEMS.getTranslationWithStyle(StyleType.MESSAGE_SPELL));
            sendMessage(sender, LangKey.MESSAGE_OPEN_GRAVE_SUCCESS.getTranslation(), false);
        }
        return 1;
    }
}
