package me.chaseoes.deathswap;

import me.chaseoes.deathswap.metadata.DSMetadata;
import me.chaseoes.deathswap.metadata.MetadataHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DSGame {

    private String name;
    private int size;
    private ArrayList<String> players = new ArrayList<String>();
    private Random rand = new Random();
    private GameState state;
    private Location lowerBound;
    private Location upperBound;
    private World world;

    public DSGame(String name, int size, Location loc1, Location loc2) {
        this.name = name;
        this.size = size;
        lowerBound = new Location(loc1.getWorld(), Math.min(loc1.getBlockX(), loc2.getBlockX()), 0, Math.min(loc1.getBlockZ(), loc2.getBlockZ()));
        upperBound = new Location(loc1.getWorld(), Math.max(loc1.getBlockX(), loc2.getBlockX()), loc1.getWorld().getMaxHeight(), Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
        world = loc1.getWorld();
    }
    
    public ArrayList<String> getPlayersIngame() {
    	return players;
    }

    public void swap() {
        Collections.shuffle(players, rand);
        ArrayList<Location> locs = new ArrayList<Location>();
        ArrayList<Player> pls = new ArrayList<Player>();

        for (String pl : players) {
            Player p = Bukkit.getPlayer(pl);
            locs.add(p.getLocation());
            pls.add(p);
        }

        for (int i = 0; i < players.size(); i++) {
            pls.get(i).teleport(locs.get((i + 1) % players.size()));
            MetadataHelper.getDSMetadata(pls.get(i)).setLastSwappedTo(pls.get((i + 1) % players.size()));
        }
    }

    public Location getRandomLoc() {
        int dx = upperBound.getBlockX() - lowerBound.getBlockX();
        int dz = upperBound.getBlockZ() - lowerBound.getBlockZ();
        int rx = rand.nextInt(dx);
        int rz = rand.nextInt(dz);
        int x = lowerBound.getBlockX() + rx;
        int z = lowerBound.getBlockZ() + rz;
        int y = world.getHighestBlockYAt(x, z);
        return new Location(world, x, y, z);
    }

    public void joinGame(Player player) {
        if (state == GameState.INGAME) {
            player.sendMessage("Game " + name + " is currently running");
        } else if (players.size() < size) {

            DSMetadata meta = MetadataHelper.getDSMetadata(player);
            meta.setCurrentGame(this);
        } else {
            player.sendMessage("Game " + name + " is full");
        }
    }

    public void leaveGame(Player player) {

    }
}
