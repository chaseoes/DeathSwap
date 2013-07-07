package com.chaseoes.deathswap.listeners;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.chaseoes.deathswap.DeathSwap;
import com.chaseoes.deathswap.DuelMenu;
import com.chaseoes.deathswap.metadata.MetadataHelper;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.hasBlock()) {
            if (event.getClickedBlock().getType() == Material.WALL_SIGN || event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.SIGN_POST) {
                Sign s = (Sign) event.getClickedBlock().getState();
                if (s.getLine(1).equalsIgnoreCase("Click to play:")) {
                    String map = s.getLine(2).replace("Map: ", "");
                    if (map.equalsIgnoreCase("Random")) {
                        if (MetadataHelper.getDSMetadata(event.getPlayer()).isIngame()) {
                            event.getPlayer().performCommand("ds leave");
                        } else {
                            event.getPlayer().performCommand("ds join");
                        }
                        event.setCancelled(true);
                    } else if (DeathSwap.getInstance().games.containsKey(map)) {
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
    public void touchyTouchy(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player && !MetadataHelper.getDSMetadata(event.getPlayer()).isDuelMenuOpen()) {
            DuelMenu menu = new DuelMenu(event.getPlayer(), (Player) event.getRightClicked());
            menu.open();
            MetadataHelper.getDSMetadata(event.getPlayer()).setDuelMenuOpen(true);
        }
    }

}
