package model;

public class YGOCard {
	private Integer cardID;
	private String deckType;

	public Integer getCardID() {
		return cardID;
	}

	public String getDeckType() {
		return deckType;
	}

	public YGOCard(int cardID, String deckType) {
		this.cardID = cardID;
		this.deckType = deckType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cardID == null) ? 0 : cardID.hashCode());
		result = prime * result + ((deckType == null) ? 0 : deckType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		YGOCard other = (YGOCard) obj;
		if (cardID == null) {
			if (other.cardID != null)
				return false;
		} else if (!cardID.equals(other.cardID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + this.deckType + ") " + this.cardID;
	}
}
