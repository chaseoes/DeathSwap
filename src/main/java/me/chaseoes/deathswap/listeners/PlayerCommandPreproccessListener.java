package me.chaseoes.deathswap.listeners;

import me.chaseoes.deathswap.metadata.MetadataHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreproccessListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (MetadataHelper.getDSMetadata(event.getPlayer()).isIngame()) {
            event.setCancelled(true);
        }
    }
}
