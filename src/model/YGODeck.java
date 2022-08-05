package model;

import java.nio.file.Path;
import java.util.HashMap;

public class YGODeck extends HashMap<Integer, Integer> {
	private final Path source;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public YGODeck(Path source) {
		super();
		this.source = source;
	}

	public static YGODeck getCastedInstance(HashMap<Integer, Integer> map) {
		YGODeck ygoDeck = new YGODeck();
		map.keySet().forEach(k -> ygoDeck.put(k, map.get(k)));
		return ygoDeck;
	}

	public YGODeck() {
		this(null);
	}

	public Path getSource() {
		return source;
	}
}
