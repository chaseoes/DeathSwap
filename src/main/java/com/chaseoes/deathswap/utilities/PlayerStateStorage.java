package com.chaseoes.deathswap.utilities;

import org.bukkit.GameMode;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerStateStorage {
	
	private Player player;
	private boolean allowFlight;
	private float exhaustion;
	private float exp;
	private float flySpeed;
	private int foodLevel;
	private int level;
	private long playerTime;
	private boolean playerTimeRelative;
	private WeatherType playerWeather;
	private float saturation;
	private GameMode gameMode;
	private boolean flying;
	private ItemStack[] inventoryContents;
	private ItemStack[] armorContents;
	
	public PlayerStateStorage(Player p) {
		player = p;
		allowFlight = player.getAllowFlight();
		exhaustion = player.getExhaustion();
		exp = player.getExp();
		flySpeed = player.getFlySpeed();
		foodLevel = player.getFoodLevel();
		level = player.getLevel();
		playerTime = player.getPlayerTime();
		playerTimeRelative = player.isPlayerTimeRelative();
		playerWeather = player.getPlayerWeather();
		saturation = player.getSaturation();
		gameMode = player.getGameMode();
		flying = player.isFlying();
		inventoryContents = player.getInventory().getContents();
		armorContents = player.getInventory().getArmorContents();
	}
	
	public void clear() {
		player.setAllowFlight(false);
		player.setFlying(false);
		player.setExhaustion(0);
		player.setExp(0);
		player.setFoodLevel(20);
		player.setLevel(0);
		player.setSaturation(0);
		player.setGameMode(GameMode.SURVIVAL);
		player.closeInventory();
		player.getInventory().clear();
	}
	
	public void restore() {
		player.setAllowFlight(allowFlight);
		player.setExhaustion(exhaustion);
		player.setExp(exp);
		player.setFlySpeed(flySpeed);
		player.setFoodLevel(foodLevel);
		player.setLevel(level);
		player.setPlayerTime(playerTime, playerTimeRelative);
		player.setPlayerWeather(playerWeather);
		player.setSaturation(saturation);
		player.setGameMode(gameMode);
		player.setFlying(flying);
		player.getInventory().clear();
		player.getInventory().setContents(inventoryContents);
		player.getInventory().setArmorContents(armorContents);
	}
	
}
