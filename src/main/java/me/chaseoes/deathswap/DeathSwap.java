package me.chaseoes.deathswap;

import java.util.HashMap;

import me.chaseoes.deathswap.listeners.PlayerCommandPreproccessListener;
import me.chaseoes.deathswap.listeners.PlayerJoinListener;
import me.chaseoes.deathswap.listeners.PlayerQuitListener;
import me.chaseoes.deathswap.metadata.MetadataHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.EmptyClipboardException;

import me.chaseoes.deathswap.listeners.SignChangeListener;
import me.chaseoes.deathswap.utilities.SerializableLocation;
import me.chaseoes.deathswap.utilities.WorldEditUtilities;

public class DeathSwap extends JavaPlugin {

	private static DeathSwap instance;
	HashMap<String, Map> maps = new HashMap<String, Map>();
	public HashMap<String, DSGame> games = new HashMap<String, DSGame>();

	public static DeathSwap getInstance() {
		return instance;
	}

	public void onEnable() {
		instance = this;
		SerializableLocation.getUtilities().setup(this);
		WorldEditUtilities.getWEUtilities().setup(this);
		MapUtilities.getUtilities().setup(this);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new SignChangeListener(), this);
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new PlayerQuitListener(), this);
        pm.registerEvents(new PlayerCommandPreproccessListener(), this);

		if (getConfig().getConfigurationSection("maps") != null) {
			for (String map : getConfig().getConfigurationSection("maps").getKeys(false)) {
				maps.put(map, new Map(map));
				games.put(map, new DSGame(map, 200, maps.get(map).getP1(), maps.get(map).getP2()));
			}
		}

        for (Player player : getServer().getOnlinePlayers()) {
            MetadataHelper.createDSMetadata(player);
        }

		getServer().getScheduler().runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				getServer().broadcastMessage(format("Server reloaded.\nChanges to the DeathSwap plugin have been made!"));
			}
		}, 40L);
	}

	public void onDisable() {
		maps.clear();
		instance = null;
	}

	public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
		if (cmnd.getName().equalsIgnoreCase("deathswap")) {
			if (strings.length == 0) {
				cs.sendMessage(format("Version " + getDescription().getVersion() + " by chaseoes."));
				return true;
			}

			if (strings[0].equalsIgnoreCase("join")) {
				if (strings.length == 2) {
					String map = strings[1];
					if (games.containsKey(map)) {
						games.get(map).joinGame((Player)cs);
						cs.sendMessage(format("Successfully joined the map " + strings[1] + "!"));
					} else {
						cs.sendMessage(format("That map does not exist!"));
					}
				} else {
					cs.sendMessage(format("Incorrect command syntax."));
				}
			}

			if (strings[0].equalsIgnoreCase("create")) {
				if (strings.length == 4) {
					if (strings[1].equalsIgnoreCase("map")) {
						String mapName = strings[2];
						String mapType = strings[3];
						try {
							Map m = new Map(mapName);
							MapUtilities.getUtilities().createMap(m, (Player) cs, GameType.get(mapType), 20);
							maps.put(mapName, m);
							games.put(m.getName(), new DSGame(m.getName(), 2000, m.getP1(), m.getP2()));
							cs.sendMessage(format("Successfully created " + mapName + "!"));
						} catch (EmptyClipboardException e) {
							cs.sendMessage(format("You must select the map with WorldEdit first."));
						}
					}
				} else {
					cs.sendMessage(format("Incorrect command syntax."));
				}
			}
		}
		return true;
	}

	public Map getMap(String name) {
		return maps.get(name);
	}

	public String format(String s) {
		return ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Death" + ChatColor.GREEN + "Swap" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " " + ChatColor.translateAlternateColorCodes('&', s);
	}

	public void setLobbyLocation(Location l) {
		getConfig().set("lobby-location", SerializableLocation.getUtilities().locationToString(l));
		saveConfig();
	}

	public Location getLobbyLocation() {
		String loc = getConfig().getString("lobby-location");
		if (loc != null) {
			return SerializableLocation.getUtilities().stringToLocation(loc);
		}
		return Bukkit.getWorlds().get(0).getSpawnLocation();
	}
}
