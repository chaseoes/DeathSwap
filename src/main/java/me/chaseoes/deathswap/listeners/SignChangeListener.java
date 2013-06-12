package me.chaseoes.deathswap.listeners;

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
		}
	}

}
