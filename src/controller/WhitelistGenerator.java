package controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import model.YGODeck;
import view.ErrorDialog;
import view.GraphicalConsole;

public class WhitelistGenerator {
	private WhitelistGenerator() {

	}

	public static void generateGeneral() throws IOException {
		generateTo(
				Paths.get(OptionsHandler.options.paths.get("whitelist folder") + OptionsHandler.options.generalWhitelistName),
				DeckHandler.getAllcardsDeck(),
				OptionsHandler.options.applyBanlist);
	}

	public static void generateGoodcards() throws IOException {
		generateTo(
				Paths.get(OptionsHandler.options.paths.get("whitelist folder") + OptionsHandler.options.goodcardsWhitelistName),
				DeckHandler.getGoodcardsDeck(),
				false);
	}

	public static void generateTrimmedGoodcards() throws IOException {
		generateTo(
				Paths.get(OptionsHandler.options.paths.get("whitelist folder") + OptionsHandler.options.trimmedGoodcardsWhitelistName),
				DeckHandler.getIntersectingDeck(DeckHandler.getGoodcardsDeck(), DeckHandler.getAllcardsDeck()),
				OptionsHandler.options.applyBanlist);
	}

	public static void generateTo(Path out, YGODeck deck, boolean withBanlist) throws IOException {
		BufferedWriter whitelistWriter = Files.newBufferedWriter(out);
		whitelistWriter.write("#whitelist converted from " + (deck.getSource() != null ? deck.getSource().toAbsolutePath() : "unknown")
				+ "\n!" + out.getFileName().toString().substring(0, out.getFileName().toString().length() - 5)
				+ "\n$whitelist\n");

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
