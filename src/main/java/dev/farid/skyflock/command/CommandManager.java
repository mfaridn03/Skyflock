package dev.farid.skyflock.command;

import dev.farid.skyflock.command.commands.ConfigCommand;
import dev.farid.skyflock.command.commands.ConfigCommandAlias;
import gg.essential.api.EssentialAPI;
import gg.essential.api.commands.Command;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final List<Command> commands = new ArrayList<>();

    public CommandManager() {
        this.commands.add(new ConfigCommand());
        this.commands.add(new ConfigCommandAlias());
    }

    public void init() {
        for (Command cmd : this.commands) {
            EssentialAPI.getCommandRegistry().registerCommand(cmd);
        }
    }
}
