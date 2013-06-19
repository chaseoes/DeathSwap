package me.chaseoes.deathswap.listeners;

import me.chaseoes.deathswap.metadata.DSMetadata;
import me.chaseoes.deathswap.metadata.MetadataHelper;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;

public class InventoryOpenListener implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        DSMetadata meta = MetadataHelper.getDSMetadata((Player) event.getPlayer());
        if (meta.isIngame() && !(event.getInventory() instanceof PlayerInventory)) {
            meta.setLastOpened(event.getInventory());
        }
    }
}
