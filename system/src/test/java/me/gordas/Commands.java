package me.gordas;

import me.googas.commands.annotations.Multiple;
import me.googas.commands.annotations.Required;
import me.googas.commands.system.Command;
import me.googas.commands.system.Result;

public class Commands {

  @Command(aliases = "a")
  public Result a(@Required String b, @Required @Multiple String c) {
    return new Result("b = " + b + ", c = " + c);
  }
}
