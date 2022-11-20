package chevyself.github.commands.system;

import chevyself.github.commands.system.context.CommandContext;
import chevyself.github.commands.time.TimeUtil;
import java.time.Duration;
import lombok.NonNull;

/** The default {@link MessagesProvider} for System commands. */
public class SystemMessagesProvider implements MessagesProvider {

  @Override
  public @NonNull String invalidLong(@NonNull String string, @NonNull CommandContext context) {
    return String.format("%s is not a valid long!", string);
  }

  @Override
  public @NonNull String invalidInteger(@NonNull String string, @NonNull CommandContext context) {
    return String.format("%s is not a valid integer!", string);
  }

  @Override
  public @NonNull String invalidDouble(@NonNull String string, @NonNull CommandContext context) {
    return String.format("%s is not a valid double!", string);
  }

  @Override
  public @NonNull String invalidBoolean(@NonNull String string, @NonNull CommandContext context) {
    return String.format("%s is not a valid boolean!", string);
  }

  @Override
  public @NonNull String invalidDuration(@NonNull String string, @NonNull CommandContext context) {
    return String.format("%s is not valid time!", string);
  }

  @Override
  public @NonNull String missingArgument(
      @NonNull String name, @NonNull String description, int position, CommandContext context) {
    return String.format("Missing the argument %s (%s) in %d", name, description, position);
  }

  @Override
  public @NonNull String missingStrings(
      @NonNull String name,
      @NonNull String description,
      int position,
      int minSize,
      int missing,
      @NonNull CommandContext context) {
    return String.format(
        "Missing arguments of %s (%s) in %d missing: %d", name, description, position, missing);
  }

  @Override
  public @NonNull String cooldown(@NonNull CommandContext context, @NonNull Duration timeLeft) {
    return "You are not allowed to run this command for another " + TimeUtil.toString(timeLeft);
  }
}
