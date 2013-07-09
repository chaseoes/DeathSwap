package com.chaseoes.deathswap;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.chaseoes.deathswap.commands.CommandManager;
import com.chaseoes.deathswap.listeners.BlockListeners;
import com.chaseoes.deathswap.listeners.EntityDamageListener;
import com.chaseoes.deathswap.listeners.InventoryCloseListener;
import com.chaseoes.deathswap.listeners.InventoryOpenListener;
import com.chaseoes.deathswap.listeners.PlayerChatListener;
import com.chaseoes.deathswap.listeners.PlayerCommandPreproccessListener;
import com.chaseoes.deathswap.listeners.PlayerDeathListener;
import com.chaseoes.deathswap.listeners.PlayerInteractListener;
import com.chaseoes.deathswap.listeners.PlayerJoinListener;
import com.chaseoes.deathswap.listeners.PlayerQuitListener;
import com.chaseoes.deathswap.listeners.SignChangeListener;
import com.chaseoes.deathswap.metadata.MetadataHelper;
import com.chaseoes.deathswap.utilities.DuelInfo;
import com.chaseoes.deathswap.utilities.MetricsLite;
import com.chaseoes.deathswap.utilities.SerializableLocation;
import com.chaseoes.deathswap.utilities.WorldEditUtilities;

public class DeathSwap extends JavaPlugin {

    private static DeathSwap instance;
    public HashMap<String, Map> maps = new HashMap<String, Map>();
    public HashMap<String, DSGame> games = new HashMap<String, DSGame>();
    public HashMap<String, DuelInfo> needsToAccept = new HashMap<String, DuelInfo>();
    public HashSet<String> disabled = new HashSet<String>();
    public HashSet<String> noRequests = new HashSet<String>();
    public HashMap<String, Long> duelReqTimes = new HashMap<String, Long>();
    public HashMap<String, String> matchups = new HashMap<String, String>();
    public final int MS_IN_A_SEC = 1000;

    public static DeathSwap getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        SerializableLocation.getUtilities().setup(this);
        WorldEditUtilities.getWEUtilities().setup(this);
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new SignChangeListener(), this);
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new PlayerQuitListener(), this);
        pm.registerEvents(new PlayerDeathListener(), this);
        pm.registerEvents(new PlayerCommandPreproccessListener(), this);
        pm.registerEvents(new BlockListeners(), this);
        pm.registerEvents(new PlayerInteractListener(), this);
        pm.registerEvents(new EntityDamageListener(), this);
        pm.registerEvents(new PlayerChatListener(), this);
        pm.registerEvents(new InventoryCloseListener(), this);
        pm.registerEvents(new InventoryOpenListener(), this);
        getCommand("deathswap").setExecutor(new CommandManager());

        if (MapUtilities.getUtilities().getCustomConfig().getConfigurationSection("maps") != null) {
            for (String map : MapUtilities.getUtilities().getCustomConfig().getConfigurationSection("maps").getKeys(false)) {
                maps.put(map, new Map(map));
                games.put(map, new DSGame(map, maps.get(map).getP1(), maps.get(map).getP2()));
            }
        }

        for (Player player : getServer().getOnlinePlayers()) {
            MetadataHelper.createDSMetadata(player);
        }

        getServer().getScheduler().runTaskLater(this, new Runnable() {
            public void run() {
                getServer().broadcastMessage(format("-------------------"));
                getServer().broadcastMessage(format("Server reloaded."));
                getServer().broadcastMessage(format("Changes to the DeathSwap plugin have been made!"));
                getServer().broadcastMessage(format("-------------------"));
            }
        }, 40L);

        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "psycowithespn broke something.");
        }
    }

    public void onDisable() {
        for (DSGame game : games.values()) {
            game.stopGame();
        }
        maps.clear();
        for (Player player : getServer().getOnlinePlayers()) {
            MetadataHelper.deleteDSMetadata(player);
        }
        instance = null;
    }

    public Map getMap(String name) {
        return maps.get(name);
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

    public DSGame getGame(String name) {
        if (games.containsKey(name)) {
            return games.get(name);
        }
        return null;
    }

    public Collection<DSGame> getGames() {
        return games.values();
    }

    int roundUp(int n) {
        return (n + 8) / 9 * 9;
    }

    public static String format(String s) {
        return ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Death" + ChatColor.GREEN + "Swap" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " " + ChatColor.translateAlternateColorCodes('&', s);
    }

}
