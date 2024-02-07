package dev.farid.skyflock.command.commands;

import dev.farid.skyflock.Skyflock;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.utils.GuiUtil;

import java.util.Objects;

import static dev.farid.skyflock.Skyflock.MODID;

public class ConfigCommand extends Command {

    public ConfigCommand() {
        super(MODID);
    }

    @DefaultHandler
    public void handle() {
        GuiUtil.open(Objects.requireNonNull(Skyflock.config.gui()));
    }
}
