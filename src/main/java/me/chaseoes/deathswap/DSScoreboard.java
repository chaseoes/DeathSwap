package me.chaseoes.deathswap;

import me.chaseoes.deathswap.metadata.MetadataHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.Map;

public class DSScoreboard {

    private final DSGame game;
    ScoreboardManager manager;
    Scoreboard board;
    Objective obj;
    private ArrayList<String> onBoard = new ArrayList<String>();
    private ArrayList<String> all = new ArrayList<String>();

    public DSScoreboard(DSGame game) {
        this.game = game;
        manager = Bukkit.getServer().getScoreboardManager();
        board = manager.getNewScoreboard();
        obj = board.registerNewObjective("DeathSwap", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(ChatColor.YELLOW + "DeathSwap");
    }

    public void addPlayer(Player player) {
        all.add(player.getName());
        updateBoard();
    }

    public void removePlayer(Player player) {
        all.remove(player.getName());
        board.resetScores(player);
        player.setScoreboard(manager.getMainScoreboard());
        updateBoard();
    }

    public void updateBoard() {
        if (DeathSwap.getInstance().getMap(game.getName()).getType() == GameType.PUBLIC) {
            for (String pl : all) {
                if (!Bukkit.getPlayerExact(pl).getScoreboard().equals(board)) {
                    Bukkit.getPlayerExact(pl).setScoreboard(board);
                }
            }
            ArrayList<String> arrayPlayers = new ArrayList<String>(all);
            Collections.sort(arrayPlayers, new GameplayerKillComparator());
            Set<Map.Entry<String, Integer>> entries = new HashSet<Map.Entry<String, Integer>>();
            for (int i = 0; i < 10 && i < arrayPlayers.size(); i++) {
                Player pl = Bukkit.getPlayerExact(arrayPlayers.get(i));
                entries.add(new AbstractMap.SimpleEntry<String, Integer>(pl.getName(), MetadataHelper.getDSMetadata(pl).getSwapKills()));
                onBoard.remove(pl.getName());
            }
            for (String pl : onBoard) {
                board.resetScores(getPlayer(pl));
            }
            onBoard.clear();
            for (Map.Entry<String, Integer> ent : entries) {
                Score score = obj.getScore(getPlayer(ent.getKey()));
                if (ent.getValue() == 0) {
                    score.setScore(1);
                }
                score.setScore(ent.getValue());
                onBoard.add(ent.getKey());
            }
        }
    }

    private OfflinePlayer getPlayer(String pl) {
        return Bukkit.getOfflinePlayer(pl);
    }

    public void resetAll() {
        for (String pl : all) {
            board.resetScores(getPlayer(pl));
        }
        all.clear();
    }

    class GameplayerKillComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            Player p1 = Bukkit.getPlayerExact(o1);
            Player p2 = Bukkit.getPlayerExact(o2);
            return MetadataHelper.getDSMetadata(p2).getSwapKills() - MetadataHelper.getDSMetadata(p1).getSwapKills();
        }
    }
}
