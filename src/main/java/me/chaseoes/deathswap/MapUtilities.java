package me.chaseoes.deathswap;

import me.chaseoes.deathswap.utilities.WorldEditUtilities;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class MapUtilities {
	
    private DeathSwap plugin;
    static MapUtilities instance = new MapUtilities();

    private MapUtilities() {

    }

    public static MapUtilities getUtilities() {
        return instance;
    }

    public void setup(DeathSwap p) {
        plugin = p;
    }
    
    public void createMap(Map map, Player p, GameType type, int maxPlayers) throws EmptyClipboardException {
    	System.out.println(map.getName() + " " + p + " " + type + " " + maxPlayers);
    	System.out.println(WorldEditUtilities.getWorldEdit());
        Selection sel = WorldEditUtilities.getWorldEdit().getSelection(p);
        if (sel != null) {
            Location b1 = new Location(p.getWorld(), sel.getNativeMinimumPoint().getBlockX(), sel.getNativeMinimumPoint().getBlockY(), sel.getNativeMinimumPoint().getBlockZ());
            Location b2 = new Location(p.getWorld(), sel.getNativeMaximumPoint().getBlockX(), sel.getNativeMaximumPoint().getBlockY(), sel.getNativeMaximumPoint().getBlockZ());
            map.setP1(b1);
            map.setP2(b2);
            map.setMaxPlayers(maxPlayers);
            map.setType(type);
        } else {
            throw new EmptyClipboardException();
        }
    }

}
