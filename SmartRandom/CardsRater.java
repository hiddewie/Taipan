package SmartRandom;

import Taipan.Card;
import Taipan.CardSet;

import java.util.TreeSet;

public class CardsRater {
	private TreeSet<Card> cards;
	public CardsRater(TreeSet<Card> cards) {
		this.cards = cards;
	}

	public int rateCards () {
		if (cards.size() == 0) {
			return 0;
		} else if (cards.size() == 1) {
			return cards.first().getValue();
		}

		int bestRating = 1;
		//for (int i = 0; i < )
		return bestRating;
	}

	public int rateCardSet(CardSet cardSet) {
		int rating = 0, size = cardSet.getCards().size();
		for (Card c: cardSet.getCards()) {
			rating += c.getValue();
		}
		return rating * size * size;
	}
}
