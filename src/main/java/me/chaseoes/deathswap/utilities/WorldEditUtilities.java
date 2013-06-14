package me.chaseoes.deathswap.utilities;

import me.chaseoes.deathswap.DeathSwap;

import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class WorldEditUtilities {

    @SuppressWarnings("unused")
    private DeathSwap plugin;
    private static WorldEditPlugin worldEditPlugin;
    static WorldEditUtilities instance = new WorldEditUtilities();

    private WorldEditUtilities() {

    }

    public static WorldEditUtilities getWEUtilities() {
        return instance;
    }

    public void setup(DeathSwap p) {
        plugin = p;
        Plugin pl = p.getServer().getPluginManager().getPlugin("WorldEdit");
        if (pl != null && pl instanceof WorldEditPlugin) {
        	worldEditPlugin = (WorldEditPlugin) pl;
        }
    }
    public static WorldEditPlugin getWorldEdit() {
        return worldEditPlugin;
    }

    //public boolean isInMap(Entity entity, Map map) {
        //return isInMap(entity.getLocation(), map);
   // }

    //public boolean isInMap(Location loc, Map map) {
       // Selection sel = new CuboidSelection(map.getP1().getWorld(), map.getP1(), map.getP2());
      //  return sel.contains(loc);
   // }
}
