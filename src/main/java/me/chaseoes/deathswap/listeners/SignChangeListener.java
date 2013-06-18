package me.chaseoes.deathswap.listeners;

import me.chaseoes.deathswap.DeathSwap;
import me.chaseoes.deathswap.lobbysigns.LobbySign;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("dwathswap.create") && event.getLine(0).equalsIgnoreCase("[DeathSwap]")) {
			String mapName = event.getLine(1);
			LobbySign ls = new LobbySign(DeathSwap.getInstance().getMap(mapName));
			ls.create(event.getBlock().getLocation());
			player.sendMessage(DeathSwap.getInstance().format("Successfully created a DeathSwap lobby sign!"));
		}
	}

}
