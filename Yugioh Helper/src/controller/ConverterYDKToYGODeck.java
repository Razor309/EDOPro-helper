package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import model.Options;
import model.YGOCard;
import model.YGODeck;

public class ConverterYDKToYGODeck {

	private ConverterYDKToYGODeck() {
	}

	public static YGODeck convert(File file) throws IOException {
		BufferedReader ydkReader = new BufferedReader(new FileReader(file));

		YGOCard currentCard = null;
		YGODeck deck = new YGODeck(file);

		String currentDeckType = Options.mainDeckKeyword;
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
				if (line.contains(Options.mainDeckKeyword)) {
					currentDeckType = Options.mainDeckKeyword;
				}
				if (line.contains(Options.extraDeckKeyword)) {
					currentDeckType = Options.extraDeckKeyword;
				}
				if (line.contains(Options.sideDeckKeyword)) {
					currentDeckType = Options.sideDeckKeyword;
				}
			}
			line = ydkReader.readLine();
		}

		ydkReader.close();
		return deck;
	}
}
