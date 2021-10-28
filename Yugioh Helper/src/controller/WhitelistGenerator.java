package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import model.YGOCard;
import model.YGODeck;
import view.GraphicalConsole;

public class WhitelistGenerator {
	private static final String defaultWhiteListHeader = "#["
			+ OptionsHandler.options.whitelistName.substring(0, OptionsHandler.options.whitelistName.length() - 5)
			+ "]\n!"
			+ OptionsHandler.options.whitelistName.substring(0, OptionsHandler.options.whitelistName.length() - 5)
			+ "\n$whitelist\n";

	private WhitelistGenerator() {

	}

	public static void generateMain() throws IOException {
		generate(DeckHandler.importFrom(new File(OptionsHandler.options.paths.get("cards"))));
	}

	public static void generate(YGODeck deck) throws IOException {
		generateTo(
				new File(OptionsHandler.options.paths.get("whitelist folder") + OptionsHandler.options.whitelistName),
				deck, defaultWhiteListHeader);
	}

	public static void generateTo(File out, YGODeck deck, String header) throws IOException {
		HashMap<String, String> paths = OptionsHandler.options.paths;

		BufferedWriter whitelistWriter = new BufferedWriter(new FileWriter(out));
		whitelistWriter.write(header);

		// insert whitelist header
		whitelistWriter.write("#whitelist converted from " + deck.getSource() + "\n");

		// insert whitelist
		// insert the main cards
		whitelistWriter.write(YGODeck.MAIN + "\n");
		for (YGOCard card : deck.getSetFor(YGODeck.MAIN)) {
			whitelistWriter.write(card.getCardID() + " " + deck.get(card) + " \n");
		}

		// insert the extra cards
		whitelistWriter.write(YGODeck.EXTRA + "\n");
		for (YGOCard card : deck.getSetFor(YGODeck.EXTRA)) {
			whitelistWriter.write(card.getCardID() + " " + deck.get(card) + " \n");
		}

		// insert the banlist
		whitelistWriter.write("#banlist from " + paths.get("banlist") + "\n");
		BufferedReader banlistReader = new BufferedReader(new FileReader(paths.get("banlist")));
		String line = banlistReader.readLine();
		while (line != null) {
			whitelistWriter.write(line + "\n");
			line = banlistReader.readLine();
		}
		banlistReader.close();
		whitelistWriter.close();
		GraphicalConsole.add("- Generated whitelist: " + out);
	}
}
