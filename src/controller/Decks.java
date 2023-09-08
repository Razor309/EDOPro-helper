package controller;

import model.YGODeck;
import view.ErrorDialog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Decks {
    public final static Path DRAFT_FOLDER = Paths.get(Options.optionsImpl.paths.get("draft folder"));
    public final static Path GOODCARDS_PATH = Paths.get(Options.optionsImpl.paths.get("goodcards"));
    private static YGODeck allcardsDeck;
    private static YGODeck goodcardsDeck;

    private Decks() {
    }

    private static Stream<Integer> getCardCodes(Stream<String> lines) {
        return lines.map(line -> {
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException ignored) {
                return null;
            }
        }).filter(Objects::nonNull);
    }

    /**
     *  Get the intersecting deck of d1 and d2. Amount is taken from d2.
     * @param d1
     * @param d2
     * @return intersection between d1 and d2 with amount of d2
     */
    public static YGODeck getIntersectingDeck(YGODeck d1, YGODeck d2) {
        Map<Integer, Integer> map = d1.keySet().stream().filter(d2::containsKey).collect(Collectors.toMap(k -> k, d2::get));
        return YGODeck.getCastedInstance((HashMap<Integer, Integer>) map);
    }

    public static YGODeck getIntersectingDeck(HashSet<Integer> banlist, YGODeck deck) {
        HashMap<Integer, Integer> map = new HashMap<>();
        banlist.stream().filter(deck::containsKey).forEach(cardid -> map.put(cardid, deck.get(cardid)));
        return YGODeck.getCastedInstance(map);
    }

    public static YGODeck getLeftKeyRightValue(YGODeck d1, YGODeck d2) {
        Map<Integer, Integer> map = d1.keySet().stream().collect(Collectors.toMap(k -> k, k -> {
            if (d2.containsKey(k)) return d2.get(k);
            return 0;
        }));
        return YGODeck.getCastedInstance((HashMap<Integer, Integer>) map);
    }

    public static YGODeck getAllcardsDeck() throws IOException {
        if (allcardsDeck == null)
            allcardsDeck = importFromDir(DRAFT_FOLDER);
        return allcardsDeck;
    }

    public static YGODeck getGoodcardsDeck() throws IOException {
        if (goodcardsDeck == null)
            goodcardsDeck = importFromFile(GOODCARDS_PATH);
        return goodcardsDeck;
    }

    public static YGODeck importFromDir(Path dir) throws IOException {
        YGODeck deck = new YGODeck(dir);
        try (final Stream<Path> ydkPaths = Files.walk(dir)
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().startsWith(".ydk", path.toString().length() - 4))
                .filter(Iflists::applyWhiteAndBlacklist)) {
            ydkPaths.forEach(path -> {
                try {
                    getCardCodes(Files.lines(path))
                            .forEach(cardCode -> {
                                if (deck.containsKey(cardCode))
                                    deck.put(cardCode, deck.get(cardCode) + 1);
                                else
                                    deck.put(cardCode, 1);
                            });
                } catch (IOException e) {
                    new ErrorDialog(e.getMessage()).showDialog();
                }
            });
        }
        return deck;
    }

    public static YGODeck importFromFile(Path path) throws IOException {
        YGODeck deck = new YGODeck(path);
        getCardCodes(Files.lines(path))
                .forEach(cardCode -> {
                    if (deck.containsKey(cardCode))
                        deck.put(cardCode, deck.get(cardCode) + 1);
                    else
                        deck.put(cardCode, 1);
                });
        return deck;
    }
}
