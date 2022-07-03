package me.googas.commands.jda;

import java.awt.*;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.context.GenericCommandContext;
import me.googas.commands.jda.context.SlashCommandContext;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.jda.result.JdaResult;
import me.googas.commands.jda.result.Result;
import me.googas.commands.jda.result.ResultType;
import me.googas.commands.time.Time;
import me.googas.commands.time.unit.Unit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * The default implementation for {@link ListenerOptions}
 *
 * <ul>
 *   <li>{@link #deleteCommands} whether to delete the message that execute the command
 *   <li>{@link #embedMessages} whether the {@link me.googas.commands.jda.result.Result} of the
 *       command should create an {@link net.dv8tion.jda.api.entities.MessageEmbed}
 *   <li>{@link #deleteErrors} whether the message of the {@link
 *       me.googas.commands.jda.result.Result} when it is a {@link ResultType#isError()} should be
 *       deleted in
 *   <li>{@link #toDeleteErrors} the {@link Time} to delete the message when it is a {@link
 *       ResultType#isError()}
 *   <li>{@link #deleteSuccess} whether the message of the {@link
 *       me.googas.commands.jda.result.Result} when it is not a {@link ResultType#isError()} should
 *       be deleted in
 *   <li>{@link #toDeleteSuccess} the {@link Time} to delete the message when it is not a {@link
 *       ResultType#isError()}
 *   <li>{@link #success} the {@link Color} of the {@link net.dv8tion.jda.api.entities.MessageEmbed}
 *       when the {@link ResultType} is not {@link ResultType#isError()}
 *   <li>{@link #error} the {@link Color} of the {@link net.dv8tion.jda.api.entities.MessageEmbed}
 *       when the {@link ResultType} is {@link ResultType#isError()}
 * </ul>
 */
@Data
public class GenericListenerOptions implements ListenerOptions {

  @NonNull @Getter private String prefix = "-";

  /** Whether to delete the message that execute the command. */
  private boolean deleteCommands = false;
  /**
   * Whether the {@link me.googas.commands.jda.result.Result} of the command should create an {@link
   * net.dv8tion.jda.api.entities.MessageEmbed}
   */
  private boolean embedMessages = true;
  /**
   * Whether the message of the {@link me.googas.commands.jda.result.Result} when it is a {@link
   * ResultType#isError()} should be deleted in
   */
  private boolean deleteErrors = true;
  /** The {@link Time} to delete the message when it is a {@link ResultType#isError()}. */
  @NonNull private Time toDeleteErrors = Time.of(15, Unit.SECONDS);
  /**
   * Whether the message of the {@link me.googas.commands.jda.result.Result} when it is not a {@link
   * ResultType#isError()} should be deleted in
   */
  private boolean deleteSuccess = false;
  /** The {@link Time} to delete the message when it is not a {@link ResultType#isError()}. */
  @NonNull private Time toDeleteSuccess = Time.of(15, Unit.SECONDS);
  /**
   * The {@link Color} of the {@link net.dv8tion.jda.api.entities.MessageEmbed} when the {@link
   * ResultType} is not {@link ResultType#isError()}
   */
  @NonNull private Color success = new Color(0x02e9ff);
  /**
   * The {@link Color} of the {@link net.dv8tion.jda.api.entities.MessageEmbed} when the {@link
   * ResultType} is {@link ResultType#isError()}
   */
  @NonNull private Color error = new Color(0xff0202);
  /**
   * Whether to handle {@link #handle(Throwable, GenericCommandContext)} by sending the error to the
   * {@link net.dv8tion.jda.api.entities.User} that execute the command
   */
  private boolean sendErrors = false;

  /**
   * Set the prefix to differentiate commands.
   *
   * @param prefix the new prefix value
   * @return this same instance
   */
  @NonNull
  public GenericListenerOptions setPrefix(String prefix) {
    this.prefix = prefix;
    return this;
  }

  @NonNull
  private Color getColor(@NonNull ResultType type) {
    if (type.isError()) {
      return this.getError();
    } else {
      return this.getSuccess();
    }
  }

  /**
   * Get the consumer to delete errors. This will delete the {@link Message} and {@link
   * net.dv8tion.jda.api.requests.restaction.AuditableRestAction#queueAfter(long, TimeUnit)} the
   * {@link #toDeleteErrors}
   *
   * @return the consumer to delete errors
   * @see #deleteErrors
   * @see #toDeleteErrors
   */
  public Consumer<Message> getErrorDeleteConsumer() {
    return msg ->
        msg.delete().queueAfter(this.getToDeleteErrors().toMillisRound(), TimeUnit.MILLISECONDS);
  }

  /**
   * Get the consumer to delete success. This will delete the {@link Message} and {@link
   * net.dv8tion.jda.api.requests.restaction.AuditableRestAction#queueAfter(long, TimeUnit)} the
   * {@link #toDeleteSuccess}
   *
   * @return the consumer to delete success
   * @see #deleteSuccess
   * @see #toDeleteSuccess
   */
  public Consumer<Message> getSuccessDeleteConsumer() {
    return msg ->
        msg.delete().queueAfter(this.getToDeleteSuccess().toMillisRound(), TimeUnit.MILLISECONDS);
  }

  @Override
  public void preCommand(
      @NonNull SlashCommandInteractionEvent event,
      @NonNull String name,
      @NonNull String[] strings) {
    // Empty. The slash command interaction will be deleted
  }

  @Override
  public void preCommand(
      @NonNull MessageReceivedEvent event, @NonNull String commandName, @NonNull String[] strings) {
    if (this.isDeleteCommands() && event.getChannelType() != ChannelType.PRIVATE) {
      event.getMessage().delete().queue();
    }
  }

  public Message processResult(Result result, @NonNull CommandContext context) {
    if (result != null && !result.getDiscordMessage().isPresent()) {
      MessageBuilder builder = new MessageBuilder();
      MessagesProvider messagesProvider = context.getMessagesProvider();
      String thumbnail = messagesProvider.thumbnailUrl(context);
      Optional<String> optionalMessage = result.getMessage();
      if (this.isEmbedMessages()) {
        if (optionalMessage.isPresent()) {
          EmbedBuilder embedBuilder =
              new EmbedBuilder()
                  .setTitle(result.getType().getTitle(messagesProvider, context))
                  .setDescription(optionalMessage.get())
                  .setThumbnail(thumbnail.isEmpty() ? null : thumbnail)
                  .setFooter(messagesProvider.footer(context))
                  .setColor(this.getColor(result.getType()));
          return builder.setEmbeds(embedBuilder.build()).build();
        } else {
          return null;
        }
      } else {
        return optionalMessage
            .map(
                s ->
                    builder
                        .append(
                            messagesProvider.response(
                                result.getType().getTitle(messagesProvider, context), s, context))
                        .build())
            .orElse(null);
      }
    } else if (result != null) {
      return result.getDiscordMessage().orElse(null);
    }
    return null;
  }

  public Consumer<Message> processConsumer(Result result, @NonNull CommandContext context) {
    if (result != null) {
      if (result.getSuccess() != null) {
        return result.getSuccess();
      } else if (this.isDeleteErrors() && result.getType().isError()) {
        return this.getErrorDeleteConsumer();
      } else if (this.isDeleteSuccess() && !result.getType().isError()) {
        if (!context.getCommand().getMap().containsKey("excluded")) {
          return this.getSuccessDeleteConsumer();
        }
      }
    }
    return null;
  }

  @Override
  public void handle(@NonNull Throwable fail, @NonNull CommandContext context) {
    if (!this.isSendErrors()) {
      return;
    }
    fail.printStackTrace();
    context
        .getSender()
        .openPrivateChannel()
        .queue(
            channel -> {
              Message message =
                  this.processResult(
                      Result.forType(ResultType.ERROR).setDescription(fail.getMessage()).build(),
                      context);
              channel.sendMessage(message).queue();
            },
            failure -> {});
  }

  @Override
  public @NonNull String getPrefix(Guild guild) {
    return this.prefix;
  }

  @Override
  public void handle(JdaResult jdaResult, @NonNull CommandContext context) {
    if (!(jdaResult instanceof Result))
      throw new IllegalArgumentException(
          jdaResult + " is a result instance which cannot be handled by this options");
    Result result = (Result) jdaResult;
    Message response = this.processResult(result, context);
    Consumer<Message> consumer = this.processConsumer(result, context);
    Optional<MessageChannel> optionalChannel = context.getChannel();
    if (context instanceof SlashCommandContext) {
      SlashCommandInteractionEvent slashEvent = ((SlashCommandContext) context).getEvent();
      if (response != null) {
        if (consumer != null) {
          slashEvent
              .getHook()
              .sendMessage(response)
              .queue(consumer, fail -> this.handle(fail, context));
        } else {
          slashEvent
              .getHook()
              .sendMessage(response)
              .queue(null, fail -> this.handle(fail, context));
        }
      }
    } else {
      if (!optionalChannel.isPresent() || response == null) return;
      MessageChannel channel = optionalChannel.get();
      if (consumer != null) {
        channel.sendMessage(response).queue(consumer, fail -> this.handle(fail, context));
      } else {
        channel.sendMessage(response).queue(null, fail -> this.handle(fail, context));
      }
    }
  }
}
