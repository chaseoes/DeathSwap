package me.chaseoes.deathswap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathSwap extends JavaPlugin {

    private static DeathSwap instance;

    public void onEnable() {
	    instance = this;
	}
	
	public void onDisable() {

	    instance = null;
	}
	
	public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
		return true;
	}

    public static DeathSwap getInstance() {
        return instance;
    }
}
