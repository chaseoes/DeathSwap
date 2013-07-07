package com.chaseoes.deathswap.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chaseoes.deathswap.DSGame;
import com.chaseoes.deathswap.DeathSwap;
import com.chaseoes.deathswap.GameType;
import com.chaseoes.deathswap.Map;
import com.chaseoes.deathswap.MapUtilities;
import com.sk89q.worldedit.EmptyClipboardException;

public class CreateCommand {

    CommandSender cs;
    String[] strings;

    public CreateCommand(CommandSender cs, String[] strings) {
        this.cs = cs;
        this.strings = strings;
    }

    public void onCommand() {
        if (cs.hasPermission("deathswap.create")) {
            if (strings.length == 4) {
                if (strings[1].equalsIgnoreCase("map")) {
                    String mapName = strings[2];
                    String mapType = strings[3];
                    try {
                        Map m = new Map(mapName);
                        MapUtilities.getUtilities().createMap(m, (Player) cs, GameType.get(mapType), 20);
                        DeathSwap.getInstance().maps.put(mapName, m);
                        DeathSwap.getInstance().games.put(m.getName(), new DSGame(m.getName(), m.getP1(), m.getP2()));
                        cs.sendMessage(DeathSwap.format("Successfully created " + mapName + "!"));
                    } catch (EmptyClipboardException e) {
                        cs.sendMessage(DeathSwap.format("You must select the map with WorldEdit first."));
                    }
                }

                if (strings[1].equalsIgnoreCase("icon")) {
                    String mapName = strings[2];
                    String mapIcon = strings[3];
                    if (DeathSwap.getInstance().games.containsKey(mapName)) {
                        DeathSwap.getInstance().getConfig().set("maps." + mapName + ".icon", mapIcon.toUpperCase());
                        DeathSwap.getInstance().saveConfig();
                    } else {
                        cs.sendMessage(DeathSwap.format("That map does not exist."));
                    }
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
