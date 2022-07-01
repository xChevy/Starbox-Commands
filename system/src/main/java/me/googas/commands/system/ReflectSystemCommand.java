package me.googas.commands.system;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.Middleware;
import me.googas.commands.ReflectCommand;
import me.googas.commands.arguments.Argument;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.exceptions.MissingArgumentException;
import me.googas.commands.flags.Option;
import me.googas.commands.messages.StarboxMessagesProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.system.context.CommandContext;
import me.googas.commands.system.context.sender.CommandSender;
import me.googas.commands.util.Strings;

/**
 * This is the direct extension of {@link SystemCommand} for reflection commands this is returned
 * from {@link CommandManager#parseCommands(Object)}.
 *
 * <p>The methods that are annotated with {@link Command} represent of this commands
 */
public class ReflectSystemCommand
    implements SystemCommand, ReflectCommand<CommandContext, SystemCommand> {

  @NonNull @Getter private final CommandManager manager;
  @NonNull @Getter private final List<String> aliases;
  @NonNull @Getter private final List<Option> options;
  @NonNull @Getter private final List<Middleware<CommandContext>> middlewares;
  @NonNull @Getter private final Method method;
  @NonNull @Getter private final Object object;
  @NonNull @Getter private final List<Argument<?>> arguments;
  @NonNull @Getter private final List<SystemCommand> children;
  private final CooldownManager cooldown;

  /**
   * Create the command.
   *
   * @param manager the manager that parsed the command
   * @param aliases the aliases that match the command for its execution
   * @param options the flags that apply in this command
   * @param middlewares the middlewares to run before and after this command is executed
   * @param method the method to execute as the command see more in {@link #getMethod()}
   * @param object the instance of the object used to invoke the method see more in {@link
   *     #getObject()}
   * @param arguments the list of arguments that are used to {@link
   *     #getObjects(StarboxCommandContext)} and invoke the {@link #getMethod()}
   * @param children the list of children commands which can be used with this parent prefix. Learn
   *     more in {@link me.googas.commands.annotations.Parent}
   * @param cooldown the manager that handles the cooldown in this command
   */
  public ReflectSystemCommand(
      @NonNull CommandManager manager,
      @NonNull List<String> aliases,
      @NonNull List<Option> options,
      @NonNull List<Middleware<CommandContext>> middlewares,
      @NonNull Method method,
      @NonNull Object object,
      @NonNull List<Argument<?>> arguments,
      @NonNull List<SystemCommand> children,
      CooldownManager cooldown) {
    this.middlewares = middlewares;
    this.method = method;
    this.object = object;
    this.arguments = arguments;
    this.manager = manager;
    this.aliases = aliases;
    this.children = children;
    this.options = options;
    this.cooldown = cooldown;
  }

  @Override
  public Result execute(@NonNull CommandContext context) {
    return SystemCommand.super.execute(context);
  }

  @Override
  public Result run(@NonNull CommandContext context) {
    CommandSender sender = context.getSender();
    try {
      Object object = this.method.invoke(this.getObject(), this.getObjects(context));
      if (object instanceof Result) {
        return (Result) object;
      } else {
        return null;
      }
    } catch (final IllegalAccessException e) {
      e.printStackTrace();
      return new Result("IllegalAccessException, e");
    } catch (final InvocationTargetException e) {
      final String message = e.getMessage();
      if (message != null && !message.isEmpty()) {
        return new Result("{0}");
      } else {
        e.printStackTrace();
        return new Result("InvocationTargetException, e");
      }
    } catch (MissingArgumentException | ArgumentProviderException e) {
      return new Result(e.getMessage());
    }
  }

  @Override
  public @NonNull String getUsage() {
    return this.manager.getListener().getPrefix()
        + Strings.buildUsageAliases(this.getAliases())
        + Argument.generateUsage(this.getArguments());
  }

  @Override
  public @NonNull ProvidersRegistry<CommandContext> getRegistry() {
    return this.getManager().getProvidersRegistry();
  }

  @Override
  public @NonNull StarboxMessagesProvider<CommandContext> getMessagesProvider() {
    return this.getManager().getMessagesProvider();
  }

  @Override
  public boolean hasAlias(@NonNull String alias) {
    for (String name : this.getAliases()) {
      if (name.equalsIgnoreCase(alias)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public @NonNull Optional<CooldownManager> getCooldownManager() {
    return Optional.ofNullable(cooldown);
  }
}
