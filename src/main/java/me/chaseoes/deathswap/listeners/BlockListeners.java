package me.chaseoes.deathswap.listeners;

import me.chaseoes.deathswap.DSGame;
import me.chaseoes.deathswap.metadata.MetadataHelper;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListeners implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		if (MetadataHelper.getDSMetadata(event.getPlayer()).isIngame()) {
			DSGame g = MetadataHelper.getDSMetadata(event.getPlayer()).getCurrentGame();
			g.addBlock(event.getBlock().getState());
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (MetadataHelper.getDSMetadata(event.getPlayer()).isIngame()) {
			DSGame g = MetadataHelper.getDSMetadata(event.getPlayer()).getCurrentGame();
			g.addBlock(event.getBlockReplacedState());
		}
	}

}
