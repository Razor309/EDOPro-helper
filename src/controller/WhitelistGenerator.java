package controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import model.YGODeck;
import view.ErrorDialog;
import view.GraphicalConsole;

import static controller.DeckHandler.draftFolder;

public class WhitelistGenerator {
	private static final String WHITELIST_FOLDER = OptionsHandler.options.paths.get("whitelist folder");

	private WhitelistGenerator() {

	}

	public static void generateGeneral() throws IOException {
		generateTo(
				Paths.get(WHITELIST_FOLDER + OptionsHandler.options.generalWhitelistName),
				DeckHandler.getAllcardsDeck(),
				OptionsHandler.options.applyBanlist);
	}

	public static void generateGoodcards() throws IOException {
		generateTo(
				Paths.get(WHITELIST_FOLDER + OptionsHandler.options.goodcardsWhitelistName),
				DeckHandler.getGoodcardsDeck(),
				false);
	}

	public static void generateTrimmedGoodcards() throws IOException {
		generateTo(
				Paths.get(WHITELIST_FOLDER + OptionsHandler.options.trimmedGoodcardsWhitelistName),
				DeckHandler.getIntersectingDeck(DeckHandler.getGoodcardsDeck(), DeckHandler.getAllcardsDeck()),
				OptionsHandler.options.applyBanlist);
	}

	public static void generateDraftWhitelists() throws IOException {
		AtomicBoolean generated = new AtomicBoolean(false);
		try (Stream<Path> paths = Files.walk(draftFolder)) {
			paths.filter(Files::isRegularFile).filter(path -> {
				boolean whitelisted = true;
				if (OptionsHandler.options.draftExporterFilter.get("whitelist").size() != 0) {
					// Check if file is whitelisted
					for (String s : OptionsHandler.options.draftExporterFilter.get("whitelist")) {
						whitelisted = path.toString().contains(s);
					}
				}
				// if the file is not in the non-empty whitelist return false
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
					String whitelistName = OptionsHandler.options.draftExporterExtensions.get("prefix")
							+ path.getFileName().toString().split("\\.")[0] + OptionsHandler.options.draftExporterExtensions.get("suffix");
					Path out = Paths.get( WHITELIST_FOLDER + "\\" + whitelistName + ".conf");
					WhitelistGenerator.generateTo(out, DeckHandler.importFromFile(path), false, out.toString(), whitelistName);
					generated.set(true);
				} catch (Exception e) {
					ErrorDialog ed = new ErrorDialog(e.getMessage());
					ed.showDialog();
				}
			});
			if (!generated.get())
				GraphicalConsole.add("There was nothing to generate...");
		}
	}

	public static void generateTo(Path out, YGODeck deck, boolean withBanlist) throws IOException {
		String convertedFrom = deck.getSource() != null ? deck.getSource().toAbsolutePath().toString() : "unknown";
		String whitelistName = out.getFileName().toString().substring(0, out.getFileName().toString().length() - 5);
		generateTo(out, deck, withBanlist, convertedFrom, whitelistName);
	}

	public static void generateTo(Path out, YGODeck deck, boolean withBanlist, String convertedFrom, String whitelistName) throws IOException {
		BufferedWriter whitelistWriter = Files.newBufferedWriter(out);
		whitelistWriter.write("Whitelist converted from: " + convertedFrom + "\n");
		whitelistWriter.write("!" + whitelistName + "\n");
		whitelistWriter.write("$whitelist\n");

		// insert whitelist
		// insert the cards
		for (Integer key : deck.keySet())
			whitelistWriter.write("" + key + " " + (deck.get(key) >= 3 ? 3 : deck.get(key)) + "\n");

		if (withBanlist) {
			whitelistWriter.write("\n#banlist from " + Paths.get(OptionsHandler.options.paths.get("banlist")) + ":" + "\n");
			Files.newBufferedReader(Paths.get(OptionsHandler.options.paths.get("banlist")))
					.lines()
					.forEach(line -> {
						try {
							whitelistWriter.write(line + "\n");
						} catch (IOException e) {
							new ErrorDialog(e.getMessage()).showDialog();
						}
					});
		}

		GraphicalConsole.add("- Generated whitelist: " + out);
		whitelistWriter.close();
	}

	// Banlist included in whitelist
//	public static void generateTo(Path out, YGODeck deck, boolean withBanlist) throws IOException {
//		BufferedWriter whitelistWriter = Files.newBufferedWriter(out);
//		whitelistWriter.write("#whitelist converted from " + (deck.getSource() != null ? deck.getSource().toAbsolutePath() : "unknown")
//				+ "\n!" + out.getFileName().toString().substring(0, out.getFileName().toString().length() - 5)
//				+ "\n$whitelist\n");
//
//		// insert whitelist
//		// insert the cards
//		if (withBanlist)
//			for (Integer key : deck.keySet())
//				whitelistWriter.write("" + key + " " + (deck.get(key) >= 3 ? 3 : deck.get(key)) + "\n");
//		else {
//			HashMap<Integer, Integer> banlist = null;
//			try {
//				 banlist = (HashMap<Integer, Integer>) Files.newBufferedReader(
//								Paths.get(OptionsHandler.options.paths.get("banlist")))
//						.lines()
//						.filter(line -> !line.equals(""))
//						.collect(
//								Collectors.toMap(
//										line -> Integer.parseInt(line.split(" ")[0]),
//										line -> Integer.parseInt(line.split(" ")[1])
//								)
//						);
//			} catch (Exception e) {
//				new ErrorDialog(e.getClass().toString().split(" ")[1] + " " + e.getMessage().toLowerCase(Locale.ROOT)).showDialog();
//			}
//
//			for (Integer key : deck.keySet()) {
//				assert banlist != null;
//				if (banlist.containsKey(key)){
//					if (banlist.get(key) > 0) {
//						Integer banlistValue = banlist.get(key);
//						Integer deckValue = deck.get(key);
//						whitelistWriter.write("" + key + " " + (deckValue >= banlistValue ? banlistValue : deckValue) + "\n");
//					}
//				} else
//					whitelistWriter.write("" + key + " " + (deck.get(key) >= 3 ? 3 : deck.get(key)) + "\n");
//			}
//		}
//
//		GraphicalConsole.add("- Generated whitelist: " + out);
//		whitelistWriter.close();
//	}
}
