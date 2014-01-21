package SmartRandom;

import Taipan.Card;
import Taipan.CardSet;
import Taipan.IllegalMoveException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class CardsRater {
	private TreeSet<Card> cards;

	public CardsRater(TreeSet<Card> cards) {
		this.cards = cards;
	}

	public int rateCards() {
		if (cards.size() == 0) {
			return 0;
		} else if (cards.size() == 1) {
			return cards.first().getValue();
		}

		/*
		for (Card c1 : cardsAL) {
			working.remove(c1);
			for (Card c2 : cardsAL) {
				if (c1.equals(c2)) {
					continue;
				}
				working.remove(c2);
				for (Card c3 : cardsAL) {
					if (c3.equals(c2) || c3.equals(c1)) {
						continue;
					}
					working.remove(c3);


					System.out.println(c1 + " " + c2 + " " + c3);


					working.add(c3);
				}
				working.add(c2);
			}
			working.add(c1);
		}
*/


		int bestRating = 1, rating;
		int[] numCards = new int[Card.DRAGON + 1];
		Card last3Card = null, last2Card = null, lastCard = null;
		ArrayList<Card> progress = new ArrayList<Card>(4);


		boolean findBomb = false, findTriple = false, findPair = false;
		int val;
		for (Card c : cards) {
			val = c.getValue();
			numCards[val]++;
			if (numCards[val] == 2) {
				findPair = true;
			} else if (numCards[val] == 3) {
				findTriple = true;
			} else if (numCards[val] == 4) {
				findBomb = true;
			}
		}

		try {
			TreeSet<Card> cardsAL = (TreeSet<Card>) cards.clone();
			TreeSet<Card> working = new TreeSet<Card>();
			for (Card c : cards) {
				cardsAL.add(c);
				working.add(c);
			}

			for(Card c:cardsAL) {
			//Iterator<Card> it = cards.iterator();
			//while(it.hasNext()) {
			//	Card c = it.next();
				progress.clear();
				if (findBomb) {
					if (last3Card != null && last2Card != null && lastCard != null) {
						if (last3Card.getValue() == last2Card.getValue() && last2Card.getValue() == lastCard.getValue() &&
								lastCard.getValue() == c.getValue()) {
							progress.add(last3Card);
							progress.add(last2Card);
							progress.add(lastCard);
							progress.add(c);
							working.remove(last3Card);
							working.remove(last2Card);
							working.remove(lastCard);
							working.remove(c);
							rating = rateCardSet(new CardSet(progress)) + new CardsRater(working).rateCards();
							if (rating > bestRating) {
								bestRating = rating;
							}
							working.add(last3Card);
							working.add(last2Card);
							working.add(lastCard);
							working.add(c);
						}
					}
				} else if (findTriple) {
					if (last2Card != null && lastCard != null) {
						if (last2Card.getValue() == lastCard.getValue() && lastCard.getValue() == c.getValue()) {
							progress.add(last2Card);
							progress.add(lastCard);
							progress.add(c);
							working.remove(last2Card);
							working.remove(lastCard);
							working.remove(c);
							rating = rateCardSet(new CardSet(progress)) + new CardsRater(working).rateCards();
							if (rating > bestRating) {
								bestRating = rating;
							}
							working.add(last2Card);
							working.add(lastCard);
							working.add(c);
						}
					}
				} else if (findPair) {
					if (lastCard != null) {
						if (lastCard.getValue() == c.getValue()) {
							progress.add(lastCard);
							progress.add(c);
							working.remove(lastCard);
							working.remove(c);
							rating = rateCardSet(new CardSet(progress)) + new CardsRater(working).rateCards();
							if (rating > bestRating) {
								bestRating = rating;
							}
							working.add(lastCard);
							working.add(c);
						}
					}
				}

				last3Card = last2Card;
				last2Card = lastCard;
				lastCard = c;
			}
		} catch (IllegalMoveException e) {
			e.printStackTrace();
		}

		//System.out.println("Best rating for hand " + cards + " is " + bestRating);

		return bestRating;
	}

	public int rateCardSet(CardSet cardSet) {
		int rating = 0, size = cardSet.getCards().size();
		for (Card c : cardSet.getCards()) {
			rating += c.getValue();
		}
		return rating * size * size;
	}
}
