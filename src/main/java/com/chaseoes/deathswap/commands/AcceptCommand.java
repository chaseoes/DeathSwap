package com.chaseoes.deathswap.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chaseoes.deathswap.DSGame;
import com.chaseoes.deathswap.DeathSwap;
import com.chaseoes.deathswap.GameState;
import com.chaseoes.deathswap.metadata.MetadataHelper;
import com.chaseoes.deathswap.utilities.DuelInfo;

public class AcceptCommand {

    CommandSender cs;
    String[] strings;

    public AcceptCommand(CommandSender cs, String[] strings) {
        this.cs = cs;
        this.strings = strings;
    }

    public void onCommand() {
        if (MetadataHelper.getDSMetadata((Player) cs).isIngame()) {
            cs.sendMessage(DeathSwap.format("You are already in a game!"));
        } else if (cs.hasPermission("deathswap.play")) {
            if (strings.length == 1) {
                if (!DeathSwap.getInstance().needsToAccept.containsKey(cs.getName())) {
                    cs.sendMessage(DeathSwap.format("Nobody has requested to duel you!"));
                } else {
                    DuelInfo info = DeathSwap.getInstance().needsToAccept.get(cs.getName());
                    DeathSwap.getInstance().needsToAccept.remove(cs.getName());
                    Player chall = Bukkit.getPlayerExact(info.getChallenger());
                    DSGame game = DeathSwap.getInstance().games.get(info.getMap());
                    if (chall == null) {
                        cs.sendMessage(DeathSwap.format("Your challenger is no longer online!"));
                    } else if (game.getState() != GameState.WAITING) {
                        cs.sendMessage(DeathSwap.format("The requested map is already ingame!"));
                    } else if (MetadataHelper.getDSMetadata(chall).isIngame()) {
                        cs.sendMessage(DeathSwap.format("Your challenger is already in a game!"));
                    } else {
                        cs.sendMessage(DeathSwap.format("Accepted request"));
                        game.joinGame(chall);
                        game.joinGame((Player) cs);
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
