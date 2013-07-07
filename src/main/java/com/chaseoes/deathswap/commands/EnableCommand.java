package com.chaseoes.deathswap.commands;

import org.bukkit.command.CommandSender;

import com.chaseoes.deathswap.DeathSwap;

public class EnableCommand {

    CommandSender cs;
    String[] strings;

    public EnableCommand(CommandSender cs, String[] strings) {
        this.cs = cs;
        this.strings = strings;
    }

    public void onCommand() {
        if (cs.hasPermission("deathswap.create")) {
            if (strings.length == 2) {
                String map = strings[1];
                if (DeathSwap.getInstance().games.containsKey(map)) {
                    DeathSwap.getInstance().disabled.remove(map);
                    cs.sendMessage(DeathSwap.format("Successfully enabled " + map + "."));
                } else {
                    cs.sendMessage(DeathSwap.format("That map does not exist!"));
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
