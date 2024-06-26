package dev.farid.skyflock.features.beastiary;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.farid.skyflock.Skyflock;
import dev.farid.skyflock.features.Feature;
import dev.farid.skyflock.utils.LocationUtils;
import dev.farid.skyflock.utils.RenderUtils;
import kotlin.Triple;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.List;
import java.util.*;

public class Golems extends Feature {

    private int stage = -1; // 0 = not found, 1234 = stages, 5 = spawning, 6 = fighting, -1 = reset
    private int index = 0;
    private long spawnTime = -1;
    private long lastDeadTime = -1;
    private boolean stopScanning = false;
    private final List<Triple<Integer, Integer, String>> xzLocations = Arrays.asList(
            new Triple<>(-644, -269, "Front"),
            new Triple<>(-639, -328, "Front Right"),
            new Triple<>(-678, -332, "Back Right"),
            new Triple<>(-727, -284, "Back"), // haha wysi
            new Triple<>(-689, -273, "Altar"),
            new Triple<>(-649, -219, "Left")
    );
    private final Map<String, Integer> scanStatus = new HashMap<>(); // -1 = not loaded, 0 = not found, 1 = found
    private String location = null;

    public Golems() {
        super("Golem Helper");
        /* Features
        - Scanner DONE
        - TODO: Timer
        - TODO: Hide mobs, players when spawn
        - TODO: Golem highlight (mixin)
        - TODO: Arrow curve path?
         */
        reset();
    }

    public void reset() {
        this.stage = -1;
        this.spawnTime = -1L;
        this.stopScanning = false;
        this.location = null;

        // default values
        this.scanStatus.put("Front", -1);
        this.scanStatus.put("Front Right", -1);
        this.scanStatus.put("Back Right", -1);
        this.scanStatus.put("Back", -1);
        this.scanStatus.put("Altar", -1);
        this.scanStatus.put("Left", -1);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        reset();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        // scanner
        if (!LocationUtils.inSkyblock || !getConfigStatus()) return;
        if (!inEnd()) return;
        if (this.stopScanning || System.currentTimeMillis() - this.lastDeadTime < 2000) return;

        // scan one location per tick
        this.index = (this.index + 1) % 6; // 6 = xzLocations size
        Triple<Integer, Integer, String> loc = this.xzLocations.get(this.index);
        int x = loc.getFirst();
        int z = loc.getSecond();
        String area = loc.getThird();

        // don't continue if chunk is not loaded
        if (mc.theWorld.isAirBlock(new BlockPos(x, 4, z)))
            return;

        // otherwise proceed normally
        for (int y = 5; y <= 9; y++) {
            IBlockState state = mc.theWorld.getBlockState(new BlockPos(x, y, z));

            if (state.getBlock() instanceof BlockSkull) {
                // golem found
                this.scanStatus.put(area, 1);
                this.stage = y - 4;
                this.stopScanning = true;
                this.location = "§a" + area;

                // TODO: display title & play sound option
                if (Skyflock.config.golemStage4Warning && (this.stage == 4 || this.stage == 5))
                    RenderUtils.Render2D.showTitle("§cStage " + this.stage, area, 1, 30, 1);
                return;
            }
        }

        // armour stand check
        List<EntityArmorStand> stands = mc.theWorld.getEntities(EntityArmorStand.class, e -> e.getName().contains("Endstone Protector"));
        if (!stands.isEmpty()) {
            // sort by entity distance to each location
            EntityArmorStand golem = stands.get(0);
            List<Triple<Integer, Integer, String>> copy = new ArrayList<>(this.xzLocations);
            copy.sort(Comparator.comparingDouble(e -> golem.getDistance(e.getFirst(), golem.posY, e.getSecond())));

            String trueArea = copy.get(0).getThird();
            this.scanStatus.put(trueArea, 1);
            this.stage = 6;
            this.stopScanning = true;
            this.location = "§a" + trueArea;

            if (Skyflock.config.golemStage5Warning)
                RenderUtils.Render2D.showTitle("§cFight Started!", area, 1, 30, 1);
            return;
        }
        this.scanStatus.put(area, 0);

        // check if all scanStatus is 0 (loaded, not found). If it is, then no golem found
        if (this.scanStatus.values().stream().allMatch(v -> v == 0)) {
            this.stopScanning = true;
            this.stage = 0;
            this.location = "§7Dead";
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (!LocationUtils.inSkyblock || !getConfigStatus() || event.type == (byte) 2) return;
        if (!inEnd()) return;

        String unfMsg = ChatFormatting.stripFormatting(event.message.getFormattedText());

        switch (unfMsg) {
            // tremor message
            case "You feel a tremor from beneath the earth!":
                // if no golem found, re-scan
                if (this.stage == 0)
                    this.stopScanning = false;
                else
                    // if golem found, increment stage by 1
                    this.stage += 1;

                if (this.stage == 4 && Skyflock.config.golemStage4Warning)
                    RenderUtils.Render2D.showTitle("§cStage " + this.stage, this.location, 1, 30, 1);
                break;

            // stage 5 message
            case "The ground begins to shake as an Endstone Protector rises from below!":
                this.spawnTime = System.currentTimeMillis() + (20 * 1000L);
                this.stage = 5;
                if (Skyflock.config.golemStage5Warning)
                    RenderUtils.Render2D.showTitle("§cStage 5!", this.location, 1, 30, 1);
                break;

            // dead message
            case "                    ENDSTONE PROTECTOR DOWN!":
                this.lastDeadTime = System.currentTimeMillis();

                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                    reset();
                    this.stage = 0;
                }).start();
                break;

            // just spawned
            case "BEWARE - An Endstone Protector has risen!":
                this.spawnTime = -1L;
                this.stage = 6;
                break;
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (!getConfigStatus() || event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        if (!inEnd())
            return;
        if (mc.currentScreen == Skyflock.config.golemLocationGui)
            return;

        int x = Skyflock.config.golemLocationGui.x;
        int y = Skyflock.config.golemLocationGui.y;
        FontRenderer fr = mc.fontRendererObj;

        fr.drawString("§lGolem:§r " + (this.location == null ? "§cNot Loaded (out of reach)" : this.location), x, y, Color.white.getRGB(), true);
        switch (this.stage) {
            case -1:
                fr.drawString("§lStage:§r §cUnknown", x, y + fr.FONT_HEIGHT + 2, Color.white.getRGB(), true);
                break;

            case 0:
                fr.drawString("§lStage:§r §c0", x, y + fr.FONT_HEIGHT + 2, Color.white.getRGB(), true);
                break;

            case 6:
                fr.drawString("§lStage:§r §eFighting!", x, y + fr.FONT_HEIGHT + 2, Color.white.getRGB(), true);
                break;

            default:
                fr.drawString("§lStage:§r §a" + this.stage, x, y + fr.FONT_HEIGHT + 2, Color.white.getRGB(), true);
                break;
        }
    }

    public boolean inEnd() {
        // TEMP: area check better
        if (Skyflock.config.golemScanNestOnly)
            return LocationUtils.location != null && LocationUtils.location.equals("Dragon's Nest");

        return LocationUtils.location != null && (
                LocationUtils.location.equals("Dragon's Nest") ||
                        LocationUtils.location.equals("The End") ||
                        LocationUtils.location.equals("Void Sepulture") ||
                        LocationUtils.location.equals("Zealot Bruiser Hideout") ||
                        LocationUtils.location.equals("Void Slate")
        );
    }

    @Override
    public boolean getConfigStatus() {
        return Skyflock.config.toggleGolems;
    }
}
