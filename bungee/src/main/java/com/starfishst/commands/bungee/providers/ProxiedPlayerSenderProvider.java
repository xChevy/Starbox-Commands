package com.starfishst.commands.bungee.providers;

import com.starfishst.commands.bungee.context.CommandContext;
import com.starfishst.commands.bungee.messages.MessagesProvider;
import com.starfishst.commands.bungee.providers.type.BungeeExtraArgumentProvider;
import me.googas.commands.exceptions.ArgumentProviderException;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/** Provides with the player that executed the command */
public class ProxiedPlayerSenderProvider implements BungeeExtraArgumentProvider<ProxiedPlayer> {

  @NonNull private final MessagesProvider messagesProvider;

  /**
   * Create the provider
   *
   * @param messagesProvider the provider for the error message
   */
  public ProxiedPlayerSenderProvider(@NonNull MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull Class<ProxiedPlayer> getClazz() {
    return ProxiedPlayer.class;
  }

  @NonNull
  @Override
  public ProxiedPlayer getObject(@NonNull CommandContext context) throws ArgumentProviderException {
    if (context.getSender() instanceof ProxiedPlayer) {
      return (ProxiedPlayer) context.getSender();
    } else {
      throw new ArgumentProviderException(messagesProvider.onlyPlayers(context));
    }
  }
}
