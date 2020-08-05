package ovh.corail.tombstone.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.village.VillageSiege;
import net.minecraft.world.Difficulty;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import ovh.corail.tombstone.config.ConfigTombstone;
import ovh.corail.tombstone.helper.LangKey;
import ovh.corail.tombstone.helper.VillageSiegeHandler.CustomVillageSiege;

import static ovh.corail.tombstone.helper.VillageSiegeHandler.SiegeState.SIEGE_START;

public class CommandTBSiege extends TombstoneCommand {

    public CommandTBSiege(CommandDispatcher<CommandSource> commandDispatcher) {
        super(commandDispatcher);
    }

    @Override
    public String getName() {
        return "tbsiege";
    }

    @Override
    LiteralArgumentBuilder<CommandSource> getBuilder(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(c -> launchSiege(c.getSource()));
        return builder;
    }

    private int launchSiege(CommandSource sender) {
        if (!ConfigTombstone.village_siege.handleVillageSiege.get()) {
            throw LangKey.MESSAGE_DISABLED_COMMAND.asCommandException();
        }
        ServerWorld world = DimensionManager.getWorld(sender.getServer(), DimensionType.OVERWORLD, false, false);
        if (world == null) {
            // should never happen
            throw LangKey.MESSAGE_UNLOADED_DIMENSION.asCommandException();
        }
        if (world.getWorldInfo().getDifficulty() == Difficulty.PEACEFUL) {
            throw LangKey.MESSAGE_DIFFICULTY_PEACEFUL.asCommandException();
        }
        if (world.isDaytime()) {
            throw LangKey.MESSAGE_ONLY_AT_NIGHT.asCommandException();
        }
        ChunkGenerator<?> chunkGenerator = world.getChunkProvider().getChunkGenerator();
        if (!(chunkGenerator instanceof OverworldChunkGenerator)) {
            throw new CommandException(new StringTextComponent("The Overworld chunk generator is not the vanilla one"));
        }
        VillageSiege villageSiege = ((OverworldChunkGenerator) chunkGenerator).siegeSpawner;
        if (villageSiege instanceof CustomVillageSiege) {
            CustomVillageSiege currentSiege = (CustomVillageSiege) villageSiege;
            switch (currentSiege.siegeState) {
                case SIEGE_START:
                case SPAWN_MOBS:
                    throw LangKey.MESSAGE_START_SIEGE_FAILED.asCommandException();
                case SIEGE_END:
                    currentSiege.hasFailedTrySiege = false;
                    currentSiege.siegeState = SIEGE_START;
                    sendMessage(sender, LangKey.MESSAGE_START_SIEGE_SUCCESS.getText(), false);
            }
        }
        return 1;
    }
}
