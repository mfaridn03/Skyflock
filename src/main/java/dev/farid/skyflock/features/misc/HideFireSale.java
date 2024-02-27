package dev.farid.skyflock.features.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.farid.skyflock.Skyflock;
import dev.farid.skyflock.features.Feature;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class HideFireSale extends Feature {

    private boolean hide = false;
    private final List<String> hoverText = new ArrayList<>();
    private final String shortenedMessage = "§6§k§lA§r §c§lFIRE SALE §r§6§k§lA§r §8§o[hover for details]§r";

    public HideFireSale() {
        super("Hide Fire Sale");
    }

    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent event) {
        if (mc.thePlayer == null || !getConfigStatus() || event.type == (byte) 2) return;

        String formatted = event.message.getFormattedText();
        String unformatted = ChatFormatting.stripFormatting(formatted);

        // fire sale start message
        if (formatted.equals("§6§k§lA§r §c§lFIRE SALE §r§6§k§lA§r")) {
            event.setCanceled(true);
            this.hoverText.clear();
            this.hide = true;

            new Thread(
                    () -> {
                        try {
                            Thread.sleep(500);
                        }
                        catch (InterruptedException ignored) {}
                        this.hide = false;
                    }
            ).start();
            return;
        }

        if (!formatted.contains("♨")) return;

        // following messages
        if (!formatted.startsWith("§c") || !unformatted.trim().startsWith("♨") || !this.hide)
            return;

        event.setCanceled(true);

        // if shorten mode
        if (Skyflock.config.hideFireSale == 2) {
            this.hoverText.add(formatted.replaceAll("§a§l\\[WARP]§r", ""));

            if (formatted.matches("§c♨ §eVisit the Community Shop in the next .+ §eto grab yours! .+")) {
                this.hoverText.add("§r§a§lClick to warp to Elizabeth§r");
                // TODO: a TextUtils/PlayerUtils wrapper to easily add chat message components & events
                mc.thePlayer.addChatMessage(
                        new ChatComponentText(this.shortenedMessage).setChatStyle(
                                        new ChatStyle()
                                                .setChatHoverEvent(
                                                        new HoverEvent(
                                                                HoverEvent.Action.SHOW_TEXT,
                                                                new ChatComponentText(String.join("\n", this.hoverText))
                                                        )
                                                )
                                                .setChatClickEvent(
                                                        new ClickEvent(
                                                                ClickEvent.Action.RUN_COMMAND,
                                                                "/warp elizabeth"
                                                        )
                                                )
                        )
                );
            }
        }
    }

    @Override
    public boolean getConfigStatus() {
        // 1 = hide completely, 2 = shorten
        return Skyflock.config.hideFireSale != 0;
    }

}
