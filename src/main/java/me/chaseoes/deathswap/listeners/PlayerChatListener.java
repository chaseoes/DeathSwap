package me.chaseoes.deathswap.listeners;

import me.chaseoes.deathswap.DSGame;
import me.chaseoes.deathswap.DeathSwap;
import me.chaseoes.deathswap.metadata.MetadataHelper;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent event) {
		if (MetadataHelper.getDSMetadata(event.getPlayer()).isIngame()) {
			event.getRecipients().clear();
			for (String p : MetadataHelper.getDSMetadata(event.getPlayer()).getCurrentGame().getPlayersIngame()) {
				Player player = DeathSwap.getInstance().getServer().getPlayerExact(p);
				event.getRecipients().add(player);
			}
		}

		if (!MetadataHelper.getDSMetadata(event.getPlayer()).isIngame()) {
			for (DSGame game : DeathSwap.getInstance().games.values()) {
				for (String p : game.getPlayersIngame()) {
					Player player = DeathSwap.getInstance().getServer().getPlayerExact(p);
					event.getRecipients().remove(player);
				}
			}
		}
	}

}
