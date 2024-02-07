package dev.farid.skyflock.config;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.io.File;

import static dev.farid.skyflock.Skyflock.printLogs;

public class SkyflockConfig extends Vigilant {

    @Property(
            type = PropertyType.SWITCH,
            name = "Remove Death Animation",
            description = "Removes entity death animation",
            category = "QOL"
    )
    public boolean noDeathAnim = false;

    public SkyflockConfig() {
        super(new File("./config/skyflock.toml"));
        initialize();

        printLogs(null, "Config loaded", false);
    }
}
