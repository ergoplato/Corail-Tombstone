package ovh.corail.tombstone.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

import java.util.Locale;

public interface ISubCommand {
    default LiteralArgumentBuilder<CommandSource> literal() {
        return Commands.literal(toString().toLowerCase(Locale.ROOT));
    }
}
