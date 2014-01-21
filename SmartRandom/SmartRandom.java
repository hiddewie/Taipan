package SmartRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import Taipan.*;

public class SmartRandom extends Player {
	private CardSet lastPlayedCardSet = null;
	boolean greatTaipan = false, taipan = false;
	int[] cardCount = new int[17];

	public SmartRandom() {
		super();
		Arrays.fill(cardCount, 0);
	}

	public Card[] giveCards() {
		Card[] giving = new GiveCards(cards).giveCards();

		giving = distributeGiveCards(giving);

		System.out.println("I am giving cards: L: " + giving[1] + ", F: " + giving[2] + ", R: " + giving[0]);
		System.out.println();
		return giving;
	}

	public static Card[] distributeGiveCards(Card[] giving) {
		Card a = giving[0], b = giving[1], c = giving[2], temp;
		if (a.compareTo(b) > 0) {
			temp = b;
			b = a;
			a = temp;
		}
		if (b.compareTo(c) > 0) {
			temp = b;
			b = c;
			c = temp;
		}
		if (a.compareTo(b) > 0) {
			temp = b;
			b = a;
			a = temp;
		}

		// If we give a special card, give the even one to the right.
		if (a.getType() == Card.SPECIAL) {
			temp = a;
			a = b;
			b = c;
			c = temp;
			//giving = new Card[]{b, c, a};
		} else if (b.getType() == Card.SPECIAL) {
			temp = b;
			b = c;
			c = temp;
			//giving = new Card[]{a, c, b};
		}

		int firstVal = a.getValue(), secondVal = b.getValue();
		boolean firstEven = firstVal % 2 == 0;
		boolean secondEven = secondVal % 2 == 0;
		if (firstEven && secondEven) {
			giving = new Card[]{a, b, c};
		} else if (!firstEven && secondEven) {
			giving = new Card[]{b, a, c};
		} else if (firstEven && !secondEven) {
			giving = new Card[]{a, b, c};
		} else {
			giving = new Card[]{b, a, c};
		}
		// 0: right, 1: left, 2: front
		return giving;
	}

	private CardSet getCardsToPlay() throws IllegalMoveException {
		if (lastPlayedCardSet == null) {
			int gotPairs = -1;
			for (int i = 0; i < cardCount.length; i++) {
				if (cardCount[i] >= 2) {
					gotPairs = i;
					break;
				}
			}

			ArrayList<Card> cs = new ArrayList<Card>();
			if (gotPairs > -1) {
				Iterator<Card> it = cards.iterator();
				Card lastCard = it.next();
				while (it.hasNext()) {
					Card c = it.next();
					if (lastCard.getValue() == gotPairs) {
						cs.add(lastCard);
						cs.add(c);
						break;
					}
					lastCard = c;
				}
			} else {
				cs.add(cards.first());
			}
			return new CardSet(cs);
		}
		if (lastPlayedCardSet.getType() == CardSet.SINGLE) {
			int lastPlayedValue = lastPlayedCardSet.getCards().get(0).getValue();
			ArrayList<Card> cs = new ArrayList<Card>();
			for (Card c : cards) {
				if (c.getValue() > lastPlayedValue) {
					cs.add(c);
					break;
				}
			}
			if (cs.size() == 0) {
				return null;
			}
			return new CardSet(cs);
		} else if (lastPlayedCardSet.getType() == CardSet.DOUBLE) {
			int gotPairs = -1;
			for (int i = 0; i < cardCount.length; i++) {
				if (cardCount[i] >= 2 && i > lastPlayedCardSet.getCards().get(0).getValue()) {
					gotPairs = i;
					break;
				}
			}

			ArrayList<Card> cs = new ArrayList<Card>();
			if (gotPairs > -1) {
				Iterator<Card> it = cards.iterator();
				Card lastCard = it.next();
				while (it.hasNext()) {
					Card c = it.next();
					if (lastCard.getValue() == gotPairs) {
						cs.add(lastCard);
						cs.add(c);
						break;
					}
					lastCard = c;
				}
			} else {
				return null;
			}
			return new CardSet(cs);
		}
		return null;
	}

	public CardSet playCards() throws IllegalMoveException {
		CardSet cardsToPlay = getCardsToPlay();
		if (cardsToPlay != null) {
			for (Card c : cardsToPlay.getCards()) {
				cardCount[c.getValue()]--;
			}
		}
		return cardsToPlay;
	}

	public CardSet bomb() {
		return null;
	}

	public int getMahjongWish() {
		return Card.TWO;
	}

	public boolean informFirstDeal() {
		int handPoints = 0;
		for (Card c : cards) {
			if (c.getValue() == Card.ACE || c.getType() == Card.SPECIAL) {
				handPoints++;
			}
		}
		if (handPoints >= 3) {
			greatTaipan = true;
		}
		return greatTaipan;
	}

	public boolean informSecondDeal() {
		return false;
	}

	public void informCardsGiven() {
		Arrays.fill(cardCount, 0);
		for (Card c : cards) {
			cardCount[c.getValue()]++;
		}
	}

	public void informCardsPlayed(CardSet cardSetPlayed, int playerID) {
		lastPlayedCardSet = cardSetPlayed;
	}

	public void informTrickEnded() {
		lastPlayedCardSet = null;
	}

	public void informRoundEnded(int[] roundPoints) {

	}

	public void informMahjongWish(int mashjongWishValue) {

	}

	public int dragonToWhom() {
		return 1;
	}

	public void informTaipan(int playerID) {

	}

	public void informGreatTaipan(int playerID) {

	}
}
