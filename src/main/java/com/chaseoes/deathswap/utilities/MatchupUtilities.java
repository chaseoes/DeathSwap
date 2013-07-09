package com.chaseoes.deathswap.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.bukkit.entity.Player;

import com.chaseoes.deathswap.DSGame;
import com.chaseoes.deathswap.DeathSwap;

public class MatchupUtilities {

    public static boolean matchup(Player player) {
        Entry<String, String> randomEntry = getRandomEntry(DeathSwap.getInstance().matchups);
        if (randomEntry != null) {
            String matched = randomEntry.getValue();
            DSGame game = DeathSwap.getInstance().games.get(randomEntry.getValue());
            game.joinGame(player);
            game.joinGame(DeathSwap.getInstance().getServer().getPlayerExact(matched));
            return true;
        }
        return false;
    }

    public static boolean matchup(Player player, String map) {
        Entry<String, String> randomEntry = getRandomEntryWithValue(DeathSwap.getInstance().matchups, map);
        if (randomEntry != null) {
            String matched = randomEntry.getValue();
            DSGame game = DeathSwap.getInstance().games.get(randomEntry.getValue());
            game.joinGame(player);
            game.joinGame(DeathSwap.getInstance().getServer().getPlayerExact(matched));
            return true;
        }
        return false;
    }

    private static java.util.Map.Entry<String, String> getRandomEntryWithValue(HashMap<String, String> map, String val) {
        List<java.util.Map.Entry<String, String>> entries = new ArrayList<java.util.Map.Entry<String, String>>();
        for (java.util.Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(val)) {
                entries.add(entry);
            }
        }
        Random r = new Random();
        if (entries.size() > 0) {
            return entries.get(r.nextInt(entries.size()));
        }
        return null;
    }

    private static Entry<String, String> getRandomEntry(HashMap<String, String> map) {
        Random r = new Random();
        Set<Entry<String, String>> entries = map.entrySet();
        if (entries.size() > 0) {
            return new ArrayList<java.util.Map.Entry<String, String>>(entries).get(r.nextInt(entries.size()));
        }
        return null;
    }

}
