package com.chaseoes.deathswap.commands;

import org.bukkit.command.CommandSender;

import com.chaseoes.deathswap.DeathSwap;
import com.chaseoes.deathswap.Map;

public class SetMaxCommand {

    CommandSender cs;
    String[] strings;

    public SetMaxCommand(CommandSender cs, String[] strings) {
        this.cs = cs;
        this.strings = strings;
    }

    public void onCommand() {
        if (cs.hasPermission("deathswap.create")) {
            if (strings.length == 3) {
                String mapName = strings[1];
                try {
                    int max = Integer.parseInt(strings[2]);
                    if (DeathSwap.getInstance().maps.containsKey(mapName)) {
                        Map m = DeathSwap.getInstance().maps.get(mapName);
                        m.setMaxPlayers(max);
                        cs.sendMessage(DeathSwap.format("Successfully set the max players to " + max + "!"));
                    } else {
                        cs.sendMessage(DeathSwap.format("That map does not exist!"));
                    }
                } catch (Exception e) {
                    cs.sendMessage(DeathSwap.format("That isn't a number!"));
                }
            } else {
                cs.sendMessage(DeathSwap.format("Incorrect command syntax."));
                cs.sendMessage(DeathSwap.format("Type &b/ds help &7for help."));
            }
        } else {
            cs.sendMessage(DeathSwap.format("You don't have permission."));
        }
    }

}
