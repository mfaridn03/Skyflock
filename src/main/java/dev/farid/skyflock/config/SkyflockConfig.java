package dev.farid.skyflock.config;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.awt.*;
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
            category = "Dungeons",
            subcategory = "Miniboss HP"
    )
    public boolean minibossHealthDisplay = false;

    @Property(
            type = PropertyType.BUTTON,
            name = "Miniboss HP Location",
            description = "Click to move the gui location",
            placeholder = "Edit Location",
            category = "Dungeons",
            subcategory = "Miniboss HP"
    )
    public void setMinibossHealthGui() {
        this.minibossHealthGui.open();
    }

    @Property(
            type = PropertyType.COLOR,
            name = "Background Colour",
            description = "Miniboss HP hud colour",
            category = "Dungeons",
            subcategory = "Miniboss HP"
    )
    public Color minibossBgColour = new Color(7, 2, 7, 120);

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

    // slayer
    @Property(
            type = PropertyType.SWITCH,
            name = "Slayer Miniboss ESP",
            description = "Draws a box/highlight around slayer minibosses.\n" +
                    "Some slayer quests (especially at higher tiers) can spawn two types of minibosses: " +
                    "§aNormal§r and §cRare§r minibosses\n\n" +
                    "§cRare§r miniboss (e.g. Voidcrazed Maniac) give much more combat exp than §aNormal§r ones (e.g. Voidling Radical)",
            category = "Slayer",
            subcategory = "Miniboss ESP"
    )
    public boolean slayerMinibossEsp = false;

    @Property(
            type = PropertyType.SELECTOR,
            name = "ESP type",
            description = "Hitbox: draws a hitbox. Fill: draws a filled hitbox",
            category = "Slayer",
            subcategory = "Miniboss ESP",
            options = {"Hitbox", "Fill"}
    )
    public int slayerMiniEspType = 0;

    @Property(
            type = PropertyType.SWITCH,
            name = "Keep Rendering ESP",
            description = "Keep ESP turned on if you don't have an active slayer quest",
            category = "Slayer",
            subcategory = "Miniboss ESP"
    )
    public boolean espWhenQuestInactive = false;

    @Property(
            type = PropertyType.COLOR,
            name = "Normal Miniboss Colour",
            description = "Normal miniboss esp colour",
            category = "Slayer",
            subcategory = "Miniboss ESP"
    )
    public Color normalMiniColour = new Color(0, 255, 0, 153);

    @Property(
            type = PropertyType.COLOR,
            name = "Rare Miniboss Colour",
            description = "Rare miniboss esp colour",
            category = "Slayer",
            subcategory = "Miniboss ESP"
    )
    public Color rareMiniColour = new Color(255, 0, 0, 150);

    public SkyflockConfig() {
        super(new File("./config/skyflock.toml"));
        initialize();

        // dungeons
        addDependency("setMinibossHealthGui", "minibossHealthDisplay");
        addDependency("minibossBgColour", "minibossHealthDisplay");

        // slayer
        addDependency("espWhenQuestInactive", "slayerMinibossEsp");
        addDependency("slayerMiniEspType", "slayerMinibossEsp");
        addDependency("normalMiniColour", "slayerMinibossEsp");
        addDependency("rareMiniColour", "slayerMinibossEsp");

        printLogs(null, "Config loaded", false);
    }
}
