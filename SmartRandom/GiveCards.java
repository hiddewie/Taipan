package SmartRandom;

import Taipan.Card;
import Taipan.CardSet;

import java.util.TreeSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

class GiveCards {
	private TreeSet<Card> cards;

	public GiveCards(TreeSet<Card> hand) {
		cards = (TreeSet<Card>) hand.clone();
		System.out.println("Hand: " + cards);

	}

	public Card[] giveCards() {
		TreeSet<Card> keepCards = (TreeSet<Card>) cards.clone();
		Card[] gc = new Card[3];

		int bestRating = 0;
		for (Card c1 : cards) {
			keepCards.remove(c1);
			for (Card c2: cards) {
				if (c1.equals(c2)) {
					continue;
				}
				keepCards.remove(c2);
				for (Card c3: cards) {
					if (c3.equals(c2) || c3.equals(c1)) {
						continue;
					}
					keepCards.remove(c3);

					int rating = new CardsRater(keepCards).rateCards();
					if (rating > bestRating) {
						bestRating = rating;
						System.out.println("New best rating of " + rating + ": " + keepCards.toString());
						gc[0] = c1;
						gc[1] = c2;
						gc[2] = c3;
					}

					keepCards.add(c3);
				}
				keepCards.add(c2);
			}
			keepCards.add(c1);
		}

		System.out.println("Giving cards: " + Arrays.toString(gc));
		System.out.println();
		return gc;
	}
}
