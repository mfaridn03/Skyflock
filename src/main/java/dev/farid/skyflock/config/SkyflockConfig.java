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
            subcategory = "ESP"
    )
    public boolean slayerMinibossEsp = false;

    @Property(
            type = PropertyType.SELECTOR,
            name = "ESP type",
            description = "Hitbox: draws a hitbox. Fill: draws a filled hitbox",
            category = "Slayer",
            subcategory = "ESP",
            options = {"Hitbox", "Fill"}
    )
    public int slayerMiniEspType = 0;

    @Property(
            type = PropertyType.SWITCH,
            name = "Keep Rendering ESP",
            description = "Keep ESP turned on if you don't have an active slayer quest",
            category = "Slayer",
            subcategory = "ESP"
    )
    public boolean espWhenQuestInactive = false;

    @Property(
            type = PropertyType.COLOR,
            name = "Normal Miniboss Colour",
            description = "Normal miniboss esp colour",
            category = "Slayer",
            subcategory = "ESP"
    )
    public Color normalMiniColour = new Color(0, 255, 0, 153);

    @Property(
            type = PropertyType.COLOR,
            name = "Rare Miniboss Colour",
            description = "Rare miniboss esp colour",
            category = "Slayer",
            subcategory = "ESP"
    )
    public Color rareMiniColour = new Color(255, 0, 0, 150);

    @Property(
            type = PropertyType.SWITCH,
            name = "Colour Boss",
            description = "Draws a solid box around the boss, coloured depending on the correct attunement",
            category = "Slayer",
            subcategory = "Blaze Slayer"
    )
    public boolean colourBlazeBoss = false;

    @Property(
            type = PropertyType.PERCENT_SLIDER,
            name = "Box Transparency",
            category = "Slayer",
            subcategory = "Blaze Slayer"
    )
    public float boxTransparency = 0.8f;

    @Property(
            type = PropertyType.SWITCH,
            name = "Hide Nearby Blaze",
            description = "Stop rendering nearby blaze when fighting boss",
            category = "Slayer",
            subcategory = "Blaze Slayer"
    )
    public boolean hideNearbyBlaze = false;

    @Property(
            type = PropertyType.SLIDER,
            name = "Blaze Hider Distance",
            description = "Blazes further than this will not be hidden if the above setting is turned on",
            category = "Slayer",
            subcategory = "Blaze Slayer",
            min = 1,
            max = 30
    )
    public int blazeHideDistance = 10;

    @Property(
            type = PropertyType.SWITCH,
            name = "Hide Nearby Particles",
            description = "Stop rendering nearby particles when fighting boss",
            category = "Slayer",
            subcategory = "Blaze Slayer"
    )
    public boolean hideNearbyParticles = false;

    @Property(
            type = PropertyType.SLIDER,
            name = "Particle Hider Distance",
            description = "Particles further than this will not be hidden if the above setting is turned on",
            category = "Slayer",
            subcategory = "Blaze Slayer",
            min = 1,
            max = 30
    )
    public int particleHideDistance = 10;

    @Property(
            type = PropertyType.SWITCH,
            name = "Point To Boss",
            description = "Draws an arrow to your boss",
            category = "Slayer",
            subcategory = "Pointer"
    )
    public boolean pointToBoss = false;

    @Property(
            type = PropertyType.COLOR,
            name = "Arrow Colour",
            category = "Slayer",
            subcategory = "Pointer"
    )
    public Color bossPointerColour = new Color(255, 255, 255, 153);

    @Property(
            type = PropertyType.DECIMAL_SLIDER,
            name = "Arrow Thickness",
            category = "Slayer",
            subcategory = "Pointer",
            minF = 0.5f,
            maxF = 5f
    )
    public float bossPointerThickness = 3f;

    @Property(
            type = PropertyType.SWITCH,
            name = "Highlight Carried",
            description = "Highlight players that you are carrying. To configure carries, run the §a/sfcarry§r command\n\n" +
                    "    §a/sfcarry §2add §f<players>§r - Add a player or multiple players\n" +
                    "    §a/sfcarry §2remove §f<players>§r - Remove a player or multiple players\n" +
                    "    §a/sfcarry §2list§r - Shows the list of people being carried\n" +
                    "    §a/sfcarry §2clear§r - Clears the carry list\n\n" +
                    "Separate players by space when adding multiple names.\n" +
                    "NOTE: player names are §ccase-sensitive!§r Make sure the capitalisation is correct",
            category = "Slayer",
            subcategory = "Carry Helper"
    )
    public boolean highlightCarried = false;

    @Property(
            type = PropertyType.COLOR,
            name = "Carry Box Colour",
            description = "Carried player esp colour",
            category = "Slayer",
            subcategory = "Carry Helper"
    )
    public Color carryBoxColour = new Color(0, 255, 0, 255);

    @Property(
            type = PropertyType.COLOR,
            name = "Carry Name Colour",
            description = "Carried player nametag colour",
            category = "Slayer",
            subcategory = "Carry Helper"
    )
    public Color carryNametagColour = new Color(255, 255, 255, 255);

    @Property(
            type = PropertyType.PARAGRAPH,
            name = "Carried Players",
            category = "Hidden",
            subcategory = "Carry Helper"
    )
    public String carriedPlayers = "";

    // misc
    @Property(
            type = PropertyType.SWITCH,
            name = "Auto Wave",
            description = "Sends 'o/' in chat whenever someone joins your party",
            category = "Misc",
            subcategory = "Party"
    )
    public boolean autoWave = false;

    @Property(
            type = PropertyType.SELECTOR,
            name = "Hide Fire Sale",
            description = "Hide fire sale messages",
            category = "Misc",
            subcategory = "Chat",
            options = {"Disabled", "Hide Completely", "Shorten"}
    )
    public int hideFireSale = 0;


    public SkyflockConfig() {
        super(new File("./config/skyflock.toml"));
        initialize();

        // hidden properties
        hidePropertyIf("carriedPlayers", () -> true);

        // dungeons
        addDependency("setMinibossHealthGui", "minibossHealthDisplay");
        addDependency("minibossBgColour", "minibossHealthDisplay");

        // slayer
        addDependency("espWhenQuestInactive", "slayerMinibossEsp");
        addDependency("slayerMiniEspType", "slayerMinibossEsp");
        addDependency("normalMiniColour", "slayerMinibossEsp");
        addDependency("rareMiniColour", "slayerMinibossEsp");

        addDependency("boxTransparency", "colourBlazeBoss");

        addDependency("blazeHideDistance", "hideNearbyBlaze");
        addDependency("particleHideDistance", "hideNearbyParticles");

        addDependency("bossPointerColour", "pointToBoss");
        addDependency("bossPointerThickness", "pointToBoss");

        addDependency("carryBoxColour", "highlightCarried");
        addDependency("carryNametagColour", "highlightCarried");

        printLogs(null, "Config loaded", false);
    }
}
