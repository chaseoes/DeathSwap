package me.chaseoes.deathswap.listeners;

import me.chaseoes.deathswap.metadata.MetadataHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        MetadataHelper.createDSMetadata(event.getPlayer());
    }
}
