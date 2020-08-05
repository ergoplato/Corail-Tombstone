package ovh.corail.tombstone.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.GameData;
import ovh.corail.tombstone.helper.CallbackHandler;
import ovh.corail.tombstone.helper.EntityHelper;
import ovh.corail.tombstone.helper.Helper;
import ovh.corail.tombstone.helper.LangKey;
import ovh.corail.tombstone.helper.StyleType;

@SuppressWarnings("deprecation")
abstract class TombstoneCommand {
    private final CommandDispatcher<CommandSource> commandDispatcher;

    public TombstoneCommand(CommandDispatcher<CommandSource> commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    abstract String getName();

    private ITextComponent getUsage() {
        return new TranslationTextComponent("tombstone.command." + getName() + ".usage");
    }

    public int getPermissionLevel() {
        return 2;
    }

    public int showUsage(CommandSource source) {
        source.sendFeedback(getUsage(), false);
        return 1;
    }

    abstract LiteralArgumentBuilder<CommandSource> getBuilder(LiteralArgumentBuilder<CommandSource> builder);

    public void registerCommand() {
        this.commandDispatcher.register(getBuilder(Commands.literal(getName()).requires(p -> p.hasPermissionLevel(getPermissionLevel()))));
    }

    static void checkAlive(Entity entity) {
        if (!entity.isAlive()) {
            throw LangKey.MESSAGE_DEAD_ENTITY.asCommandException();
        }
    }

    static void checkNotSpectator(Entity entity) {
        if (entity instanceof PlayerEntity) {
            checkNotSpectator((PlayerEntity) entity);
        }
    }

    static void checkNotSpectator(PlayerEntity player) {
        if (EntityHelper.isValidPlayer(player) && player.isSpectator()) {
            throw LangKey.MESSAGE_PLAYER_SPECTATOR.asCommandException();
        }
    }

    static void checkValidPos(World world, BlockPos pos) {
        if (!Helper.isValidPos(world, pos)) {
            throw LangKey.MESSAGE_INVALID_LOCATION.asCommandException();
        }
    }

    DimensionType getOrThrowDimensionType(int dim) {
        DimensionType dimensionType = DimensionType.getById(dim);
        if (dimensionType == null) {
            throw LangKey.MESSAGE_NO_DIMENSION.asCommandException();
        }
        return dimensionType;
    }

    Biome getOrThrowBiome(CommandContext<CommandSource> context, String name) {
        ResourceLocation rl = context.getArgument(name, ResourceLocation.class);
        return Registry.BIOME.getValue(rl).orElseThrow(LangKey.MESSAGE_INVALID_BIOME::asCommandException);
    }

    protected void sendMessage(CommandSource source, IFormattableTextComponent message, boolean allowLogging) {
        source.sendFeedback(message.setStyle(StyleType.TOOLTIP_DESC), allowLogging);
    }

    void runNextTick(Runnable runnable) {
        CallbackHandler.addCallback(1, runnable);
    }

    private static CommandExceptionType createCommandExceptionType(LangKey langKey) {
        return new SimpleCommandExceptionType(langKey.getText());
    }

    private static CommandExceptionType createDynamicCommandExceptionType(LangKey langKey) {
        return new DynamicCommandExceptionType(langKey::getText);
    }

    static final String PLAYER_PARAM = "player";
    static final String TARGET_PARAM = "target";
    static final String UUID_PARAM = "uuid";
    static final String BIOME_PARAM = "biome";
    static final String STRUCTURE_PARAM = "structure";
    static final String DIM_PARAM = "dim";
    static final String AMOUNT_PARAM = "amount";
    static final SuggestionProvider<CommandSource> SUGGESTION_STRUCTURE = (ctx, build) -> ISuggestionProvider.suggestIterable(GameData.getStructureFeatures().keySet(), build);
    static final SuggestionProvider<CommandSource> SUGGESTION_BIOME = (ctx, build) -> ISuggestionProvider.suggestIterable(Registry.BIOME.keySet(), build);
    static final SuggestionProvider<CommandSource> SUGGESTION_DIM_IDS = (ctx, build) -> ISuggestionProvider.suggest(DimensionManager.getRegistry().stream().map(dim -> String.valueOf(dim.getId())), build);
    static final SuggestionProvider<CommandSource> AMOUNT_SUGGESTION = (ctx, build) -> build.suggest(1, () -> "[0-MAX]").buildFuture();
}
