package me.googas.commands.bungee.result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.result.IResult;
import me.googas.starbox.Strings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

/** The result that can be send by the execution of the command */
public class Result implements IResult {

  /** The components that will be send after the execution */
  @NonNull @Getter private final List<BaseComponent> components = new ArrayList<>();

  /**
   * Create the result with a component
   *
   * @param component the component to send as result
   */
  public Result(@NonNull BaseComponent component) {
    this.components.add(component);
  }

  /**
   * Create the result with many components
   *
   * @param components the components to send as result
   */
  public Result(@NonNull BaseComponent... components) {
    this.components.addAll(Arrays.asList(components));
  }

  /**
   * Create a result with a text
   *
   * @param text the text to send
   */
  public Result(@NonNull String text) {
    this(new TextComponent(ChatColor.translateAlternateColorCodes('&', text)));
  }

  /**
   * Create the result with a text
   *
   * @param text the text to send but has placeholders that will be changed using the map
   * @param map the map to change the placeholders
   */
  public Result(@NonNull String text, @NonNull HashMap<String, String> map) {
    this(Strings.build(text, map));
  }

  @Override
  public String getMessage() {
    return ComponentSerializer.toString(this.components);
  }
}