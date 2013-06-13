package me.chaseoes.deathswap.metadata;

import me.chaseoes.deathswap.DSGame;
import org.bukkit.entity.Player;

public class DSMetadata {

    private String lastSwappedTo = null;
    private int swapKills = 0;
    private DSGame currentGame = null;

    public String getLastSwappedTo() {
        return lastSwappedTo;
    }

    public void setLastSwappedTo(Player lastSwappedTo) {
        if (lastSwappedTo != null) {
            this.lastSwappedTo = lastSwappedTo.getName();
        } else {
            this.lastSwappedTo = null;
        }
    }

    public int getSwapKills() {
        return swapKills;
    }

    public int incSwapKills() {
        return ++swapKills;
    }

    public void resetSwapKills() {
        swapKills = 0;
    }

    public DSGame getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(DSGame currentGame) {
        this.currentGame = currentGame;
    }

    public boolean isIngame() {
        return currentGame != null;
    }

    public void reset() {
        currentGame = null;
        lastSwappedTo = null;
        swapKills = 0;
    }
}
