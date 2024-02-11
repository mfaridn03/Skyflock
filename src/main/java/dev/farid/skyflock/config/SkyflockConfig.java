package dev.farid.skyflock.config;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.io.File;

import static dev.farid.skyflock.Skyflock.printLogs;

public class SkyflockConfig extends Vigilant {

    public MoveLocationGui minibossHealthGui = new MoveLocationGui("minibossHealthGui", 550, 120);

    // qol
    @Property(
            type = PropertyType.SWITCH,
            name = "Remove Death Animation",
            description = "Removes entity death animation",
            category = "QOL"
    )
    public boolean noDeathAnim = false;

    // dungeons
    @Property(
            type = PropertyType.SWITCH,
            name = "Miniboss Health Display",
            description = "Displays nearby miniboss hp on screen",
            category = "Dungeons"
    )
    public boolean minibossHealthDisplay = false;

    @Property(
            type = PropertyType.BUTTON,
            name = "Miniboss HP Location",
            description = "Click to move the gui location",
            category = "Dungeons"
    )
    public void setMinibossHealthGui() {
        this.minibossHealthGui.open();
    }

    @Property(
            type = PropertyType.SELECTOR,
            name = "Livid Bossfight Helper",
            description = "Draws an arrow to the correct livid colour, or a tracer to it.§l Use this alongside other livid boss solvers§r\n" +
                    "§cIf tracer mode, disable view bobbing§r otherwise the line will look ugly",
            category = "Dungeons",
            subcategory = "Livid Extras",
            options = {"Disabled", "Arrow", "Tracer"}
    )
    public int pointToCorrectLivid = 0;

    public SkyflockConfig() {
        super(new File("./config/skyflock.toml"));
        initialize();

        printLogs(null, "Config loaded", false);
    }
}
