package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import model.YGODeck;
import view.GraphicalConsole;

public class WhitelistGenerator {
	private WhitelistGenerator() {

	}

	public static void generateGeneral() throws IOException {
		generateTo(
				Paths.get(OptionsHandler.options.paths.get("whitelist folder") + OptionsHandler.options.generalWhitelistName),
				DeckHandler.getAllcardsDeck());
	}

	public static void generateGoodcards() throws IOException {
		generateTo(
				Paths.get(OptionsHandler.options.paths.get("whitelist folder") + OptionsHandler.options.goodcardsWhitelistName),
				DeckHandler.getGoodcardsDeck());
	}

	public static void generateTrimmedGoodcards() throws IOException {
		generateTo(
				Paths.get(OptionsHandler.options.paths.get("whitelist folder") + OptionsHandler.options.trimmedGoodcardsWhitelistName),
				DeckHandler.getIntersectingDeck(DeckHandler.getGoodcardsDeck(), DeckHandler.getAllcardsDeck()));
	}

	public static void generateTo(Path out, YGODeck deck) throws IOException {
		BufferedWriter whitelistWriter = Files.newBufferedWriter(out);
		whitelistWriter.write("#whitelist converted from " + (deck.getSource() != null ? deck.getSource().toAbsolutePath() : "unknown")
				+ "\n!" + out.getFileName().toString().substring(0, out.getFileName().toString().length() - 5)
				+ "\n$whitelist\n");

		// insert whitelist
		// insert the cards
		for (Integer key : deck.keySet()) {
			whitelistWriter.write("" + key + " " + (deck.get(key) >= 3 ? 3 : deck.get(key)) + "\n");
		}

		GraphicalConsole.add("- Generated whitelist: " + out);
		whitelistWriter.close();
	}
}
