package me.chaseoes.deathswap;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import me.chaseoes.deathswap.listeners.*;
import me.chaseoes.deathswap.metadata.MetadataHelper;
import me.chaseoes.deathswap.utilities.DuelInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.EmptyClipboardException;

import me.chaseoes.deathswap.utilities.SerializableLocation;
import me.chaseoes.deathswap.utilities.WorldEditUtilities;

public class DeathSwap extends JavaPlugin {

	private static DeathSwap instance;
	HashMap<String, Map> maps = new HashMap<String, Map>();
	public HashMap<String, DSGame> games = new HashMap<String, DSGame>();
	public HashMap<String, DuelInfo> needsToAccept = new HashMap<String, DuelInfo>();
	public HashSet<String> disabled = new HashSet<String>();
	public HashSet<String> noRequests = new HashSet<String>();

	public static DeathSwap getInstance() {
		return instance;
	}

	public void onEnable() {
		instance = this;
		SerializableLocation.getUtilities().setup(this);
		WorldEditUtilities.getWEUtilities().setup(this);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new SignChangeListener(), this);
		pm.registerEvents(new PlayerJoinListener(), this);
		pm.registerEvents(new PlayerQuitListener(), this);
		pm.registerEvents(new PlayerDeathListener(), this);
		pm.registerEvents(new PlayerCommandPreproccessListener(), this);
		pm.registerEvents(new BlockListeners(), this);
		pm.registerEvents(new PlayerInteractListener(), this);
		pm.registerEvents(new EntityDamageListener(), this);
		pm.registerEvents(new PlayerChatListener(), this);
        pm.registerEvents(new InventoryCloseListener(), this);
        pm.registerEvents(new InventoryOpenListener(), this);

		if (getConfig().getConfigurationSection("maps") != null) {
			for (String map : getConfig().getConfigurationSection("maps").getKeys(false)) {
				maps.put(map, new Map(map));
				games.put(map, new DSGame(map, maps.get(map).getP1(), maps.get(map).getP2()));
			}
		}

		for (Player player : getServer().getOnlinePlayers()) {
			MetadataHelper.createDSMetadata(player);
		}

		getServer().getScheduler().runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				getServer().broadcastMessage(format("-------------------"));
				getServer().broadcastMessage(format("Server reloaded."));
				getServer().broadcastMessage(format("Changes to the DeathSwap plugin have been made!"));
				getServer().broadcastMessage(format("-------------------"));
			}
		}, 40L);
	}

	int roundUp(int n) {
		return (n + 8) / 9 * 9;
	}

	public void onDisable() {
		for (DSGame game : games.values()) {
			game.stopGame();
		}
		maps.clear();
        for (Player player : getServer().getOnlinePlayers()) {
            MetadataHelper.deleteDSMetadata(player);
        }
		instance = null;
	}

	public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
		if (cmnd.getName().equalsIgnoreCase("deathswap")) {
			if (strings.length == 0) {
				cs.sendMessage(format("Version " + getDescription().getVersion() + " by chaseoes."));
				cs.sendMessage(format("Type &b/ds help &7for help."));
				return true;
			}

			if (strings[0].equalsIgnoreCase("join")) {
				if (MetadataHelper.getDSMetadata((Player) cs).isIngame()) {
					cs.sendMessage(format("You are already in a game!"));
				} else if (cs.hasPermission("deathswap.play")) {
					if (strings.length == 2) {
						String map = strings[1];
						if (games.containsKey(map)) {
							if (!disabled.contains(map)) {
								games.get(map).joinGame((Player)cs);
							} else {
								cs.sendMessage(format("That map is currently disabled."));
							}
						} else {
							cs.sendMessage(format("That map does not exist!"));
						}
					} else {
						cs.sendMessage(format("Incorrect command syntax."));
						cs.sendMessage(format("Type &b/ds help &7for help."));
					}
				} else {
					cs.sendMessage(format("You don't have permission."));
				}
			}

			if (strings[0].equalsIgnoreCase("list")) {
				if (cs.hasPermission("deathswap.play")) {
					if (strings.length >= 1) {
						DSGame game = null;
						if (strings.length == 2) {
							String map = strings[1];
							if (games.containsKey(map)) {
								game = games.get(map);
							} else {
								cs.sendMessage(format("That map does not exist!"));
								return true;
							}
						}

						if (strings.length == 1) {
							if (MetadataHelper.getDSMetadata((Player) cs).isIngame()) {
								game = MetadataHelper.getDSMetadata((Player) cs).getCurrentGame();
							} else {
								cs.sendMessage(format("You're not in a game!"));
								return true;
							}
						}

						StringBuilder sb = new StringBuilder();
						for (String player : game.getPlayersIngame()) {
							sb.append(player + ", ");
						}

						cs.sendMessage(format(sb.toString().substring(0, sb.toString().length() - 2)));
					} else {
						cs.sendMessage(format("Incorrect command syntax."));
						cs.sendMessage(format("Type &b/ds help &7for help."));
					}
				} else {
					cs.sendMessage(format("You don't have permission."));
				}
			}

			if (strings[0].equalsIgnoreCase("duel")) {
				if (MetadataHelper.getDSMetadata((Player) cs).isIngame()) {
					cs.sendMessage(format("You are already in a game!"));
				} else if (cs.hasPermission("deathswap.play")) {
					if (strings.length == 3) {
						String map = strings[1];
						String p = strings[2];
						Player player = getServer().getPlayer(p);
						if (player == null) {
							cs.sendMessage(format("That player isn't online!"));
						} else if (!maps.containsKey(map)) {
							cs.sendMessage(format("That map does not exist!"));
						} else if (maps.get(map).getType() != GameType.PRIVATE) {
							cs.sendMessage(format("That map is not for dueling!"));
						} else if (games.get(map).getState() != GameState.WAITING) {
							cs.sendMessage(format("That map is currently ingame!"));
						} else if (MetadataHelper.getDSMetadata(player).isIngame()) {
							cs.sendMessage(format(p + " is already in a game!"));
						} else if (noRequests.contains(player.getName())) {
							cs.sendMessage(format("This player currently isn't accepting requests to duel."));
						} else {
							cs.sendMessage(format("You have requested to duel " + player.getName() + "."));
							player.sendMessage(format(cs.getName() + " has requested to duel you in a DeathSwap game!"));
							player.sendMessage(format("Type &b/ds accept &7to accept their request."));
							needsToAccept.put(player.getName(), new DuelInfo(cs.getName(), map));
						}
					} else if (strings.length == 2 && strings[1].equalsIgnoreCase("toggle")) {
						if (!noRequests.contains(cs.getName())) {
							noRequests.add(cs.getName());
							cs.sendMessage(format("Players are no longer allowed to send you requests to duel."));
						} else {
							noRequests.remove(cs.getName());
							cs.sendMessage(format("Players are now allowed to send you requests to duel."));
						}
					}
					else {
						cs.sendMessage(format("Incorrect command syntax."));
						cs.sendMessage(format("Type &b/ds help &7for help."));
					}
				} else {
					cs.sendMessage(format("You don't have permission."));
				}
			}


			if (strings[0].equalsIgnoreCase("accept")) {
				if (MetadataHelper.getDSMetadata((Player) cs).isIngame()) {
					cs.sendMessage(format("You are already in a game!"));
				} else if (cs.hasPermission("deathswap.play")) {
					if (strings.length == 1) {
						if (!needsToAccept.containsKey(cs.getName())) {
							cs.sendMessage(format("Nobody has requested to duel you!"));
						} else {
							DuelInfo info = needsToAccept.get(cs.getName());
							needsToAccept.remove(cs.getName());
							Player chall = Bukkit.getPlayerExact(info.getChallenger());
							DSGame game = games.get(info.getMap());
							if (chall == null) {
								cs.sendMessage(format("Your challenger is no longer online!"));
							} else if (game.getState() != GameState.WAITING) {
								cs.sendMessage(format("The requested map is already ingame!"));
							} else if (MetadataHelper.getDSMetadata(chall).isIngame()) {
								cs.sendMessage(format("Your challenger is already in a game!"));
							} else {
								cs.sendMessage(format("Accepted request"));
								game.joinGame(chall);
								game.joinGame((Player) cs);
							}
						}
					} else {
						cs.sendMessage(format("Incorrect command syntax."));
						cs.sendMessage(format("Type &b/ds help &7for help."));
					}
				} else {
					cs.sendMessage(format("You don't have permission."));
				}
			}

			if (strings[0].equalsIgnoreCase("leave")) {
				if (cs.hasPermission("deathswap.play")) {
					if (strings.length == 1) {
						if (MetadataHelper.getDSMetadata((Player) cs).isIngame()) {
							DSGame game = MetadataHelper.getDSMetadata((Player) cs).getCurrentGame();
							game.leaveGame((Player) cs);
							cs.sendMessage(format("You have left the game."));
						} else {
							cs.sendMessage(format("You aren't in a game."));
						}
					} else {
						cs.sendMessage(format("Incorrect command syntax."));
						cs.sendMessage(format("Type &b/ds help &7for help."));
					}
				} else {
					cs.sendMessage(format("You don't have permission."));
				}
			}

			if (strings[0].equalsIgnoreCase("enable")) {
				if (cs.hasPermission("deathswap.create")) {
					if (strings.length == 2) {
						String map = strings[1];
						if (games.containsKey(map)) {
							disabled.remove(map);
							cs.sendMessage(format("Successfully enabled " + map + "."));
						} else {
							cs.sendMessage(format("That map does not exist!"));
						}
					} else {
						cs.sendMessage(format("Incorrect command syntax."));
						cs.sendMessage(format("Type &b/ds help &7for help."));
					}
				} else {
					cs.sendMessage(format("You don't have permission."));
				}
			}

			if (strings[0].equalsIgnoreCase("disable")) {
				if (cs.hasPermission("deathswap.create")) {
					if (strings.length == 2) {
						String map = strings[1];
						if (games.containsKey(map)) {
							disabled.add(map);
							games.get(map).stopGame();
							cs.sendMessage(format("Successfully disabled " + map + "."));
						} else {
							cs.sendMessage(format("That map does not exist!"));
						}
					} else {
						cs.sendMessage(format("Incorrect command syntax."));
						cs.sendMessage(format("Type &b/ds help &7for help."));
					}
				} else {
					cs.sendMessage(format("You don't have permission."));
				}
			}

			if (strings[0].equalsIgnoreCase("create")) {
				if (cs.hasPermission("deathswap.create")) {
					if (strings.length == 4) {
						if (strings[1].equalsIgnoreCase("map")) {
							String mapName = strings[2];
							String mapType = strings[3];
							try {
								Map m = new Map(mapName);
								MapUtilities.getUtilities().createMap(m, (Player) cs, GameType.get(mapType), 20);
								maps.put(mapName, m);
								games.put(m.getName(), new DSGame(m.getName(), m.getP1(), m.getP2()));
								cs.sendMessage(format("Successfully created " + mapName + "!"));
							} catch (EmptyClipboardException e) {
								cs.sendMessage(format("You must select the map with WorldEdit first."));
							}
						}

						if (strings[1].equalsIgnoreCase("icon")) {
							String mapName = strings[2];
							String mapIcon = strings[3];
							if (games.containsKey(mapName)) {
								getConfig().set("maps." + mapName + ".icon", mapIcon.toUpperCase());
								saveConfig();
							} else {
								cs.sendMessage(format("That map does not exist."));
							}
						}
					} else {
						cs.sendMessage(format("Incorrect command syntax."));
						cs.sendMessage(format("Type &b/ds help &7for help."));
					}
				} else {
					cs.sendMessage(format("You don't have permission."));
				}
			}

			if (strings[0].equalsIgnoreCase("setmax")) {
				if (cs.hasPermission("deathswap.create")) {
					if (strings.length == 3) {
						String mapName = strings[1];
						try {
							int max = Integer.parseInt(strings[2]);
							if (maps.containsKey(mapName)) {
								Map m = maps.get(mapName);
								m.setMaxPlayers(max);
								cs.sendMessage(format("Successfully set the max players to " + max + "!"));
							} else {
								cs.sendMessage(format("That map does not exist!"));
							}
						} catch (Exception e) {
							cs.sendMessage(format("That isn't a number!"));
						}
					} else {
						cs.sendMessage(format("Incorrect command syntax."));
						cs.sendMessage(format("Type &b/ds help &7for help."));
					}
				} else {
					cs.sendMessage(format("You don't have permission."));
				}
			}

			if (strings[0].equalsIgnoreCase("help")) {
				cs.sendMessage(format("DeathSwap Commands:"));
				cs.sendMessage(ChatColor.GREEN + "/ds" + ChatColor.GRAY + ": General plugin information.");
				cs.sendMessage(ChatColor.GREEN + "/ds join <map name>" + ChatColor.GRAY + ": Joins the specified map.");
				cs.sendMessage(ChatColor.GREEN + "/ds leave" + ChatColor.GRAY + ": Leave the game you're in.");
				cs.sendMessage(ChatColor.GREEN + "/ds list [map]" + ChatColor.GRAY + ": List players in the given map.");
				cs.sendMessage(ChatColor.GREEN + "/ds create map <map name> <game type>" + ChatColor.GRAY + ": Create a DeathSwap map.");
				cs.sendMessage(ChatColor.GREEN + "/ds setmax <map> <#>" + ChatColor.GRAY + ": Set the max players for a map.");
			}
		}
		return true;
	}

	public Map getMap(String name) {
		return maps.get(name);
	}

	public String format(String s) {
		return ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Death" + ChatColor.GREEN + "Swap" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " " + ChatColor.translateAlternateColorCodes('&', s);
	}

	public void setLobbyLocation(Location l) {
		getConfig().set("lobby-location", SerializableLocation.getUtilities().locationToString(l));
		saveConfig();
	}

	public Location getLobbyLocation() {
		String loc = getConfig().getString("lobby-location");
		if (loc != null) {
			return SerializableLocation.getUtilities().stringToLocation(loc);
		}
		return Bukkit.getWorlds().get(0).getSpawnLocation();
	}

    public DSGame getGame(String name) {
        if (games.containsKey(name)) {
            return games.get(name);
        }
        return null;
    }

    public Collection<DSGame> getGames() {
        return games.values();
    }
}
