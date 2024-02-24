package dev.farid.skyflock.features.misc;

import dev.farid.skyflock.Skyflock;
import dev.farid.skyflock.features.Feature;
import gg.essential.universal.UChat;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoWave extends Feature {

    public AutoWave() {
        super("Auto Wave");
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (!getConfigStatus() || event.type == (byte) 2) return;
        String msg = event.message.getFormattedText();

        // TODO: wave on kuudra & dungeons party finder, also on self join party
        if (msg.matches("(.+) §r§ejoined the party\\.§r")) {
            new Thread(() ->  {
                try {
                    Thread.sleep(500);
                    UChat.say("/pc o/");
                }
                catch (InterruptedException ignored) {}
            }).start();
        }
    }

    @Override
    public boolean getConfigStatus() {
        return Skyflock.config.autoWave;
    }
}
