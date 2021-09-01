package me.googas.commands.jda.providers;

import java.util.List;
import lombok.NonNull;
import me.googas.commands.StarboxCommandManager;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.jda.providers.type.JdaArgumentProvider;
import me.googas.commands.jda.providers.type.JdaExtraArgumentProvider;
import net.dv8tion.jda.api.entities.User;

/** Provides the {@link StarboxCommandManager} with a {@link User}. */
public class UserProvider implements JdaArgumentProvider<User>, JdaExtraArgumentProvider<User> {

  @NonNull private final MessagesProvider messagesProvider;

  /**
   * Create an instance.
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public UserProvider(@NonNull MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull Class<User> getClazz() {
    return User.class;
  }

  public static long getIdFromMention(@NonNull String mention) {
    StringBuilder builder = new StringBuilder();
    for (char c : mention.toCharArray()) {
      if (Character.isDigit(c)) builder.append(c);
    }
    return Long.parseLong(builder.toString());
  }

  @Override
  public @NonNull User getObject(@NonNull CommandContext context) {
    return context.getSender();
  }

  @NonNull
  @Override
  public User fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    User user = null;
    try {
      user = context.getJda().getUserById(UserProvider.getIdFromMention(string));
    } catch (NumberFormatException e) {
      List<User> usersByName = context.getJda().getUsersByName(string, true);
      if (!usersByName.isEmpty()) user = usersByName.get(0);
    }
    if (user != null) {
      return user;
    } else {
      throw new ArgumentProviderException(
          context.getMessagesProvider().invalidUser(string, context));
    }
  }
}
