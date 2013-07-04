package me.chaseoes.deathswap.listeners;

import me.chaseoes.deathswap.metadata.DSMetadata;
import me.chaseoes.deathswap.metadata.MetadataHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

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
