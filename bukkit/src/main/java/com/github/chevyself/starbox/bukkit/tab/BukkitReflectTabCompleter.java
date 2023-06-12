package com.github.chevyself.starbox.bukkit.tab;

import com.github.chevyself.starbox.bukkit.commands.BukkitCommand;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.common.tab.ReflectTabCompleter;
import com.github.chevyself.starbox.flags.CommandLineParser;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

public class BukkitReflectTabCompleter
    extends ReflectTabCompleter<CommandContext, BukkitCommand, CommandSender> {

  @Override
  public String getPermission(@NonNull BukkitCommand command) {
    return command.getPermission();
  }

  @Override
  public boolean hasPermission(@NonNull CommandSender sender, @NonNull String permission) {
    return sender.hasPermission(permission);
  }

  @Override
  public @NonNull CommandContext createContext(
      @NonNull CommandLineParser parser,
      @NonNull BukkitCommand command,
      @NonNull CommandSender sender) {
    return new CommandContext(
        parser, command, sender, command.getProvidersRegistry(), command.getMessagesProvider());
  }
}