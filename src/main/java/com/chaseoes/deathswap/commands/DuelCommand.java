package com.chaseoes.deathswap.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chaseoes.deathswap.DeathSwap;
import com.chaseoes.deathswap.GameState;
import com.chaseoes.deathswap.GameType;
import com.chaseoes.deathswap.metadata.MetadataHelper;
import com.chaseoes.deathswap.utilities.DuelInfo;
import com.chaseoes.deathswap.utilities.MatchupUtilities;

public class DuelCommand {

    CommandSender cs;
    String[] strings;

    public DuelCommand(CommandSender cs, String[] strings) {
        this.cs = cs;
        this.strings = strings;
    }

    public void onCommand() {
        if (MetadataHelper.getDSMetadata((Player) cs).isIngame()) {
            cs.sendMessage(DeathSwap.format("You are already in a game!"));
        } else if (cs.hasPermission("deathswap.play")) {
            if (strings.length == 3) {
                if (DeathSwap.getInstance().duelReqTimes.containsKey(cs.getName())) {
                    Long time = DeathSwap.getInstance().duelReqTimes.get(cs.getName());
                    if (System.currentTimeMillis() - time < DeathSwap.getInstance().getConfig().getInt("duel-cooldown", 10) * DeathSwap.getInstance().MS_IN_A_SEC) {
                        cs.sendMessage(DeathSwap.format("Please wait " + (DeathSwap.getInstance().getConfig().getInt("duel-cooldown", 10) * DeathSwap.getInstance().MS_IN_A_SEC - (System.currentTimeMillis() - time)) + " seconds before your next request!"));
                        return;
                    }
                    DeathSwap.getInstance().duelReqTimes.remove(cs.getName());
                }
                String map = strings[1];
                String p = strings[2];
                Player player = DeathSwap.getInstance().getServer().getPlayer(p);
                if (player == null) {
                    cs.sendMessage(DeathSwap.format("That player isn't online!"));
                } else if (!DeathSwap.getInstance().maps.containsKey(map)) {
                    cs.sendMessage(DeathSwap.format("That map does not exist!"));
                } else if (DeathSwap.getInstance().maps.get(map).getType() != GameType.PRIVATE) {
                    cs.sendMessage(DeathSwap.format("That map is not for dueling!"));
                } else if (DeathSwap.getInstance().games.get(map).getState() != GameState.WAITING) {
                    cs.sendMessage(DeathSwap.format("That map is currently ingame!"));
                } else if (MetadataHelper.getDSMetadata(player).isIngame()) {
                    cs.sendMessage(DeathSwap.format(p + " is already in a game!"));
                } else if (DeathSwap.getInstance().noRequests.contains(player.getName())) {
                    cs.sendMessage(DeathSwap.format("This player currently isn't accepting requests to duel."));
                } else {
                    DeathSwap.getInstance().duelReqTimes.put(cs.getName(), System.currentTimeMillis());
                    cs.sendMessage(DeathSwap.format("You have requested to duel " + player.getName() + "."));
                    player.sendMessage(DeathSwap.format(cs.getName() + " has requested to duel you in a DeathSwap game!"));
                    player.sendMessage(DeathSwap.format("Type &b/ds accept &7to accept their request."));
                    DeathSwap.getInstance().needsToAccept.put(player.getName(), new DuelInfo(cs.getName(), map));
                }
            } else if (strings.length == 2) {
                if (strings[1].equalsIgnoreCase("toggle")) {
                    if (!DeathSwap.getInstance().noRequests.contains(cs.getName())) {
                        DeathSwap.getInstance().noRequests.add(cs.getName());
                        cs.sendMessage(DeathSwap.format("Players are no longer allowed to send you requests to duel."));
                    } else {
                        DeathSwap.getInstance().noRequests.remove(cs.getName());
                        cs.sendMessage(DeathSwap.format("Players are now allowed to send you requests to duel."));
                    }
                } else {
                    String map = strings[1];
                    boolean b = MatchupUtilities.matchup((Player) cs, map);
                    if (!b) {
                        cs.sendMessage(DeathSwap.format("We couldn't find a player to match you with!"));
                        cs.sendMessage(DeathSwap.format("You will join a game as soon as we find a player."));
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
