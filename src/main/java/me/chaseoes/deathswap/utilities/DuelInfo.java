package me.chaseoes.deathswap.utilities;

public class DuelInfo {

    private final String challenger;
    private final String map;

    public DuelInfo(String challenger, String map) {
        this.challenger = challenger;
        this.map = map;
    }

    public String getChallenger() {
        return challenger;
    }

    public String getMap() {
        return map;
    }
}
