package me.chaseoes.deathswap;

import me.chaseoes.deathswap.lobbysigns.LobbySign;
import me.chaseoes.deathswap.metadata.DSMetadata;
import me.chaseoes.deathswap.metadata.MetadataHelper;

import me.chaseoes.deathswap.utilities.PlayerStateStorage;
import me.chaseoes.deathswap.utilities.SwapState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sun.security.provider.PolicyParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

public class DSGame {

	private String name;
	private ArrayList<String> players = new ArrayList<String>();
	private LinkedHashMap<Location, BlockState> changedBlocks = new LinkedHashMap<Location, BlockState>();
	private HashMap<String, PlayerStateStorage> playerStates = new HashMap<String, PlayerStateStorage>();
	private Random rand = new Random();
	private GameState state = GameState.WAITING;
	private Location lowerBound;
	private Location upperBound;
	private World world;
	private int swapId = -1;
	private LobbySign sign;
    private GameQueue queue;
    private DSScoreboard board;

    public DSGame(String name, Location loc1, Location loc2) {
		this.name = name;
		lowerBound = new Location(loc1.getWorld(), Math.min(loc1.getBlockX(), loc2.getBlockX()), 0, Math.min(loc1.getBlockZ(), loc2.getBlockZ()));
		upperBound = new Location(loc1.getWorld(), Math.max(loc1.getBlockX(), loc2.getBlockX()), loc1.getWorld().getMaxHeight(), Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
		world = loc1.getWorld();
		sign = new LobbySign(DeathSwap.getInstance().getMap(name));
        queue = new GameQueue(this);
        board = new DSScoreboard(this);
	}

	public ArrayList<String> getPlayersIngame() {
		return players;
	}

    public void rollbackBlocks() {
        switch (DeathSwap.getInstance().getMap(name).getRollback()) {
            case WORLD:
                final File zip = new File(DeathSwap.getInstance().getDataFolder(), getName() + ".zip");
                if (zip.exists()) {
                    MapUtilities.getUtilities().reloadWorld(DeathSwap.getInstance().getMap(name));
                    break;
                }
            case BLOCKSTATE:
            default:
                for (BlockState state : changedBlocks.values()) {
                    state.update(true);
                }
                state = GameState.WAITING;
                queue.check();
                break;

        }
        changedBlocks.clear();
    }

	public void addBlock(BlockState state) {
        if (DeathSwap.getInstance().getMap(name).getRollback() == RollbackType.BLOCKSTATE && !changedBlocks.containsKey(state.getLocation())) {
			changedBlocks.put(state.getLocation(), state);
		}
	}

	private void hideOtherPlayers(Player player) {
		List<Player> playersToHide = new ArrayList<Player>();
		for (Player p : DeathSwap.getInstance().getServer().getOnlinePlayers()) {
			playersToHide.add(p);
		}

		for (String p : getPlayersIngame()) {
			Player pl = DeathSwap.getInstance().getServer().getPlayerExact(p);
			playersToHide.remove(pl);
		}

		for (Player p : playersToHide) {
			player.hidePlayer(p);
		}
	}

	private void showOtherPlayers(Player player) {
		for (Player p : DeathSwap.getInstance().getServer().getOnlinePlayers()) {
			player.showPlayer(p);
		}
	}

	public void swap() {
		Collections.shuffle(players, rand);
		ArrayList<SwapState> states = new ArrayList<SwapState>();
		ArrayList<Player> pls = new ArrayList<Player>();

		for (String pl : players) {
			Player p = Bukkit.getPlayer(pl);
			states.add(SwapState.getSwapState(p));
			pls.add(p);
		}

		for (int i = 0; i < players.size(); i++) {
			states.get((i + 1) % players.size()).applySwapState(pls.get(i));
			MetadataHelper.getDSMetadata(pls.get(i)).setDeathBlame(pls.get((i + 1) % players.size()));
		}
	}


    public void joinGame(Player player) {
        if (!queue.gameHasRoom() || state == GameState.INGAME) {
            queue.add(player);
            player.sendMessage(DeathSwap.getInstance().format("You are number " + queue.getPosition(player) + " in the queue"));
        } else if (state == GameState.WAITING) {
            if (DeathSwap.getInstance().getMap(name).getType() == GameType.PRIVATE) {
                //TODO: Approval of join goes here
                players.add(player.getName());
                MetadataHelper.getDSMetadata(player).setCurrentGame(this);
                player.sendMessage(DeathSwap.getInstance().format("Successfully joined the map " + name + "!"));
                broadcast(DeathSwap.getInstance().format(player.getName() + " has joined the game!"));
                if (players.size() == 2) {
                    startGame();
                }
                board.addPlayer(player);
            } else if (queue.gameHasRoom()) {
                queue.remove(player);
                players.add(player.getName());
                DSMetadata meta = MetadataHelper.getDSMetadata(player);
                meta.setCurrentGame(this);
                player.sendMessage(DeathSwap.getInstance().format("Successfully joined the map " + name + "!"));
                broadcast(DeathSwap.getInstance().format(player.getName() + " has joined the game!"));
                if (players.size() >= (DeathSwap.getInstance().getMap(name).getMaxPlayers())) {
                    startGame();
                }
                board.addPlayer(player);
            } else {
                queue.add(player);
                player.sendMessage("You are number " + (queue.getPosition(player) + 1) + " in the queue");
                return;
            }
        } else if (state == GameState.ROLLBACK) {
            player.sendMessage(DeathSwap.getInstance().format("That game is currently rolling back."));
        }
        sign.update();
    }

	public void leaveGame(Player player) {
		players.remove(player.getName());
		player.getInventory().addItem(new ItemStack(Material.WATCH, 1));
		broadcast(DeathSwap.getInstance().format(player.getName() + " has left the game."));
		if (state == GameState.INGAME) {
			if (players.size() == 1) {
				winGame(Bukkit.getPlayerExact(players.get(0)));
			}
		}
        board.removePlayer(player);
		MetadataHelper.getDSMetadata(player).reset();
		player.teleport(DeathSwap.getInstance().getLobbyLocation());
        playerStates.get(player.getName()).restore();
        playerStates.remove(player.getName());
		sign.update();
		showOtherPlayers(player);
        for (String pl : players) {
            hideOtherPlayers(Bukkit.getPlayerExact(pl));
        }
        queue.check();
	}

	public void winGame(Player player) {
		broadcast(DeathSwap.getInstance().format(player.getName() + " won on the map " + name + "!"));
		stopGame();
	}

	public void stopGame() {
		broadcast(DeathSwap.getInstance().format("The game has ended."));
		stopSwapTask();
		for (String p : players) {
			Player player = Bukkit.getPlayerExact(p);
			MetadataHelper.getDSMetadata(player).reset();
			player.teleport(DeathSwap.getInstance().getLobbyLocation());
			showOtherPlayers(player);
			playerStates.get(p).restore();
		}
        board.resetAll();
		players.clear();
        playerStates.clear();
		state = GameState.ROLLBACK;
		rollbackBlocks();
		sign.update();
	}

	public void startGame() {
		broadcast(DeathSwap.getInstance().format("The game has started! Good luck!"));
		startSwapTimer();
		teleportToRandomSpawns();
		state = GameState.INGAME;
		for (String p : getPlayersIngame()) {
			Player player = DeathSwap.getInstance().getServer().getPlayerExact(p);
			PlayerStateStorage state = new PlayerStateStorage(player);
			playerStates.put(p, state);
			state.clear();
		}
	}

	public void startSwapTimer() {
		if (swapId == -1) {
			swapId = Bukkit.getScheduler().runTaskTimer(DeathSwap.getInstance(), new Runnable() {
                Map map = DeathSwap.getInstance().getMap(getName());
				int diff = map.getSwapMax() - map.getSwapMin();
				int currTime = 0;
				int currRand = rand.nextInt(diff) + map.getSwapMin();
				@Override
				public void run() {
					if (currTime > currRand) {
						broadcast(DeathSwap.getInstance().format("Commencing swap!"));
						swap();
                        if (diff != 0) {
						    currRand = rand.nextInt(diff) + map.getSwapMin();
                        } else {
                            currRand = map.getSwapMax();
                        }
						currTime = 0;
					} else {
						currTime++;
					}
				}
			}, 20L, 20L).getTaskId();
		}
	}

	public void stopSwapTask() {
		if (swapId != -1) {
			Bukkit.getScheduler().cancelTask(swapId);
			swapId = -1;
		}
	}

	//Hell method
	public void teleportToRandomSpawns() {
		double scale = Math.ceil(Math.sqrt(players.size() * 9));
		double xDist = upperBound.getBlockX() - lowerBound.getBlockX();
		double zDist = upperBound.getBlockZ() - lowerBound.getBlockZ();
		double xDistOvScale = xDist / scale;
		double zDistOvScale = zDist / scale;
		ArrayList<ArrayList<PartCoords>> grid = new ArrayList<ArrayList<PartCoords>>((int) scale);

		for (int i = 0; i < scale; i++) {
			ArrayList<PartCoords> arrayList = new ArrayList<PartCoords>((int) scale);
			grid.add(arrayList);
			for (int j = 0; j < scale; j++) {
				arrayList.add(new PartCoords(i, j));
			}
		}

		ArrayList<Location> locs = new ArrayList<Location>(players.size());

		for (int i = 0; i < players.size(); i++) {
			ArrayList<PartCoords> coords = grid.get(rand.nextInt(grid.size()));
			PartCoords pc = coords.get(rand.nextInt(coords.size()));
			Location lower = new Location(world, lowerBound.getBlockX() + (xDistOvScale * (double) pc.x), 0, lowerBound.getBlockZ() + (zDistOvScale * (double) pc.z));
			Location upper = new Location(world, lowerBound.getBlockX() + (xDistOvScale * (double) (pc.x + 1)), 0, lowerBound.getBlockZ() + (zDistOvScale * (double) (pc.z + 1)));
			locs.add(getRandomLoc(lower, upper));

			for (int j = -1 + pc.x; j < (2 + pc.x); j++) {
				for (int k = -1 + pc.z; k < (2 + pc.z); k++) {
					if (j < 0 || k < 0) {
						continue;
					} else {
						for (ArrayList<PartCoords> aL : grid) {
							for (int m = 0; m < aL.size(); m++) {
								PartCoords part = aL.get(m);
								if (part.x == j && part.z == k) {
									aL.remove(m);
								}
							}
						}
					}
				}
			}

			Iterator<ArrayList<PartCoords>> it = grid.iterator();
			while(it.hasNext()) {
				if (it.next().isEmpty()) {
					it.remove();
				}
			}

		}

		for (int i = 0; i < players.size(); i++) {
			Player player = DeathSwap.getInstance().getServer().getPlayerExact(players.get(i));
			player.teleport(locs.get(i));
			hideOtherPlayers(player);
		}
	}

	//Hell's partner in crime
	public Location getRandomLoc(Location loc1, Location loc2) {
		int dx = Math.max(loc1.getBlockX(), loc2.getBlockX()) - Math.min(loc1.getBlockX(), loc2.getBlockX());
		int dz = Math.max(loc1.getBlockZ(), loc2.getBlockZ()) - Math.min(loc1.getBlockZ(), loc2.getBlockZ());
		System.out.println(dx);
		int rx = rand.nextInt(dx);
		int rz = rand.nextInt(dz);
		int x = Math.min(loc1.getBlockX(), loc2.getBlockX()) + rx;
		int z = Math.min(loc1.getBlockZ(), loc2.getBlockZ()) + rz;
		int y = world.getHighestBlockYAt(x, z) + 1;
		return new Location(world, x, y, z);
	}

	public void broadcast(String message) {
		for (String str : players) {
			Bukkit.getPlayerExact(str).sendMessage(message);
		}
	}

	public GameState getState() {
		return state;
	}

    public String getName() {
        return name;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public GameQueue getQueue() {
        return queue;
    }

    class PartCoords {
		int x;
		int z;

		PartCoords(int x, int z) {
			this.x = x;
			this.z = z;
		}
	}
}
