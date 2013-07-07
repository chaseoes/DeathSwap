package com.chaseoes.deathswap.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.chaseoes.deathswap.DSGame;
import com.chaseoes.deathswap.DeathSwap;
import com.chaseoes.deathswap.metadata.MetadataHelper;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        MetadataHelper.createDSMetadata(event.getPlayer());
        for (DSGame game : DeathSwap.getInstance().games.values()) {
        	for (String p : game.getPlayersIngame()) {
        		Player player = DeathSwap.getInstance().getServer().getPlayerExact(p);
        		player.hidePlayer(event.getPlayer());
        	}
        }
    }
}
