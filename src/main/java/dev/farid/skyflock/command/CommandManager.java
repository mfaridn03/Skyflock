package dev.farid.skyflock.command;

import dev.farid.skyflock.command.commands.ConfigCommand;
import dev.farid.skyflock.command.commands.ConfigCommandAlias;
import dev.farid.skyflock.command.commands.SlayerCarryCommand;
import gg.essential.api.EssentialAPI;
import gg.essential.api.commands.Command;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final List<Command> commands = new ArrayList<>();

    public CommandManager() {
        this.commands.add(new ConfigCommand());
        this.commands.add(new ConfigCommandAlias());
        this.commands.add(new SlayerCarryCommand());
    }

    public void init() {
        for (Command cmd : this.commands) {
            EssentialAPI.getCommandRegistry().registerCommand(cmd);
            MinecraftForge.EVENT_BUS.register(cmd);
        }
    }
}
