package dev.farid.skyflock.command.commands;

import dev.farid.skyflock.Skyflock;
import dev.farid.skyflock.utils.SlayerUtils;
import gg.essential.api.commands.*;
import gg.essential.universal.UChat;

import java.util.ArrayList;
import java.util.List;

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

    @SubCommand(value = "add", description = "Add a player to the carry list. Separate by spaces")
    public void sAdd(@Greedy @DisplayName("playerNames") String playerNames) {
        editList(playerNames.split(" "), 1);
    }

    @SubCommand(value = "remove", description = "Removes a player from carry list")
    public void sRemove(@Greedy @DisplayName("playerNames") String playerNames) {
        editList(playerNames.split(" "), 0);
    }

    @SubCommand(value = "clear", description = "Clears the carried players list")
    public void sClear() {
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
            if (p.isEmpty()) continue;

            if (mode == 1) {
                if (currentPlayers.stream().noneMatch(entry -> entry.equalsIgnoreCase(p))) {
                    modified.add(p);
                    SlayerUtils.addCarry(p);
                    currentPlayers.add(p);
                }
            }

            else if (mode == 0) {
                currentPlayers.removeIf(entry -> {
                    if (entry.equalsIgnoreCase(p)) {
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
}
