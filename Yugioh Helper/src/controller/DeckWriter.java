package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import model.Options;
import model.YGOCard;
import model.YGODeck;

public class DeckWriter {
	private DeckWriter() {

	}

	public static void writeCards(YGODeck deck) throws IOException {
		writeLimitedTo(new File(Options.paths.getString("cards")), deck, 3);
	}

	public static void writeLimitedTo(File out, YGODeck deck, int limit) throws IOException {
		BufferedWriter deckWriter = new BufferedWriter(new FileWriter(out));

		deckWriter.write(Options.mainDeckKeyword + "\n");
		for (YGOCard card : deck.getSetFor(Options.mainDeckKeyword)) {
			for (int i = 0; i < deck.get(card) && i < limit; i++)
				deckWriter.write(card.getCardID() + "\n");
		}

		deckWriter.write(Options.extraDeckKeyword + "\n");
		for (YGOCard card : deck.getSetFor(Options.extraDeckKeyword)) {
			for (int i = 0; i < deck.get(card) && i < limit; i++)
				deckWriter.write(card.getCardID() + "\n");
		}
		deckWriter.write(Options.sideDeckKeyword + "\n");
		for (YGOCard card : deck.getSetFor(Options.sideDeckKeyword)) {
			for (int i = 0; i < deck.get(card) && i < limit; i++)
				deckWriter.write(card.getCardID() + "\n");
		}
		deckWriter.close();
	}

}
