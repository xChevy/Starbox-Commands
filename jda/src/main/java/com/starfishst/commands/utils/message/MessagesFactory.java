package com.starfishst.commands.utils.message;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

/** A factory for creating messages easily */
public class MessagesFactory {

  /** The builder for messages */
  private static final MessageBuilder messageBuilder = new MessageBuilder();

  /**
   * Get a message from a string
   *
   * @param string the string to send as message
   * @return a message query built from a string
   */
  public static MessageQuery fromString(@NotNull final String string) {
    return new MessageQuery(MessagesFactory.getMessageBuilder().append(string));
  }

  /**
   * Get a message query from an embed
   *
   * @param embed the embed to use as message
   * @return the embed as message
   */
  public static MessageQuery fromEmbed(@NotNull final MessageEmbed embed) {
    return new MessageQuery(MessagesFactory.getMessageBuilder().setEmbed(embed));
  }

  /**
   * Get the builder used in the factory. (It is empty)
   *
   * @return the builder
   */
  public static @NotNull MessageBuilder getMessageBuilder() {
    return MessagesFactory.messageBuilder.clear();
  }
}
