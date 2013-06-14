package me.chaseoes.deathswap;

public enum GameType {

	PUBLIC("public"), PRIVATE("private");

	private final String name;

	private GameType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static GameType get(String s) {
		if (s.equalsIgnoreCase("public")) {
			return GameType.PUBLIC;
		} else {
			return GameType.PRIVATE;
		}
	}

}
