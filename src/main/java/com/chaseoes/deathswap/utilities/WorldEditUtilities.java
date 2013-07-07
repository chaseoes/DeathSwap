package com.chaseoes.deathswap.utilities;

import org.bukkit.plugin.Plugin;

import com.chaseoes.deathswap.DeathSwap;
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

}
