package dev.farid.skyflock.command.commands;

import dev.farid.skyflock.Skyflock;
import dev.farid.skyflock.events.PacketEvent;
import dev.farid.skyflock.utils.SlayerUtils;
import dev.farid.skyflock.utils.TextUtils;
import gg.essential.api.commands.*;
import gg.essential.universal.UChat;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlayerCarryCommand extends Command {

    public SlayerCarryCommand() {
        super("sfcarry");
    }

    @DefaultHandler
    public void handle() {
        UChat.chat(Skyflock.CHAT_PREFIX + "Usage:\n§2/sfcarry §3add §a<players>§r - add player(s). Separate by a space\n" +
                "§2/sfcarry §3remove §a<players>§r - remove player(s). Separate by a space\n" +
                "§2/sfcarry §3clear§r - clears player carry list\n" +
                "§2/sfcarry §3list§r - shows the list of carried players");
    }

    @SubCommand(value = "add", description = "Add player(s) to the carry list. Separate names by space")
    public void sAdd(@Greedy @DisplayName("playerNames") String playerNames) {
        editList(playerNames.split(" "), 1);
    }

    @SubCommand(value = "remove", description = "Removes player(s) from carry list. Separate names by space")
    public void sRemove(@Greedy @DisplayName("playerNames") String playerNames) {
        editList(playerNames.split(" "), 0);
    }

    @SubCommand(value = "clear", description = "Clears the carried players list")
    public void sClear() {
        if (SlayerUtils.carriedPlayers.isEmpty()) return;

        SlayerUtils.carriedPlayers.clear();
        Skyflock.config.carriedPlayers = "";
        Skyflock.forceSaveConfig();
        UChat.chat(Skyflock.CHAT_PREFIX + "Cleared carry list");
    }

    @SubCommand(value = "list", description = "Shows the list of players being carried")
    public void sList() {
        List<String> c = getCurrentCarries();
        UChat.chat(Skyflock.CHAT_PREFIX + (c.isEmpty() ? "Nobody being carried" : "Carry list: §7" + String.join("§r, §7", c)));
    }

    private void editList(String[] playersToProcess, int mode) {
        // 1 = add, 0 = remove
        List<String> currentPlayers = getCurrentCarries();
        List<String> modified = new ArrayList<>();

        for (String p : playersToProcess) {
            String cleanName = TextUtils.removeUnicode(p);
            if (cleanName.isEmpty()) continue;

            if (mode == 1) {
                if (currentPlayers.stream().noneMatch(entry -> entry.equalsIgnoreCase(cleanName))) {
                    modified.add(cleanName);
                    SlayerUtils.addCarry(cleanName);
                    currentPlayers.add(cleanName);
                }
            }

            else if (mode == 0) {
                currentPlayers.removeIf(entry -> {
                    if (entry.equalsIgnoreCase(cleanName)) {
                        modified.add(entry);
                        SlayerUtils.removeCarry(entry);
                        return true;
                    }
                    return false;
                });
            }
        }

        if (modified.isEmpty()) return;

        UChat.chat(Skyflock.CHAT_PREFIX + (mode == 1 ? "Added " : "Removed ") + "the following players: §c" + String.join("§r, §c", modified));


        Skyflock.config.carriedPlayers = String.join(";", currentPlayers);
        Skyflock.forceSaveConfig();
    }

    private List<String> getCurrentCarries() {
        List<String> empty = new ArrayList<>();
        for (String s : Skyflock.config.carriedPlayers.split(";")) {
            if (s.isEmpty()) continue;
            empty.add(s);
        }
        return empty;
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!Skyflock.config.clearOnDisband) return;

        String formatted = event.message.getFormattedText();
        if (formatted.matches(".+§r§ehas disbanded the party!§r") || formatted.matches("§cThe party was disbanded because all invites expired and the party was empty\\.§r")) {
            new Thread(() -> {
                try {
                    // wait slight delay so it doesn't mess up chat
                    Thread.sleep(50);
                }
                catch (InterruptedException ignored) {}
                sClear();
            }).start();
        }
    }

    @SubscribeEvent
    public void onPacketOut(PacketEvent.Outgoing event) {
        if (event.packet instanceof C14PacketTabComplete && Skyflock.config.highlightCarried) {
            C14PacketTabComplete p = (C14PacketTabComplete) event.packet;

            // lazy player tab completion lol
            Pattern pattern = Pattern.compile("^/sfcarry (add|remove)(.+)");
            Matcher matcher = pattern.matcher(p.getMessage());

            if (matcher.matches() && matcher.groupCount() == 2) {
                String cont = matcher.group(2);

                // instead of modifying incoming packet, spoof outgoing packet so that it returns a list of
                // valid players by pretending to auto complete /wdr command
                event.setCanceled(true);
                Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C14PacketTabComplete("/wdr" + cont));
            }
        }
    }
}
