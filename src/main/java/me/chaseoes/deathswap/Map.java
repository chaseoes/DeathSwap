package me.chaseoes.deathswap;

import me.chaseoes.deathswap.utilities.SerializableLocation;

import org.bukkit.Location;

public class Map {

	String name;
	GameType type;
	Location p1;
	Location p2;
	int maxPlayers;
    RollbackType rollback;

	public Map(String n) {
		name = n;
		load();
	}

	public String getName() {
		return name;
	}

	public void setType(GameType type) {
		this.type = type;
		DeathSwap.getInstance().getConfig().set(getPath() + "type", type.getName());
		DeathSwap.getInstance().saveConfig();
	}

	public GameType getType() {
		return type;
	}

	public void load() {
		String typ = DeathSwap.getInstance().getConfig().getString(getPath() + "type");
		if (typ != null) {
			System.out.println("TYPE " + typ + "GT " + GameType.get(typ));
			type = GameType.get(typ);
			maxPlayers = DeathSwap.getInstance().getConfig().getInt(getPath() + "max-players");
			p1 = SerializableLocation.getUtilities().stringToLocation(DeathSwap.getInstance().getConfig().getString(getPath() + "region.p1"));
			p2 = SerializableLocation.getUtilities().stringToLocation(DeathSwap.getInstance().getConfig().getString(getPath() + "region.p2"));
            String rb = DeathSwap.getInstance().getConfig().getString(getPath() + "rollback");
            if (rb != null) {
                rollback = RollbackType.valueOf(rb);
            } else {
                rollback = RollbackType.BLOCKSTATE;
            }
		}
	}

	public Location getP1() {
        if (p1.getWorld() == null) {
            p1 = SerializableLocation.getUtilities().stringToLocation(DeathSwap.getInstance().getConfig().getString(getPath() + "region.p1"));
        }
		return p1;
	}

	public Location getP2() {
        if (p2.getWorld() == null) {
            p2 = SerializableLocation.getUtilities().stringToLocation(DeathSwap.getInstance().getConfig().getString(getPath() + "region.p2"));
        }
		return p2;
	}

	public void setP1(Location p1) {
		this.p1 = p1;
		DeathSwap.getInstance().getConfig().set(getPath() + "region.p1", SerializableLocation.getUtilities().locationToString(p1));
		DeathSwap.getInstance().saveConfig();
	}

	public void setP2(Location p2) {
		this.p2 = p2;
		DeathSwap.getInstance().getConfig().set(getPath() + "region.p2", SerializableLocation.getUtilities().locationToString(p2));
		DeathSwap.getInstance().saveConfig();
	}

	public void setMaxPlayers(int i) {
		this.maxPlayers = i;
		DeathSwap.getInstance().getConfig().set(getPath() + "max-players", i);
		DeathSwap.getInstance().saveConfig();
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
        DeathSwap.getInstance().getConfig().set(getPath() + "rollback", rollback);
        DeathSwap.getInstance().saveConfig();
    }
}
