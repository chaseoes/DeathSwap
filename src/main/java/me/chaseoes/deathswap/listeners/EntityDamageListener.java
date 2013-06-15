package me.chaseoes.deathswap.listeners;

import me.chaseoes.deathswap.metadata.MetadataHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
            if (ev.getDamager() instanceof Player) {
                MetadataHelper.getDSMetadata((Player) ev.getEntity()).setDeathBlame((Player) ev.getDamager());
            }
        }
    }
}
