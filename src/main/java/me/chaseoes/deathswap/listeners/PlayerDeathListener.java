package me.chaseoes.deathswap.listeners;

import me.chaseoes.deathswap.DSGame;
import me.chaseoes.deathswap.DeathSwap;
import me.chaseoes.deathswap.metadata.MetadataHelper;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (MetadataHelper.getDSMetadata(event.getEntity()).isIngame()) {
			event.setDeathMessage(DeathSwap.getInstance().format(event.getDeathMessage() + "!"));
			DSGame g = MetadataHelper.getDSMetadata(event.getEntity()).getCurrentGame();
			g.leaveGame(event.getEntity());
		}
	}

}
