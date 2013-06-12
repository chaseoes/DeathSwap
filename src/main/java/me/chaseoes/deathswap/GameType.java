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
		for (GameType type : GameType.values()) {
			if (type.getName().equalsIgnoreCase(s)) {
				return type;        
			}
		}
		return null;
	}

}
