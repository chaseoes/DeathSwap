package com.chaseoes.deathswap;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import com.chaseoes.deathswap.utilities.SerializableLocation;

public class Map {

	String name;
	GameType type;
	Location p1;
	Location p2;
	int maxPlayers;
    RollbackType rollback;
    int swapMin;
    int swapMax;

	public Map(String n) {
		name = n;
		load();
	}

	public String getName() {
		return name;
	}

	public void setType(GameType type) {
		this.type = type;
		MapUtilities.getUtilities().getCustomConfig().set(getPath() + "type", type.getName());
		DeathSwap.getInstance().saveConfig();
	}

	public GameType getType() {
		return type;
	}

	public void load() {
        YamlConfiguration config = MapUtilities.getUtilities().getCustomConfig();
		String typ = config.getString(getPath() + "type");
		if (typ != null) {
			System.out.println("TYPE " + typ + "GT " + GameType.get(typ));
			type = GameType.get(typ);
			maxPlayers = config.getInt(getPath() + "max-players");
			p1 = SerializableLocation.getUtilities().stringToLocation(config.getString(getPath() + "region.p1"));
			p2 = SerializableLocation.getUtilities().stringToLocation(config.getString(getPath() + "region.p2"));
            String rb = config.getString(getPath() + "rollback");
            if (rb != null) {
                rollback = RollbackType.valueOf(rb);
            } else {
                rollback = RollbackType.BLOCKSTATE;
            }
            swapMin = config.getInt(getPath() + "swapmin", 20);
            swapMax = config.getInt(getPath() + "swapmax", 120);
		}
	}

	public Location getP1() {
        if (p1.getWorld() == null) {
            p1 = SerializableLocation.getUtilities().stringToLocation(MapUtilities.getUtilities().getCustomConfig().getString(getPath() + "region.p1"));
        }
		return p1;
	}

	public Location getP2() {
        if (p2.getWorld() == null) {
            p2 = SerializableLocation.getUtilities().stringToLocation(MapUtilities.getUtilities().getCustomConfig().getString(getPath() + "region.p2"));
        }
		return p2;
	}

	public void setP1(Location p1) {
		this.p1 = p1;
		MapUtilities.getUtilities().getCustomConfig().set(getPath() + "region.p1", SerializableLocation.getUtilities().locationToString(p1));
		MapUtilities.getUtilities().saveData();
	}

	public void setP2(Location p2) {
		this.p2 = p2;
		MapUtilities.getUtilities().getCustomConfig().set(getPath() + "region.p2", SerializableLocation.getUtilities().locationToString(p2));
		MapUtilities.getUtilities().saveData();
	}

	public void setMaxPlayers(int i) {
		this.maxPlayers = i;
		MapUtilities.getUtilities().getCustomConfig().set(getPath() + "max-players", i);
		MapUtilities.getUtilities().saveData();
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	private String getPath() {
		return "maps." + getName() + ".";
	}

    public RollbackType getRollback() {
        if (rollback != null) {
            return rollback;
        }
        return RollbackType.BLOCKSTATE;
    }

    public void setRollback(RollbackType rollback) {
        this.rollback = rollback;
        MapUtilities.getUtilities().getCustomConfig().set(getPath() + "rollback", rollback);
        MapUtilities.getUtilities().saveData();
    }

    public int getSwapMin() {
        return swapMin;
    }

    public void setSwapMin(int swapMin) {
        MapUtilities.getUtilities().getCustomConfig().set(getPath() + "swapmin", swapMin);
        MapUtilities.getUtilities().saveData();
        this.swapMin = swapMin;
    }

    public int getSwapMax() {
        return swapMax;
    }

    public void setSwapMax(int swapMax) {
        MapUtilities.getUtilities().getCustomConfig().set(getPath() + "swapmax", swapMax);
        MapUtilities.getUtilities().saveData();
        this.swapMax = swapMax;
    }
}
