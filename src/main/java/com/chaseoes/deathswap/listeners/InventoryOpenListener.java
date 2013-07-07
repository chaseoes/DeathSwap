package com.chaseoes.deathswap.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.PlayerInventory;

import com.chaseoes.deathswap.metadata.DSMetadata;
import com.chaseoes.deathswap.metadata.MetadataHelper;

public class InventoryOpenListener implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        DSMetadata meta = MetadataHelper.getDSMetadata((Player) event.getPlayer());
        if (meta.isIngame() && !(event.getInventory() instanceof PlayerInventory)) {
            meta.setLastOpened(event.getInventory());
        }
    }
}
