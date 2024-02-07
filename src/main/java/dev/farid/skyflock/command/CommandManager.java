package dev.farid.skyflock.command;

import gg.essential.api.EssentialAPI;
import gg.essential.api.commands.Command;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final List<Command> commands = new ArrayList<>();

    public CommandManager() {}

    public void add(Command cmd) {
        this.commands.add(cmd);
    }

    public void registerAll() {
        for (Command cmd : this.commands) {
            EssentialAPI.getCommandRegistry().registerCommand(cmd);
        }
    }
}
