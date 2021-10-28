package model;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class YGODeck extends HashMap<YGOCard, Integer> {
	private File source;
	public static final String MAIN = "#main", EXTRA = "#extra", SIDE = "!side";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public YGODeck(File source) {
		super();
		this.source = source;
	}

	public YGODeck() {
		this(null);
	}

	public Set<YGOCard> getSetFor(String keyword) {
		Set<YGOCard> cardSet = new HashSet<YGOCard>();
		for (YGOCard card : this.keySet()) {
			if (card.getDeckType() == keyword)
				cardSet.add(card);
		}
		return cardSet;
	}

	@Override
	public String toString() {
		String str = "";
		Set<YGOCard> keySet = this.keySet();
		for (YGOCard card : keySet) {
			str += card + "[" + this.get(card) + "]\n";
		}
		return str;
	}

	public File getSource() {
		return source;
	}
}
