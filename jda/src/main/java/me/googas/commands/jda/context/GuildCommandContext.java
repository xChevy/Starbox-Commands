package me.googas.commands.jda.context;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.flags.FlagArgument;
import me.googas.commands.jda.CommandManager;
import me.googas.commands.jda.JdaCommand;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * This context is used when the command is executed inside a guild. The context is still a
 * {@link User} but you can also get the {@link Member}
 */
public class GuildCommandContext extends GenericCommandContext {

  /** The sender of the command as a member. */
  @NonNull @Getter private final Member member;
  /** The guild where the command was executed. */
  @NonNull @Getter private final Guild guild;

  /**
   * Create an instance.
   *
   * @param jda the jda instance in which the {@link CommandManager} is registered
   * @param command the command for which this context was created
   * @param sender the sender of the command
   * @param string the input strings joined
   * @param args the strings send in the command
   * @param registry the registry of the command context
   * @param messagesProvider the messages' provider for this context
   * @param flags the flags in the input of the command
   * @param event the event of the message that executes the command
   * @param channel the channel where the command was executed
   * @param message the message where the command was executed
   */
  public GuildCommandContext(
      @NonNull JDA jda,
      @NonNull JdaCommand command,
      @NonNull User sender,
      @NonNull String string,
      @NonNull String[] args,
      @NonNull ProvidersRegistry<CommandContext> registry,
      @NonNull MessagesProvider messagesProvider,
      @NonNull List<FlagArgument> flags,
      @NonNull MessageReceivedEvent event,
      @NonNull MessageChannel channel,
      @NonNull Message message) {
    super(
        jda,
        command,
        sender,
        string,
        args,
        registry,
        messagesProvider,
        flags,
        event,
        channel,
        message);
    this.member =
        Objects.requireNonNull(
            message.getMember(), "Guild command context must have a valid member");
    this.guild = message.getGuild();
  }

  @Override
  public String toString() {
    return "GuildCommandContext{" + "member=" + this.member + ", guild=" + this.guild + '}';
  }

  @Override
  public @NonNull GuildCommandContext getChildren() {
    return new GuildCommandContext(
        this.jda,
        this.command,
        this.sender,
        this.string,
        Arrays.copyOfRange(strings, 1, strings.length),
        this.registry,
        this.messagesProvider,
        this.flags,
        this.event,
        this.channel,
        this.message);
  }
}
