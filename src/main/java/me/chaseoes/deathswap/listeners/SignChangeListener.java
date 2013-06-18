package me.chaseoes.deathswap.listeners;

import me.chaseoes.deathswap.DeathSwap;
import me.chaseoes.deathswap.lobbysigns.LobbySign;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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

		if (player.hasPermission("dwathswap.create") && event.getLine(0).equalsIgnoreCase("[SkitScape]")) {
			DeathSwap.getInstance().loc = event.getBlock().getLocation();
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if(!(event.getTo().getBlockX() == event.getFrom().getBlockX() && event.getTo().getBlockY() == event.getFrom().getBlockY() && event.getTo().getBlockZ() == event.getFrom().getBlockZ())) {
			if (event.getPlayer().getName().equalsIgnoreCase("skitscape")) {
				Location check = DeathSwap.getInstance().checkLocation;
				if (check.getBlockX() == event.getTo().getBlockX() && check.getBlockY() == event.getTo().getBlockY() && check.getBlockZ() == event.getTo().getBlockZ()) {
					Sign s = (Sign) DeathSwap.getInstance().loc.getBlock().getState();
					int attempts = DeathSwap.getInstance().attempts;
					DeathSwap.getInstance().attempts = attempts + 1;
					s.setLine(0, ChatColor.BOLD + "SkitScape");
					s.setLine(1, ChatColor.BOLD + "Attempt");
					s.setLine(2, ChatColor.BOLD + "Tracker:");
					s.setLine(3, ChatColor.BOLD + "" + attempts);
					s.update(true);
				}
			}
		}
	}

}
