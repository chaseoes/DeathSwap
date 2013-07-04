package me.chaseoes.deathswap;

import java.util.Set;

import me.chaseoes.deathswap.utilities.IconMenu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DuelMenu {

	Player player;
	private IconMenu menu;

	public DuelMenu(final Player player, final Player playerToDuel) {
		this.player = player;
		Set<String> maps = DeathSwap.getInstance().getConfig().getConfigurationSection("maps").getKeys(false);
		menu = new IconMenu("Pick a map to duel on!", roundUp(maps.size()), new IconMenu.OptionClickEventHandler() {
			public void onOptionClick(IconMenu.OptionClickEvent event) {
				event.getPlayer().performCommand("ds duel " + ChatColor.stripColor(event.getName()) + " " + playerToDuel.getName());
				event.setWillClose(true);
				event.setWillDestroy(true);
			}
		});

		int i = 0;
		for (String map : maps) {
			if (DeathSwap.getInstance().games.containsKey(map)) {
				if (DeathSwap.getInstance().maps.get(map).getType() == GameType.PRIVATE) {
					i++;
					String players = ChatColor.GREEN + "" + DeathSwap.getInstance().games.get(map).getPlayersIngame().size() + " Players";
					menu.setOption(i, getIcon(map), ChatColor.RESET + "" + ChatColor.AQUA + map, players);
				}
			}
		}
	}
	
	public void open() {
		menu.open(player);
	}

	private ItemStack getIcon(String mapName) {
		String item = DeathSwap.getInstance().getConfig().getString("maps." + mapName + ".icon");
		ItemStack i;

		if (item != null) {
			try {
				int id = Integer.parseInt(item);
				i = new ItemStack(id, 1);
			} catch (NumberFormatException e) {
				i = new ItemStack(Material.getMaterial(item.toUpperCase()));
			}
		} else {
			i = new ItemStack(Material.BEDROCK, 1);
		}

		return i;
	}

	private int roundUp(int n) {
		return (n + 8) / 9 * 9;
	}

}
