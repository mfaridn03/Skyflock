package dev.farid.skyflock.features.dungeons;

import dev.farid.skyflock.Skyflock;
import dev.farid.skyflock.features.Feature;
import dev.farid.skyflock.utils.LocationUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MinibossHp extends Feature {

    private final Set<String> minisNames = new HashSet<>(
            Arrays.asList(
                    "Lost Adventurer",
                    "Angry Archaeologist",
                    "Diamond Guy", // angry archaeologist apparently
                    "Shadow Assassin",
                    "King Midas",
                    "Frozen Adventurer"
            )
    );
    private final Map<EntityOtherPlayerMP, EntityWrapper> seenMinis = new HashMap<>();
    private EntityWrapper targetMini;

    public MinibossHp() {
        super("Miniboss HP Display");
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        EntityWrapper temp = this.targetMini;
        if (temp == null || !LocationUtils.inDungeons || event.type != RenderGameOverlayEvent.ElementType.ALL)
            return;

        if (this.targetMini.entity.isDead) {
            this.seenMinis.remove(this.targetMini.entity);
            return;
        }

        FontRenderer fr = mc.fontRendererObj;
        int x = Skyflock.config.minibossHealthGui.x;
        int y = Skyflock.config.minibossHealthGui.y;
        int hpPercent = (int) (temp.getHpPercent() * 100);

        int g = (int)(255 * temp.getHpPercent());
        int r = (int)(255 * (1 - temp.getHpPercent()));
        Color hpColour = new Color(r, g, 0, 255);
        Color bgColour = new Color(0, 15, 100, 120);

        String hpStr = Integer.toString(hpPercent);
        String distanceStr = "Distance: " + Double.parseDouble(String.format("%.1f", temp.entity.getDistanceToEntity(mc.thePlayer)));

        int stringWidth = fr.getStringWidth(temp.name + " " + hpStr + "%");

        Gui.drawRect(x, y, x + stringWidth + 35, y + (fr.FONT_HEIGHT * 2) + 25, bgColour.getRGB());

        fr.drawString(temp.name, x + 25, y + 10, 0xffffffff);
        fr.drawString(" " + hpStr + "%", x + 25 + fr.getStringWidth(temp.name + " "), y + 10, hpColour.getRGB());
        fr.drawString(distanceStr, x + 25, y + 10 + fr.FONT_HEIGHT + 2, 0xffffffff);
        // TODO: HP bar
        GuiInventory.drawEntityOnScreen(x + 12, y + 35, 15, 20f, 10f, temp.entity);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!LocationUtils.inDungeons)
            return;

        List<EntityOtherPlayerMP> entities = mc.theWorld.getEntities(
                EntityOtherPlayerMP.class,
                // temp: stars instead of name (minis can have no star)
                e -> {
                    if (this.minisNames.contains(e.getName()) && mc.thePlayer.canEntityBeSeen(e) && !e.isDead) {
                        if (!this.seenMinis.containsKey(e)) {
                            this.seenMinis.put(e, new EntityWrapper(e, e.getHealth()));
                        }
                        return true;
                    }
                    return false;
                }
        );

        if (entities.isEmpty()) {
            this.targetMini = null;
            return;
        }
        entities.sort(Comparator.comparingDouble(e -> e.getDistance(mc.thePlayer.posX, e.posY, mc.thePlayer.posZ)));
        this.targetMini = this.seenMinis.get(entities.get(0));
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        this.seenMinis.clear();
        this.targetMini = null;
    }

    @Override
    public boolean getConfigStatus() {
        return Skyflock.config.minibossHealthDisplay;
    }

    public class EntityWrapper {
        public float startHP;
        public String name;
        public EntityOtherPlayerMP entity;

        public EntityWrapper(EntityOtherPlayerMP e, float hp) {
            this.startHP = hp;
            this.entity = e;
            this.name = this.entity.getName();
        }

        public float getHpPercent() {
            if (this.entity.getHealth() > this.startHP)
                this.startHP = this.entity.getHealth();

            return Math.min(1.0f, this.entity.getHealth() / startHP);
        }
    }
}
