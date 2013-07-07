package com.chaseoes.deathswap.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chaseoes.deathswap.DSGame;
import com.chaseoes.deathswap.DeathSwap;
import com.chaseoes.deathswap.metadata.MetadataHelper;

public class LeaveCommand {

    CommandSender cs;
    String[] strings;

    public LeaveCommand(CommandSender cs, String[] strings) {
        this.cs = cs;
        this.strings = strings;
    }

    public void onCommand() {
        if (cs.hasPermission("deathswap.play")) {
            if (strings.length == 1) {
                if (MetadataHelper.getDSMetadata((Player) cs).isIngame()) {
                    DSGame game = MetadataHelper.getDSMetadata((Player) cs).getCurrentGame();
                    game.leaveGame((Player) cs);
                    cs.sendMessage(DeathSwap.format("You have left the game."));
                } else {
                    cs.sendMessage(DeathSwap.format("You aren't in a game."));
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
