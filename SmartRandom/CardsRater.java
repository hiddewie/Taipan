package SmartRandom;

import Taipan.Card;
import Taipan.CardSet;
import Taipan.IllegalMoveException;
import Taipan.Player;

import java.util.*;

public class CardsRater {
	static HashMap<TreeSet<Card>, Integer> save = new HashMap<TreeSet<Card>, Integer>();

	private CardsRater() {
	}

	public static int rateCards(TreeSet<Card> cards) {
		if (cards.size() == 0) {
			return 0;
		} else if (cards.size() == 1) {
			return cards.first().getRatingValue();
		}

		if (save.containsKey(cards)) {
			return save.get(cards);
		}

		int bestRating = 1;
		try {
			int rating;
			TreeSet<Card> working = (TreeSet<Card>) cards.clone();

			/* TODO: needs optimizing, a lot of double counting! */
			int val;
			ArrayList<Card>[] numCards = new ArrayList[Card.DRAGON + 1];
			for (int i = Card.DOG; i <= Card.DRAGON; i++) {
				numCards[i] = new ArrayList<Card>(4);
			}
			for (Card c : cards) {
				val = c.getValue();
				numCards[val].add(c);
			}

			for (int i = Card.TWO; i <= Card.ACE; i++) {
				ArrayList<Card> temp;
				for (int j = 2; j <= 4; j++) {
					if (numCards[i].size() >= j) {
						temp = new ArrayList<Card>(numCards[i].subList(0, j));
						rating = new CardSet(temp).getRating() + getRatingWithout(numCards[i], working);
						if (rating > bestRating) {
							bestRating = rating;
						}
					}
				}
			}

			for (int begin = Card.MAHJONG; begin <= Card.ACE - 5; begin++) {
				boolean phoenixInHand = numCards[Card.PHOENIX].size() > 0;
				ArrayList<Card> longestStreet = new ArrayList<Card>(12), currentStreet = new ArrayList<Card>(12);
				boolean phoenixAvailable = phoenixInHand;

				for (int i = begin; i <= Card.ACE; i++) {
					if (numCards[i].size() > 0) {
						currentStreet.add(numCards[i].get(0));
					} else {
						if (phoenixAvailable && i >= Card.TWO && i <= Card.ACE) {
							currentStreet.add(numCards[Card.PHOENIX].get(0));
							phoenixAvailable = false;
						} else {
							if (currentStreet.size() > longestStreet.size()) {
								longestStreet = (ArrayList<Card>) currentStreet.clone();
							}
							currentStreet.clear();
							phoenixAvailable = phoenixInHand;
						}
					}
				}
				while (longestStreet.size() >= 5) {
					// TODO: Add removing cards from end.
					//System.out.println("new street " + longestStreet);
					//System.out.flush();
					try {
						rating = new CardSet(longestStreet).getRating() + getRatingWithout(longestStreet, working);

						if (rating > bestRating) {
							bestRating = rating;
						}
					} catch (IllegalMoveException e) {
						// TODO: solve illegal card sets.
					}

					longestStreet.remove(longestStreet.size() - 1);

				}
			}
		} catch (IllegalMoveException e) {
			e.printStackTrace();
		}

		save.put(cards, bestRating);
		return bestRating;
	}

	private static int getRatingWithout(ArrayList<Card> without, TreeSet<Card> working) {
		for (Card c : without) {
			working.remove(c);
		}
		int rating = new CardsRater().rateCards(working);
		for (Card c : without) {
			working.add(c);
		}
		return rating;
	}
}
