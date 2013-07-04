package me.chaseoes.deathswap.metadata;

import me.chaseoes.deathswap.DSGame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class DSMetadata {

    private String deathBlame = null;
    private int swapKills = 0;
    private DSGame currentGame = null;
    private Inventory lastOpened = null;

    public String getLastSwappedTo() {
        return deathBlame;
    }

    public void setDeathBlame(Player deathBlame1) {
        if (deathBlame1 != null) {
            this.deathBlame = deathBlame1.getName();
        } else {
            this.deathBlame = null;
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
        deathBlame = null;
        swapKills = 0;
        lastOpened = null;
    }

    public String getDeathBlame() {
        return deathBlame;
    }

    public Inventory getLastOpened() {
        return lastOpened;
    }

    public void setLastOpened(Inventory lastOpened) {
        this.lastOpened = lastOpened;
    }
}
