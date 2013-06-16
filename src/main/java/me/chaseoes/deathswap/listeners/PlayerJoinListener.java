package me.chaseoes.deathswap.listeners;

import me.chaseoes.deathswap.DSGame;
import me.chaseoes.deathswap.DeathSwap;
import me.chaseoes.deathswap.metadata.MetadataHelper;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
