package com.chaseoes.deathswap.commands;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chaseoes.deathswap.DeathSwap;
import com.chaseoes.deathswap.GameQueue;
import com.chaseoes.deathswap.GameState;
import com.chaseoes.deathswap.GameType;
import com.chaseoes.deathswap.Map;
import com.chaseoes.deathswap.metadata.MetadataHelper;

public class JoinCommand {

    CommandSender cs;
    String[] strings;

    public JoinCommand(CommandSender cs, String[] strings) {
        this.cs = cs;
        this.strings = strings;
    }

    public void onCommand() {
        if (MetadataHelper.getDSMetadata((Player) cs).getCurrentQueue() != null) {
            GameQueue queue = DeathSwap.getInstance().games.get(MetadataHelper.getDSMetadata((Player) cs).getCurrentQueue()).getQueue();
            cs.sendMessage(DeathSwap.format("You are number " + queue.getPosition((Player) cs) + "in line!"));
        } else if (MetadataHelper.getDSMetadata((Player) cs).isIngame()) {
            cs.sendMessage(DeathSwap.format("You are already in a game!"));
        } else if (cs.hasPermission("deathswap.play")) {
            if (strings.length == 2) {
                String map = strings[1];
                if (DeathSwap.getInstance().games.containsKey(map)) {
                    if (!DeathSwap.getInstance().disabled.contains(map)) {
                        DeathSwap.getInstance().games.get(map).joinGame((Player) cs);
                    } else {
                        cs.sendMessage(DeathSwap.format("That map is currently disabled."));
                    }
                } else {
                    cs.sendMessage(DeathSwap.format("That map does not exist!"));
                }
            } else if (strings.length == 1) {
                ArrayList<String> validMaps = new ArrayList<String>();
                for (Map map : DeathSwap.getInstance().maps.values()) {
                    if (map.getType() == GameType.PUBLIC && DeathSwap.getInstance().games.get(map.getName()).getState() == GameState.WAITING && !DeathSwap.getInstance().disabled.contains(map.getName())) {
                        validMaps.add(map.getName());
                    }
                }
                Collections.shuffle(validMaps);
                if (validMaps.size() == 0) {
                    cs.sendMessage(DeathSwap.format("No joinable games were found!"));
                } else {
                    DeathSwap.getInstance().games.get(validMaps.get(0)).joinGame((Player) cs);
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
