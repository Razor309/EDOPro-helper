package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.stream.Stream;

import model.YGOCard;
import model.YGODeck;
import view.ErrorDialog;
import view.GraphicalConsole;

public class DeckHandler {
	private static File draftFolder = new File(OptionsHandler.options.paths.get("draft folder"));
	private static File deckFolder = new File(OptionsHandler.options.paths.get("deck folder"));
	private static YGODeck draftExport = null;
	private static boolean generated;

	private DeckHandler() {
	}

	public static void generateCards() throws IOException {
		if (draftExport == null)
			generateDraftExport();
		writeCards(draftExport);
		GraphicalConsole.add("- Converted 'draft export' cleaned up and trimmed to 3 cards max to "
				+ OptionsHandler.options.paths.get("cards") + "\n");

	}

	public static void generateDraftExport() throws IOException {
		HashSet<File> files = new HashSet<>();
		try (Stream<Path> paths = Files.walk(Paths.get(draftFolder.getCanonicalPath()))) {
			paths.filter(Files::isRegularFile).forEach(path -> {
				files.add(path.toFile());
			});
			if (!files.isEmpty()) {
				File draftExportFile = new File(OptionsHandler.options.paths.get("draft export"));
				FileMerger.mergeTo(draftExportFile, files);
				GraphicalConsole.add("- Merged files in: " + draftFolder + "\nto " + draftExportFile + "\n");
				draftExport = DeckHandler.importFrom(draftExportFile);
			}
		}
	}

	public static void generateTrimmedDeck() throws IOException {
		if (draftExport == null)
			generateDraftExport();
		writeUnlimitedTo(new File(OptionsHandler.options.paths.get("trimmeddeck")),
				compareAndTrim(importFrom(new File(OptionsHandler.options.paths.get("allcards"))), draftExport));
		GraphicalConsole.add("- Generated 'trimmeddeck' at " + OptionsHandler.options.paths.get("trimmeddeck"));
	}

	public static void generateDraftWhitelists() throws IOException {
		generated = false;
		try (Stream<Path> paths = Files.walk(Paths.get(draftFolder.getCanonicalPath()))) {
			paths.filter(Files::isRegularFile).filter(path -> {
				boolean whitelisted = true;
				if (OptionsHandler.options.draftExporterFilter.get("whitelist").size() != 0) {
					// Check if file is whitelisted
					for (String s : OptionsHandler.options.draftExporterFilter.get("whitelist")) {
						//
						whitelisted = false;
						if (path.toString().contains(s)) {
							whitelisted = true;
						}
					}
				}
				// if the file is not in the non empty whitelist return false
				if (!whitelisted)
					return false;
				// If there was no whitelist or the file is whitelisted -> Check if file is
				// blacklisted
				for (String s : OptionsHandler.options.draftExporterFilter.get("blacklist")) {
					if (path.toString().contains(s))
						return false;
				}
				return true;
			}).forEach(path -> {
				try {
					File out = new File(deckFolder + "\\" + OptionsHandler.options.draftExporterExtensions.get("prefix")
							+ path.toFile().getName() + OptionsHandler.options.draftExporterExtensions.get("suffix"));
					GraphicalConsole.add("Exported : " + path.toFile() + "\nas whitelist to\n" + out + "\n");
					WhitelistGenerator.generate(DeckHandler.importFrom(path.toFile()));
					generated = true;
				} catch (IOException e) {
					ErrorDialog ed = new ErrorDialog(e.getMessage());
					ed.showDialog();
				}
			});
			if (!generated)
				GraphicalConsole.add("There was nothing to generate...");
		}

	}

	public static YGODeck importFrom(File file) throws IOException {
		BufferedReader ydkReader = new BufferedReader(new FileReader(file));

		YGOCard currentCard = null;
		YGODeck deck = new YGODeck(file);

		String currentDeckType = YGODeck.MAIN;
		String line = ydkReader.readLine();
		while (line != null) {
			line = line.trim();
			try {
				currentCard = new YGOCard(Integer.parseInt(line), currentDeckType);
				if (deck.containsKey(currentCard)) {
					deck.put(currentCard, deck.get(currentCard) + 1);
				} else {
					deck.put(currentCard, 1);
				}
			} catch (NumberFormatException e) {
				// line is certainly not a currentCard because it is no number. So it may be a
				// deckTypeKeyword
				// check if it is a maindeck part or an extradeck part
				if (line.contains(YGODeck.MAIN)) {
					currentDeckType = YGODeck.MAIN;
				}
				if (line.contains(YGODeck.EXTRA)) {
					currentDeckType = YGODeck.EXTRA;
				}
				if (line.contains(YGODeck.SIDE)) {
					currentDeckType = YGODeck.SIDE;
				}
			}
			line = ydkReader.readLine();
		}

		ydkReader.close();
		return deck;
	}

	/**
	 * Compares d1 and d2 and returns all non intersecting cards
	 * 
	 * @param allcards
	 * @param drafts
	 * @return YGODeck consisting of all non intersecting cards in d1 and d2
	 */
	public static YGODeck compareAndTrim(YGODeck allcards, YGODeck drafts) {
		YGODeck trimmedDeck = new YGODeck();

		for (YGOCard card : allcards.keySet()) {
			// case 1: card doesn't exist in drafts -> add [card, allcards.get(card)] to
			// trimmedDeck
			// case 2: allcards has more of that cards than drafts -> add [card,
			// allcards.get(card) -
			// drafts.get(card)] to
			// trimmedDeck
			// else -> dismiss
			if (drafts.get(card) == null)
				trimmedDeck.put(card, allcards.get(card));
			else if (allcards.get(card) > drafts.get(card)) {
				trimmedDeck.put(card, allcards.get(card) - drafts.get(card));
			}
		}

		return trimmedDeck;
	}

	public static void writeCards(YGODeck deck) throws IOException {
		writeLimitedTo(new File(OptionsHandler.options.paths.get("cards")), deck, 3);
	}

	public static void writeLimitedTo(File out, YGODeck deck, int limit) throws IOException {
		BufferedWriter deckWriter = new BufferedWriter(new FileWriter(out));

		deckWriter.write(YGODeck.MAIN + "\n");
		for (YGOCard card : deck.getSetFor(YGODeck.MAIN)) {
			for (int i = 0; i < deck.get(card) && i < limit; i++)
				deckWriter.write(card.getCardID() + "\n");
		}

		deckWriter.write(YGODeck.EXTRA + "\n");
		for (YGOCard card : deck.getSetFor(YGODeck.EXTRA)) {
			for (int i = 0; i < deck.get(card) && i < limit; i++)
				deckWriter.write(card.getCardID() + "\n");
		}
		deckWriter.write(YGODeck.SIDE + "\n");
		for (YGOCard card : deck.getSetFor(YGODeck.SIDE)) {
			for (int i = 0; i < deck.get(card) && i < limit; i++)
				deckWriter.write(card.getCardID() + "\n");
		}
		deckWriter.close();
	}

	public static void writeUnlimitedTo(File out, YGODeck deck) throws IOException {
		BufferedWriter deckWriter = new BufferedWriter(new FileWriter(out));

		deckWriter.write(YGODeck.MAIN + "\n");
		for (YGOCard card : deck.getSetFor(YGODeck.MAIN)) {
			for (int i = 0; i < deck.get(card); i++)
				deckWriter.write(card.getCardID() + "\n");
		}

		deckWriter.write(YGODeck.EXTRA + "\n");
		for (YGOCard card : deck.getSetFor(YGODeck.EXTRA)) {
			for (int i = 0; i < deck.get(card); i++)
				deckWriter.write(card.getCardID() + "\n");
		}
		deckWriter.write(YGODeck.SIDE + "\n");
		for (YGOCard card : deck.getSetFor(YGODeck.SIDE)) {
			for (int i = 0; i < deck.get(card); i++)
				deckWriter.write(card.getCardID() + "\n");
		}
		deckWriter.close();
	}

	public static YGODeck getDraftExport() {
		return draftExport;
	}
}
