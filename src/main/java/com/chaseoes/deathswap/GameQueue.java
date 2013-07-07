package com.chaseoes.deathswap;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.chaseoes.deathswap.metadata.MetadataHelper;

public class GameQueue {

    DSGame game;
    ArrayList<String> queue = new ArrayList<String>();

    public GameQueue(DSGame g) {
        game = g;
    }

    public int getPosition(Player player) {
        if (contains(player)) {
            return queue.indexOf(player.getName());
        }
        return -1;
    }

    public boolean contains(Player player) {
        return queue.contains(player.getName());
    }

    public boolean gameHasRoom() {
        int i = DeathSwap.getInstance().getMap(game.getName()).getMaxPlayers() - (game.getPlayersIngame().size() + 1);
        return i >= 0;
    }

    public boolean add(Player player) {
        if (contains(player)) {
            return false;
        }

        for (DSGame g : DeathSwap.getInstance().getGames()) {
            g.getQueue().remove(player);
        }

        queue.add(player.getName());
        MetadataHelper.getDSMetadata(player).setCurrentQueue(game.getName());
        return true;
    }

    public boolean remove(Player player) {
        queue.remove(player.getName());
        MetadataHelper.getDSMetadata(player).setCurrentQueue(null);
        return true;
    }

    public void check() {
        if (game.getState() != GameState.INGAME && DeathSwap.getInstance().isEnabled()) {
            DeathSwap.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(DeathSwap.getInstance(), new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> playersInQueue = new ArrayList<String>(queue);
                    for (String p : playersInQueue) {
                        Player player = DeathSwap.getInstance().getServer().getPlayer(p);
                        if (player != null) {
                            if (gameHasRoom()) {
                                game.joinGame(player);
                            }
                        } else {
                            queue.remove(p);
                        }
                    }
                }
            }, 5L);
        }
    }
}
