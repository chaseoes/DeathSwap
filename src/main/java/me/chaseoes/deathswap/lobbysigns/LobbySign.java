package me.chaseoes.deathswap.lobbysigns;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;

import me.chaseoes.deathswap.DeathSwap;
import me.chaseoes.deathswap.Map;
import me.chaseoes.deathswap.utilities.SerializableLocation;

public class LobbySign {

	Map map;

	public LobbySign(Map map) {
		this.map = map;
	}

	public void create(Location location) {
		String locString = SerializableLocation.getUtilities().locationToString(location);
		List<String> signLocs = DeathSwap.getInstance().getConfig().getStringList("maps." + map.getName() + ".signs");
		signLocs.add(locString);
		DeathSwap.getInstance().getConfig().set("maps." + map.getName() + ".signs", signLocs);
		DeathSwap.getInstance().saveConfig();
		update();
	}

	public void delete(Location location) {
		String locString = SerializableLocation.getUtilities().locationToString(location);
		List<String> signLocs = DeathSwap.getInstance().getConfig().getStringList("maps." + map.getName() + ".signs");
		signLocs.remove(locString);
		DeathSwap.getInstance().getConfig().set("maps." + map.getName() + ".signs", signLocs);
		DeathSwap.getInstance().saveConfig();
		map = null;
	}

	public void update() {
		for (String locationStr : DeathSwap.getInstance().getConfig().getStringList("maps." + map.getName() + ".signs")) {
			Location location = SerializableLocation.getUtilities().stringToLocation(locationStr);
			if (location.getBlock().getType() != Material.WALL_SIGN) {
				location.getBlock().setType(Material.WALL_SIGN);
			}
			
			Sign s = (Sign) location.getBlock().getState();
			s.setLine(0, ChatColor.BOLD + "[DeathSwap]");
			s.setLine(1, "Click to play:");
			s.setLine(2, "Map: " + map.getName());
			s.setLine(3, "Players: " + DeathSwap.getInstance().games.get(map.getName()).getPlayersIngame().size() + "/" + map.getMaxPlayers());
			s.update(true);
		}
	}

}
