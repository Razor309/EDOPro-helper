package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.json.JsonObject;

import model.Options;
import model.YGOCard;
import model.YGODeck;

public class ConverterYGODeckToWhitelist {
	private static final String defaultWhiteListHeader = "#["
			+ Options.whitelistName.substring(0, Options.whitelistName.length() - 5) + "]\n!"
			+ Options.whitelistName.substring(0, Options.whitelistName.length() - 5) + "\n$whitelist\n";

	private ConverterYGODeckToWhitelist() {

	}

	public static void convert(YGODeck deck) throws IOException {
		convertTo(new File(Options.paths.getString("whitelist folder") + Options.whitelistName), deck,
				defaultWhiteListHeader);
	}

	public static void convertTo(File out, YGODeck deck, String header) throws IOException {
		JsonObject paths = Options.paths;

		BufferedWriter whitelistWriter = new BufferedWriter(new FileWriter(out));
		whitelistWriter.write(header);

		// insert whitelist header
		whitelistWriter.write("#whitelist converted from " + deck.getSource() + "\n");

		// insert whitelist
		// insert the main cards
		whitelistWriter.write(Options.mainDeckKeyword + "\n");
		for (YGOCard card : deck.getSetFor(Options.mainDeckKeyword)) {
			whitelistWriter.write(card.getCardID() + " " + deck.get(card) + " \n");
		}

		// insert the extra cards
		whitelistWriter.write(Options.extraDeckKeyword + "\n");
		for (YGOCard card : deck.getSetFor(Options.extraDeckKeyword)) {
			whitelistWriter.write(card.getCardID() + " " + deck.get(card) + " \n");
		}

		// insert the banlist
		whitelistWriter.write("#banlist from " + paths.getString("banlist") + "\n");
		BufferedReader banlistReader = new BufferedReader(new FileReader(paths.getString("banlist")));
		String line = banlistReader.readLine();
		while (line != null) {
			whitelistWriter.write(line + "\n");
			line = banlistReader.readLine();
		}
		banlistReader.close();
		whitelistWriter.close();
	}
}
