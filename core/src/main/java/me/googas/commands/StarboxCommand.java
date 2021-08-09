package me.googas.commands;

import java.util.Collection;
import java.util.Optional;
import lombok.NonNull;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.result.StarboxResult;

/**
 * This class represents a Command which may be executed by an user depending on the implementations
 * it may change.
 *
 * <p>// TODO add command types
 *
 * <p>This allows to have children commands which will be recognized using the first parameter of
 * the command as follows:
 *
 * <p>[prefix][parent] [children].
 *
 * <p>This is to give many outputs using a single command
 *
 * <p>// TODO examples
 *
 * @param <C> the context that is required to run the command
 * @param <T> the type of commands that are allowed as children commands
 */
public interface StarboxCommand<C extends StarboxCommandContext, T extends StarboxCommand<C, T>> {

  /**
   * Execute the command.
   *
   * @param context the context that is required to execute the command
   * @return the result of the command execution
   */
  StarboxResult execute(@NonNull C context);

  /**
   * Check if the command can the command be recognized by the given alias. This is used because
   * commands have names and aliases, instead of asking for the name and aliases of the command just
   * check if the command has the alias
   *
   * <p>TODO example
   *
   * @param alias the alias to check
   * @return true if this command has the given alias
   */
  boolean hasAlias(@NonNull String alias);

  /**
   * Add a children that can be used to run in this.
   *
   * @param command the child command to add
   * @return this same command instance to allow chain methods
   */
  @NonNull
  default StarboxCommand<C, T> addChildren(@NonNull T command) {
    this.getChildren().add(command);
    return this;
  }

  /**
   * Get a children command by an alias.
   *
   * @see StarboxCommand#hasAlias(String)
   * @param alias the alias to match the command
   * @return a {@link Optional} instance wrapping the nullable children
   */
  @NonNull
  default Optional<T> getChildren(@NonNull String alias) {
    return this.getChildren().stream().filter(child -> child.hasAlias(alias)).findFirst();
  }

  /**
   * Get the collection of registered children in this parent. All the children added in this
   * collection add from {@link #addChildren(StarboxCommand)}
   *
   * @return the collection of children
   */
  @NonNull
  Collection<T> getChildren();
}
