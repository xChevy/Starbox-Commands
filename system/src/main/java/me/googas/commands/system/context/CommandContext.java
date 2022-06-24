package me.googas.commands.system.context;

import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.flags.FlagArgument;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.system.MessagesProvider;
import me.googas.commands.system.SystemCommand;
import me.googas.commands.system.context.sender.CommandSender;

/**
 * This context to execute {@link me.googas.commands.system.SystemCommand} that does not include
 * anything different from {@link StarboxCommandContext} the {@link #sender} is the static instance
 * of {@link me.googas.commands.system.context.sender.ConsoleCommandSender#INSTANCE}
 */
public class CommandContext implements StarboxCommandContext {

  @NonNull @Getter private final SystemCommand command;
  @NonNull @Getter private final CommandSender sender;
  @NonNull @Getter private final String[] strings;
  @NonNull @Getter private final String string;
  @NonNull @Getter private final ProvidersRegistry<CommandContext> registry;
  @NonNull @Getter private final MessagesProvider messagesProvider;
  @NonNull @Getter private final List<FlagArgument> flags;

  /**
   * Create the command context.
   *
   * @param command the command for which this context was created
   * @param sender the static instance of {@link
   *     me.googas.commands.system.context.sender.ConsoleCommandSender#INSTANCE}
   * @param strings the input strings read from the {@link
   *     me.googas.commands.system.CommandListener}
   * @param string the input strings joined
   * @param registry the registry to parse {@link Object} array to run {@link SystemCommand}
   * @param messagesProvider the messages provider for messages of providers
   * @param flags the flags in the input of the command
   */
  public CommandContext(
      @NonNull SystemCommand command,
      @NonNull CommandSender sender,
      @NonNull String[] strings,
      @NonNull String string,
      @NonNull ProvidersRegistry<CommandContext> registry,
      @NonNull MessagesProvider messagesProvider,
      @NonNull List<FlagArgument> flags) {
    this.command = command;
    this.sender = sender;
    this.strings = strings;
    this.string = string;
    this.registry = registry;
    this.messagesProvider = messagesProvider;
    this.flags = flags;
  }
}
