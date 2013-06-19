package me.chaseoes.deathswap.listeners;

import me.chaseoes.deathswap.DeathSwap;
import me.chaseoes.deathswap.metadata.MetadataHelper;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.hasBlock()) {
			if (event.getClickedBlock().getType() == Material.WALL_SIGN || event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.SIGN_POST) {
				Sign s = (Sign) event.getClickedBlock().getState();
				if (s.getLine(1).equalsIgnoreCase("Click to play:")) {
					String map = s.getLine(2).replace("Map: ", "");
					if (DeathSwap.getInstance().games.containsKey(map)) {
						if (MetadataHelper.getDSMetadata(event.getPlayer()).isIngame()) {
							event.getPlayer().performCommand("ds leave");
						} else {
							event.getPlayer().performCommand("ds join " + map);
						}
						event.setCancelled(true);
					}
				}
			} 
		}
	}
	
	@EventHandler
    public void touchytouchy(PlayerInteractEntityEvent event){
        Entity rightclick = event.getRightClicked();
        if(rightclick instanceof Player){
        	Player clicked = (Player) event.getRightClicked();
            DeathSwap.getInstance().refreshMenu(event.getPlayer(), clicked);
            DeathSwap.getInstance().duelMenus.get(event.getPlayer().getName()).open(event.getPlayer());
        }
    }
    

}
