package me.chaseoes.deathswap.listeners;

import me.chaseoes.deathswap.metadata.DSMetadata;
import me.chaseoes.deathswap.metadata.MetadataHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        DSMetadata meta = MetadataHelper.getDSMetadata(event.getPlayer());
        if (meta != null && meta.isIngame()) {
            meta.getCurrentGame().leaveGame(event.getPlayer());
        }
    }
}
