package me.chaseoes.deathswap;

import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.chaseoes.deathswap.listeners.SignChangeListener;
import me.chaseoes.deathswap.utilities.SerializableLocation;

public class DeathSwap extends JavaPlugin {

    private static DeathSwap instance;
    HashMap<String, DSGame> games = new HashMap<String, DSGame>();

    public void onEnable() {
	    instance = this;
	    SerializableLocation.getUtilities().setup(this);
	    PluginManager pm = getServer().getPluginManager();
	    pm.registerEvents(new SignChangeListener(), this);
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
    
    public DSGame getGame(String mapName) {
    	return games.get(mapName);
    }
}
