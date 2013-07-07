package com.chaseoes.deathswap.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.chaseoes.deathswap.metadata.MetadataHelper;

public class PlayerCommandPreproccessListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (MetadataHelper.getDSMetadata(event.getPlayer()).isIngame() && !event.getPlayer().hasPermission("deathswap.create") && !event.getMessage().startsWith("/ds")) {
            event.setCancelled(true);
        }
    }
}
