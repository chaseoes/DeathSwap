package me.chaseoes.deathswap.utilities;

import me.chaseoes.deathswap.metadata.MetadataHelper;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SwapState {

    private final String owner;
    private Location loc;
    private Entity vehicle;
    private Inventory inv;
    private org.bukkit.util.Vector velocity;
    private float fallDistance;

    private SwapState(Player owner) {
        this.owner = owner.getName();
    }

    public static SwapState getSwapState(Player player) {
        SwapState state = new SwapState(player);
        state.loc = player.getLocation();
        state.vehicle = player.getVehicle();
        state.inv = MetadataHelper.getDSMetadata(player).getLastOpened();
        state.velocity = player.getVelocity();
        state.fallDistance = player.getFallDistance();

        return state;
    }

    public void applySwapState(Player player) {
        player.leaveVehicle();
        player.closeInventory();

        player.teleport(loc);
        if (vehicle != null) {
            vehicle.setPassenger(player);
        }
        if (inv != null) {
            player.openInventory(inv);
        }
        player.setVelocity(velocity);
        player.setFallDistance(fallDistance);
    }
}
