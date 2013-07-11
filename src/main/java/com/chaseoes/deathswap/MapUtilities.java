package com.chaseoes.deathswap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.chaseoes.deathswap.utilities.WorldEditUtilities;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class MapUtilities {

    static MapUtilities instance = new MapUtilities();
    private File customConfigFile;
    private YamlConfiguration customConfig;

    private MapUtilities() {

    }

    public static MapUtilities getUtilities() {
        return instance;
    }

    public void createMap(Map map, Player p, GameType type, int maxPlayers) throws EmptyClipboardException {
        System.out.println(map.getName() + " " + p + " " + type + " " + maxPlayers);
        System.out.println(WorldEditUtilities.getWorldEdit());
        Selection sel = WorldEditUtilities.getWorldEdit().getSelection(p);
        if (sel != null) {
            Location b1 = new Location(p.getWorld(), sel.getNativeMinimumPoint().getBlockX(), sel.getNativeMinimumPoint().getBlockY(), sel.getNativeMinimumPoint().getBlockZ());
            Location b2 = new Location(p.getWorld(), sel.getNativeMaximumPoint().getBlockX(), sel.getNativeMaximumPoint().getBlockY(), sel.getNativeMaximumPoint().getBlockZ());
            map.setP1(b1);
            map.setP2(b2);
            map.setMaxPlayers(maxPlayers);
            map.setType(type);
        } else {
            throw new EmptyClipboardException();
        }
    }

    public void reloadWorld(final Map map) {
        final File zip = new File(DeathSwap.getInstance().getDataFolder(), map.getName() + ".zip");
        if (!zip.exists()) {

        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv remove " + map.getName());
        final File world = new File(Bukkit.getWorldContainer(), map.getName());
        Bukkit.getScheduler().runTaskAsynchronously(DeathSwap.getInstance(), new Runnable() {
            @Override
            public void run() {
                rmRecursive(world);
                world.mkdir();
                try {
                    ZipFile file = new ZipFile(zip);
                    Enumeration<? extends ZipEntry> entries = file.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = entries.nextElement();
                        File worldEntry = new File(world, entry.getName());
                        worldEntry.getParentFile().mkdirs();
                        worldEntry.createNewFile();
                        InputStream stream = file.getInputStream(entry);
                        FileOutputStream fos = new FileOutputStream(worldEntry);
                        int data;
                        while ((data = stream.read()) != -1) {
                            fos.write(data);
                        }
                        fos.flush();
                        fos.close();
                        stream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (DeathSwap.getInstance().isEnabled()) {
                    Bukkit.getScheduler().runTask(DeathSwap.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv import " + map.getName() + " normal");
                            DeathSwap.getInstance().getGame(map.getName()).setState(GameState.WAITING);
                            DeathSwap.getInstance().getGame(map.getName()).getQueue().check();
                        }
                    });
                }
            }
        });
    }

    private void rmRecursive(File file) {
        if (file.isDirectory() && file.listFiles().length != 0) {
            for (File f : file.listFiles()) {
                rmRecursive(f);
            }
        }
        file.delete();
    }

    public void reloadDataConfig() {
        try {
            if (customConfigFile == null) {
                customConfigFile = new File(DeathSwap.getInstance().getDataFolder(), "data.yml");
            }
            customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getCustomConfig() {
        if (customConfig == null) {
            reloadDataConfig();
        }
        return customConfig;
    }

    public void saveData() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            getCustomConfig().save(customConfigFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
