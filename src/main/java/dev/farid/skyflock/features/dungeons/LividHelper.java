package dev.farid.skyflock.features.dungeons;

import dev.farid.skyflock.Skyflock;
import dev.farid.skyflock.features.Feature;
import dev.farid.skyflock.utils.DungeonUtils;
import dev.farid.skyflock.utils.RenderUtils;
import gg.essential.universal.UChat;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class LividHelper extends Feature {

    private EntityOtherPlayerMP correctLivid = null;
    private Color lividColour =  new Color(255, 255, 255, 255);
    private long lastLividFound = -1L;

    public LividHelper() {
        super("Livid Helper");
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        this.correctLivid = null;
        this.lastLividFound = -1L;
        this.lividColour = new Color(255, 255, 255, 255);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!getConfigStatus()) return;
        if (!floorCheck()) return;

        IBlockState blockState = mc.theWorld.getBlockState(new BlockPos(5, 108, 42));
        if (blockState.getProperties().isEmpty())
            return;

        EnumDyeColor colour = blockState.getValue(BlockStainedGlass.COLOR);
        String lividName = getLividNameFromColour(colour);

        Color c = Color.getColor(null, colour.getMapColor().colorValue);
        this.lividColour = new Color(c.getRed(), c.getGreen(), c.getBlue(), 255);

        if (lividName != null) {
            // find livid
            List<EntityOtherPlayerMP> stands = mc.theWorld.getEntities(
                    EntityOtherPlayerMP.class,
                     e -> e.getName().equals(lividName)
            );

            if (stands.size() == 1)
                this.correctLivid = stands.get(0);
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (!getConfigStatus() || !floorCheck()) return;
        if (this.correctLivid == null || this.correctLivid.isDead) return;

        if (this.lastLividFound < -0.1) {
            this.lastLividFound = System.currentTimeMillis();
            return;
        }

        // don't render until for a bit until
        if (System.currentTimeMillis() - this.lastLividFound < 1500L)
            return;

        if (Skyflock.config.pointToCorrectLivid == 1) {
            RenderUtils.Render3D.drawArrowToEntity(
                    this.correctLivid,
                    this.lividColour,
                    6f,
                    event.partialTicks
            );
        }
        else if (Skyflock.config.pointToCorrectLivid == 2){
            RenderUtils.Render3D.drawLines(
                    Arrays.asList(
                            mc.thePlayer.getPositionEyes(event.partialTicks),
                            this.correctLivid.getPositionEyes(event.partialTicks)
                    ),
                    this.lividColour,
                    4f,
                    event.partialTicks,
                    true
            );
        }
    }

    private boolean floorCheck() {
        if (mc.thePlayer == null || mc.theWorld == null)
            return false;

        if (DungeonUtils.currentFloor == null || (!DungeonUtils.currentFloor.equals("F5") && !DungeonUtils.currentFloor.equals("M5")))
            return false;

        return DungeonUtils.boss == DungeonUtils.Boss.LIVID;
    }

    private String getLividNameFromColour(EnumDyeColor colour) {
        String colString;
        // From: https://hypixel-skyblock.fandom.com/wiki/Livid#Boss_Minions
        switch (colour) {
            case WHITE:
                colString = "Vendetta Livid";
                break;

            case MAGENTA:
                colString = "Crossed";
                break;

            case RED:
                colString = "Hockey";
                break;

            case GRAY:
                colString = "Doctor";
                break;

            case GREEN:
                colString = "Frog";
                break;

            case LIME:
                colString = "Smile";
                break;

            case BLUE:
                colString = "Scream";
                break;

            case PURPLE:
                colString = "Purple";
                break;

            case YELLOW:
                colString = "Arcade";
                break;

            default:
                // UChat.chat("No matching livid colour found. Boss might not have started yet");
                return null;
        }
        return colString + " Livid";
    }

    @Override
    public boolean getConfigStatus() {
        return Skyflock.config.pointToCorrectLivid != 0;
    }
}
