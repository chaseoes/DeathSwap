package com.chaseoes.deathswap.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.chaseoes.deathswap.DeathSwap;

public class CommandManager implements CommandExecutor {

    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (strings.length == 0) {
            cs.sendMessage(DeathSwap.format("Version " + DeathSwap.getInstance().getDescription().getVersion() + " by chaseoes."));
            cs.sendMessage(DeathSwap.format("Type &b/ds help &7for help."));
            return true;
        }

        String subCommand = strings[0];
        if (subCommand.equalsIgnoreCase("accept")) {
            AcceptCommand acceptCommand = new AcceptCommand(cs, strings);
            acceptCommand.onCommand();
        } else if (subCommand.equalsIgnoreCase("create")) {
            CreateCommand createCommand = new CreateCommand(cs, strings);
            createCommand.onCommand();
        } else if (subCommand.equalsIgnoreCase("disable")) {
            DisableCommand disableCommand = new DisableCommand(cs, strings);
            disableCommand.onCommand();
        } else if (subCommand.equalsIgnoreCase("duel")) {
            DuelCommand duelCommand = new DuelCommand(cs, strings);
            duelCommand.onCommand();
        } else if (subCommand.equalsIgnoreCase("enable")) {
            EnableCommand enableCommand = new EnableCommand(cs, strings);
            enableCommand.onCommand();
        } else if (subCommand.equalsIgnoreCase("help")) {
            HelpCommand helpCommand = new HelpCommand(cs, strings);
            helpCommand.onCommand();
        } else if (subCommand.equalsIgnoreCase("join")) {
            JoinCommand joinCommand = new JoinCommand(cs, strings);
            joinCommand.onCommand();
        } else if (subCommand.equalsIgnoreCase("leave")) {
            LeaveCommand leaveCommand = new LeaveCommand(cs, strings);
            leaveCommand.onCommand();
        } else if (subCommand.equalsIgnoreCase("list")) {
            ListCommand listCommand = new ListCommand(cs, strings);
            listCommand.onCommand();
        } else if (subCommand.equalsIgnoreCase("setmax")) {
            SetMaxCommand setMaxCommand = new SetMaxCommand(cs, strings);
            setMaxCommand.onCommand();
        } else {
            cs.sendMessage(DeathSwap.format("Unknown command. Type &b/ds help &7for help."));
        }
        return true;
    }

}
