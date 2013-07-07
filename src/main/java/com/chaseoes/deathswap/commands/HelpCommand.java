package com.chaseoes.deathswap.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.chaseoes.deathswap.DeathSwap;

public class HelpCommand {

    CommandSender cs;
    String[] strings;

    public HelpCommand(CommandSender cs, String[] strings) {
        this.cs = cs;
        this.strings = strings;
    }

    public void onCommand() {
        cs.sendMessage(DeathSwap.format("DeathSwap Commands:"));
        cs.sendMessage(ChatColor.GREEN + "/ds" + ChatColor.GRAY + ": General plugin information.");
        cs.sendMessage(ChatColor.GREEN + "/ds join <map name>" + ChatColor.GRAY + ": Joins the specified map.");
        cs.sendMessage(ChatColor.GREEN + "/ds leave" + ChatColor.GRAY + ": Leave the game you're in.");
        cs.sendMessage(ChatColor.GREEN + "/ds list [map]" + ChatColor.GRAY + ": List players in the given map.");
        cs.sendMessage(ChatColor.GREEN + "/ds create map <map name> <game type>" + ChatColor.GRAY + ": Create a DeathSwap map.");
        cs.sendMessage(ChatColor.GREEN + "/ds setmax <map> <#>" + ChatColor.GRAY + ": Set the max players for a map.");
    }

}
