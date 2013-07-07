package com.chaseoes.deathswap.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chaseoes.deathswap.DSGame;
import com.chaseoes.deathswap.DeathSwap;
import com.chaseoes.deathswap.metadata.MetadataHelper;

public class ListCommand {

    CommandSender cs;
    String[] strings;

    public ListCommand(CommandSender cs, String[] strings) {
        this.cs = cs;
        this.strings = strings;
    }

    public void onCommand() {
        if (cs.hasPermission("deathswap.play")) {
            if (strings.length >= 1) {
                DSGame game = null;
                if (strings.length == 2) {
                    String map = strings[1];
                    if (DeathSwap.getInstance().games.containsKey(map)) {
                        game = DeathSwap.getInstance().games.get(map);
                    } else {
                        cs.sendMessage(DeathSwap.format("That map does not exist!"));
                        return;
                    }
                }

                if (strings.length == 1) {
                    if (MetadataHelper.getDSMetadata((Player) cs).isIngame()) {
                        game = MetadataHelper.getDSMetadata((Player) cs).getCurrentGame();
                    } else {
                        cs.sendMessage(DeathSwap.format("You're not in a game!"));
                        return;
                    }
                }

                StringBuilder sb = new StringBuilder();
                for (String player : game.getPlayersIngame()) {
                    sb.append(player + ", ");
                }

                cs.sendMessage(DeathSwap.format(sb.toString().substring(0, sb.toString().length() - 2)));
            } else {
                cs.sendMessage(DeathSwap.format("Incorrect command syntax."));
                cs.sendMessage(DeathSwap.format("Type &b/ds help &7for help."));
            }
        } else {
            cs.sendMessage(DeathSwap.format("You don't have permission."));
        }
    }

}
