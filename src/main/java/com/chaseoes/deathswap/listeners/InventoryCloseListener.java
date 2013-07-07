package com.chaseoes.deathswap.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.chaseoes.deathswap.metadata.DSMetadata;
import com.chaseoes.deathswap.metadata.MetadataHelper;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        DSMetadata meta = MetadataHelper.getDSMetadata((Player) event.getPlayer());
        if (meta.isIngame()) {
            meta.setLastOpened(null);
        } else {
            meta.setDuelMenuOpen(false);
        }
    }
}
