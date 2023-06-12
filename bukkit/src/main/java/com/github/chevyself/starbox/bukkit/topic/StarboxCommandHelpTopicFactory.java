package com.github.chevyself.starbox.bukkit.topic;

import com.github.chevyself.starbox.bukkit.BukkitCommandExecutor;
import com.github.chevyself.starbox.bukkit.messages.BukkitMessagesProvider;
import lombok.NonNull;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;

/**
 * This {@link HelpTopicFactory} is used for every time that a {@link StarboxCommand} is registered
 * using {@link CommandManager#register(StarboxBukkitCommand)} adds a {@link
 * StarboxCommandHelpTopic} to the {@link org.bukkit.help.HelpMap}
 */
public class StarboxCommandHelpTopicFactory implements HelpTopicFactory<BukkitCommandExecutor> {

  @NonNull private final BukkitMessagesProvider provider;

  /**
   * Create the factory.
   *
   * @param provider the messages' provider to format the help topics see {@link
   *     StarboxCommandHelpTopic}
   */
  public StarboxCommandHelpTopicFactory(@NonNull BukkitMessagesProvider provider) {
    this.provider = provider;
  }

  @Override
  public @NonNull HelpTopic createTopic(@NonNull BukkitCommandExecutor command) {
    return new StarboxCommandHelpTopic(command.getCommand(), null, this.provider);
  }
}
