package com.chaseoes.deathswap.listeners;


import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.chaseoes.deathswap.DSGame;
import com.chaseoes.deathswap.metadata.MetadataHelper;

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
