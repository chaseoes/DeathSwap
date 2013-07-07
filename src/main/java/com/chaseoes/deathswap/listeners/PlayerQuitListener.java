package com.chaseoes.deathswap.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.chaseoes.deathswap.DeathSwap;
import com.chaseoes.deathswap.metadata.DSMetadata;
import com.chaseoes.deathswap.metadata.MetadataHelper;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        DSMetadata meta = MetadataHelper.getDSMetadata(event.getPlayer());
        if (meta != null && meta.isIngame()) {
            meta.getCurrentGame().leaveGame(event.getPlayer());
        }
        DeathSwap.getInstance().duelReqTimes.remove(event.getPlayer().getName());
    }
}
