package controller;

import model.YGODeck;
import view.ErrorDialog;
import view.GraphicalConsole;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static controller.Decks.*;

public class Iflists {
    private static final String WHITELIST_FOLDER = Options.optionsImpl.paths.get("whitelist folder");

    private Iflists() {

    }

    public static void generateGeneral() throws IOException {
        generateTo(
                Paths.get(WHITELIST_FOLDER + Options.optionsImpl.generalWhitelistName),
                getAllcardsDeck(),
                Options.optionsImpl.applyBanlist,
                false);
    }

    public static void generateGoodcards() throws IOException {
        generateTo(
                Paths.get(WHITELIST_FOLDER + Options.optionsImpl.goodcardsWhitelistName),
                Decks.getLeftKeyRightValue(getGoodcardsDeck(), getAllcardsDeck()),
                Options.optionsImpl.applyBanlist,
                true);
    }

    public static void generateTrimmedGoodcards() throws IOException {
        generateTo(
                Paths.get(WHITELIST_FOLDER + Options.optionsImpl.trimmedGoodcardsWhitelistName),
                getIntersectingDeck(getGoodcardsDeck(), getAllcardsDeck()),
                Options.optionsImpl.applyBanlist,
                false);
    }

    public static void generateDraftWhitelists() throws IOException {
        if (Options.optionsImpl.deleteOldDrafts) deleteDraftWhitelists();
        AtomicBoolean generated = new AtomicBoolean(false);
        HashSet<String> generatedWhitelists = Options.optionsImpl.generatedDraftWhitlists;
        HashMap<Long, Path> lastModifiedMap = new HashMap<>();
        AtomicInteger counter = new AtomicInteger();
        int filequantity = Options.optionsImpl.draftExportQuantity;
        try (Stream<Path> paths = Files.walk(DRAFT_FOLDER)) {
            paths.filter(Files::isRegularFile)
                    .filter(Iflists::applyWhiteAndBlacklist)
                    .forEach(path -> lastModifiedMap.put(path.toFile().lastModified(), path));
            lastModifiedMap.keySet().stream().sorted().forEach(key -> {
                counter.getAndIncrement();
                if (counter.get() > lastModifiedMap.keySet().size() - ((filequantity == -1) ? lastModifiedMap.keySet().size() : filequantity)) {
                    try {
                        String whitelistName = Options.optionsImpl.draftExporterExtensions.get("prefix")
                                + lastModifiedMap.get(key).getFileName().toString().split("\\.")[0] + Options.optionsImpl.draftExporterExtensions.get("suffix");
                        String draftGoodcardsName = Options.optionsImpl.draftExporterExtensions.get("prefix") + "gc_" + whitelistName + Options.optionsImpl.draftExporterExtensions.get("suffix");
                        Path normalOut = Paths.get(WHITELIST_FOLDER + "\\" + whitelistName + ".conf");
                        Path goodcardOut = Paths.get(WHITELIST_FOLDER + "\\" + draftGoodcardsName + ".conf");
                        YGODeck draftDeck = Decks.importFromFile(lastModifiedMap.get(key));
                        Iflists.generateTo(
                                normalOut,
                                draftDeck,
                                false,
                                false,
                                normalOut.toString(),
                                whitelistName);
                        Iflists.generateTo(
                                goodcardOut,
                                Decks.getLeftKeyRightValue(getIntersectingDeck(getGoodcardsDeck(), draftDeck), getAllcardsDeck()),
                                Options.optionsImpl.applyBanlistGCDraft,
                                Options.optionsImpl.banlistVisibleGCDraft,
                                goodcardOut.toString(),
                                draftGoodcardsName);
                        generated.set(true);
                        generatedWhitelists.add(normalOut.toString());
                        generatedWhitelists.add(goodcardOut.toString());
                    } catch (Exception e) {
                        ErrorDialog ed = new ErrorDialog(e.getMessage());
                        ed.showDialog();
                    }
                }
            });
            if (!generated.get())
                GraphicalConsole.add("There was nothing to generate...");
            else
                Options.serializeOptions();
        }
    }

    public static void generateBanlistWhitelist() throws IOException {
        Path banlistPath = Paths.get(WHITELIST_FOLDER + Options.optionsImpl.exportBanlistName);
        BufferedWriter writer = Files.newBufferedWriter(banlistPath);
        writer.write("!Banlist\n");
        writer.write("$whitelist\n");
        Files.newBufferedReader(Paths.get(Options.optionsImpl.paths.get("banlist")))
                .lines()
                .forEach(line -> {
                    try {
                        writer.write(line + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        GraphicalConsole.add("Generated whitelist: " + banlistPath);
        writer.close();
    }

    public static void generateBanlistWhitelistTrimmed() throws IOException {
        generateTo(
                Paths.get(WHITELIST_FOLDER + Options.optionsImpl.exportBanlistTrimmedName),
                getIntersectingDeck(get(Paths.get(Options.optionsImpl.paths.get("banlist"))), getAllcardsDeck()),
                false,
                false
        );
    }

    public static HashSet<Integer> get(Path in) throws IOException {
        HashSet<Integer> banlist = new HashSet<>();
        Files.newBufferedReader(in).lines().filter(line -> !line.equals("")).forEach(
                line -> banlist.add(Integer.parseInt(line.split(" ")[0]))
        );
        return banlist;
    }

    public static boolean applyWhiteAndBlacklist(Path path) {
        boolean whitelisted = true;
        if (Options.optionsImpl.importFilter.get("whitelist").size() != 0) {
            // Check if file is whitelisted
            for (String s : Options.optionsImpl.importFilter.get("whitelist")) {
                whitelisted = path.toString().contains(s);
            }
        }
        // if the file is not in the non-empty whitelist return false
        if (!whitelisted)
            return false;
        // If there was no whitelist or the file is whitelisted -> Check if file is
        // blacklisted
        for (String s : Options.optionsImpl.importFilter.get("blacklist")) {
            if (path.toString().contains(s))
                return false;
        }
        return true;
    }

    public static void deleteDraftWhitelists() throws IOException {
        HashSet<String> itemsToDelete = new HashSet<>();
        Options.optionsImpl.generatedDraftWhitlists.forEach(path -> {
            try {
                Files.delete(Paths.get(path));
                GraphicalConsole.add("Deleted: " + path);
                itemsToDelete.add(path);
            } catch (IOException ignored) {
            }
        });
        Options.optionsImpl.generatedDraftWhitlists.removeAll(itemsToDelete);
        Options.serializeOptions();
    }

    public static void generateTo(Path out, YGODeck deck, boolean withBanlist, boolean banlistCardsVisible) throws IOException {
        String convertedFrom = deck.getSource() != null ? deck.getSource().toAbsolutePath().toString() : "unknown";
        String whitelistName = out.getFileName().toString().substring(0, out.getFileName().toString().length() - 5);
        generateTo(out, deck, withBanlist, banlistCardsVisible, convertedFrom, whitelistName);
    }

    public static void generateTo(Path out, YGODeck deck, boolean withBanlist, boolean banlistCardsVisible, String convertedFrom, String whitelistName) throws IOException {
        BufferedWriter whitelistWriter = Files.newBufferedWriter(out);
        whitelistWriter.write("Whitelist converted from: " + convertedFrom + "\n");
        whitelistWriter.write("!" + whitelistName + "\n");
        whitelistWriter.write("$whitelist\n");

        // insert whitelist
        // insert the cards
        for (Integer key : deck.keySet())
            whitelistWriter.write("" + key + " " + (deck.get(key) >= 3 ? 3 : deck.get(key)) + "\n");

        if (withBanlist) {
            writeBanlist(whitelistWriter, banlistCardsVisible);
        }

        GraphicalConsole.add("Generated whitelist: " + out);
        whitelistWriter.close();
    }

    private static void writeBanlist(BufferedWriter writer, boolean banlistCardsVisible) throws IOException {
        writer.write("\n#banlist from " + Paths.get(Options.optionsImpl.paths.get("banlist")) + ":" + "\n");

        int cardQuantity = banlistCardsVisible ? 0 : -1;
        Files.newBufferedReader(Paths.get(Options.optionsImpl.paths.get("banlist")))
                .lines()
                .forEach(line -> {
                    AtomicBoolean firstLine = new AtomicBoolean(true);
                    Arrays.stream(line.split(" ")).forEach(subline -> {
                        try {
                            if (firstLine.get()) {
                                writer.write(cardQuantity + " ");
                                firstLine.set(false);
                            } else
                                writer.write(subline + " ");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    try {
                        writer.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
