package model;

import java.nio.file.Path;
import java.util.HashMap;

public class YGODeck extends HashMap<Integer, Integer> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final Path source;

    public YGODeck(Path source) {
        super();
        this.source = source;
    }

    public YGODeck() {
        this(null);
    }

    public static YGODeck getCastedInstance(HashMap<Integer, Integer> map) {
        YGODeck ygoDeck = new YGODeck();
        map.keySet().forEach(k -> ygoDeck.put(k, map.get(k)));
        return ygoDeck;
    }

    public Path getSource() {
        return source;
    }
}
