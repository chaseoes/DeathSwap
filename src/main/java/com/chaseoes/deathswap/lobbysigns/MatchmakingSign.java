package com.chaseoes.deathswap.lobbysigns;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;

import com.chaseoes.deathswap.DSGame;
import com.chaseoes.deathswap.DeathSwap;
import com.chaseoes.deathswap.GameState;
import com.chaseoes.deathswap.Map;
import com.chaseoes.deathswap.MapUtilities;
import com.chaseoes.deathswap.utilities.SerializableLocation;

public class MatchmakingSign {

    Map map;

    public MatchmakingSign(Map map) {
        this.map = map;
    }

    public void create(Location location) {
        String locString = SerializableLocation.getUtilities().locationToString(location);
        List<String> signLocs = MapUtilities.getUtilities().getCustomConfig().getStringList("maps." + map.getName() + ".match-signs");
        signLocs.add(locString);
        MapUtilities.getUtilities().getCustomConfig().set("maps." + map.getName() + ".match-signs", signLocs);
        MapUtilities.getUtilities().saveData();
        update();
    }

    public void delete(Location location) {
        String locString = SerializableLocation.getUtilities().locationToString(location);
        List<String> signLocs = MapUtilities.getUtilities().getCustomConfig().getStringList("maps." + map.getName() + ".match-signs");
        signLocs.remove(locString);
        MapUtilities.getUtilities().getCustomConfig().set("maps." + map.getName() + ".match-signs", signLocs);
        MapUtilities.getUtilities().saveData();
        map = null;
    }

    public void update() {
        for (String locationStr : MapUtilities.getUtilities().getCustomConfig().getStringList("maps." + map.getName() + ".match-signs")) {
            Location location = SerializableLocation.getUtilities().stringToLocation(locationStr);
            if (location.getBlock().getType() != Material.WALL_SIGN) {
                location.getBlock().setType(Material.WALL_SIGN);
            }

            Sign s = (Sign) location.getBlock().getState();
            s.setLine(0, ChatColor.BOLD + "[Matchmaker]");
            s.setLine(1, ChatColor.ITALIC + "Click to duel!");
            s.setLine(2, "Map: " + map.getName());
            s.setLine(3, status());
            s.update(true);
        }
    }

    private String status() {
        DSGame game = DeathSwap.getInstance().games.get(map.getName());
        if (game.getState() == GameState.WAITING) {
            return ChatColor.GREEN + "" + ChatColor.BOLD + "OPEN";
        } else if (game.getState() == GameState.INGAME) {
            return ChatColor.DARK_RED + "" + ChatColor.BOLD + "INGAME";
        } else if (game.getState() == GameState.ROLLBACK) {
            return ChatColor.DARK_RED + "...";
        }
        return ChatColor.RED + "" + ChatColor.BOLD + "ERROR";
    }

}
