package me.chaseoes.deathswap;

import org.bukkit.Location;

public class Map {
	
	String name;
	GameType type;
	Location p1;
	Location p2;
	int maxPlayers;
	
	public Map(String n, GameType t) {
		name = n;
		type = t;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean exists() {
		return DeathSwap.getInstance().getConfig().getString(getPath() + "type", type.getName()) != null;
	}
	
	public void create() {
		DeathSwap.getInstance().getConfig().set(getPath() + "type", type.getName());
	}
	
	public void load() {
		type = GameType.get(DeathSwap.getInstance().getConfig().getString(getPath() + "type", type.getName()));
		maxPlayers = DeathSwap.getInstance().getConfig().getInt(getPath() + "max-players");
	}
	
    public void setP1(Location p1) {
        this.p1 = p1;
        DeathSwap.getInstance().getConfig().set(getPath() + "region.p1.w", p1.getWorld().getName());
        DeathSwap.getInstance().getConfig().set(getPath() + "region.p1.x", p1.getBlockX());
        DeathSwap.getInstance().getConfig().set(getPath() + "region.p1.y", p1.getBlockY());
        DeathSwap.getInstance().getConfig().set(getPath() + "region.p1.z", p1.getBlockZ());
        DeathSwap.getInstance().saveConfig();
    }

    public Location getP2() {
        return p2;
    }

    public void setP2(Location p2) {
        this.p2 = p2;
        DeathSwap.getInstance().getConfig().set("region.p2.w", p2.getWorld().getName());
        DeathSwap.getInstance().getConfig().set("region.p2.x", p2.getBlockX());
        DeathSwap.getInstance().getConfig().set("region.p2.y", p2.getBlockY());
        DeathSwap.getInstance().getConfig().set("region.p2.z", p2.getBlockZ());
        DeathSwap.getInstance().saveConfig();
    }
    
    public void setMaxPlayers(int i) {
    	this.maxPlayers = i;
    	DeathSwap.getInstance().getConfig().set(getPath() + "max-players", i);
    }
    
    public int getMaxPlayers() {
    	return this.maxPlayers;
    }

    private String getPath() {
    	return "maps." + getName() + ".";
    }
    
}
