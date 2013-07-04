package me.chaseoes.deathswap.metadata;

import me.chaseoes.deathswap.DeathSwap;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class MetadataHelper {

    private static final String METADATA_NAME = "deathswap";

    public static DSMetadata getDSMetadata(Player player) {
        List<MetadataValue> vals = player.getMetadata(METADATA_NAME);
        for (MetadataValue val : vals) {
            if (val.getOwningPlugin().getName().equalsIgnoreCase(DeathSwap.getInstance().getName())) {
                return (DSMetadata) val.value();
            }
        }
        return null;
    }

    public static void createDSMetadata(Player player) {
        player.setMetadata(METADATA_NAME, new FixedMetadataValue(DeathSwap.getInstance(), new DSMetadata()));
    }

    public static void deleteDSMetadata(Player player) {
        player.removeMetadata(METADATA_NAME, DeathSwap.getInstance());
    }
}
