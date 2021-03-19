package me.googas.commands.providers;

import lombok.NonNull;
import me.googas.commands.EasyCommandManager;
import me.googas.commands.context.ICommandContext;
import me.googas.commands.objects.JoinedStrings;
import me.googas.commands.providers.type.IMultipleArgumentProvider;

/** Provides the {@link EasyCommandManager} with a {@link JoinedStrings} */
public class JoinedStringsProvider<T extends ICommandContext>
    implements IMultipleArgumentProvider<JoinedStrings, T> {

  @NonNull
  @Override
  public JoinedStrings fromStrings(@NonNull String[] strings, @NonNull T context) {
    return new JoinedStrings(strings);
  }

  @Override
  public @NonNull Class<JoinedStrings> getClazz() {
    return JoinedStrings.class;
  }
}
