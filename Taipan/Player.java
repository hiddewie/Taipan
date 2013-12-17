package Taipan;

import java.util.TreeSet;
import java.util.Collections;
import java.util.List;

public abstract class Player {
	protected TreeSet<Card> cards = new TreeSet<Card>();
	protected int id;
	private static int lastID = 0;
	protected RuleChecker ruleChecker = null;

	public Player() {
		id = lastID++;
	}

	public int getID() {
		return id;
	}
	public static void resetID() {
		lastID = 0;
	}

	public void giveCard(Card c) {
		cards.add(c);
	}

	public void giveCards(Card[] cards) {
		for (Card c : cards) {
			giveCard(c);
		}
	}

	public void giveCards(TreeSet<Card> cards) {
		for (Card c : cards) {
			giveCard(c);
		}
	}

	public void removeCard(Card c) {
		cards.remove(c);
	}

	public void removeCards(Card[] cards) {
		for (Card c : cards) {
			removeCard(c);
		}
	}

	public void removeCards(CardSet cards) {
		for (Card c : cards.getCards()) {
			removeCard(c);
		}
	}

	public int numberOfCards() {
		return cards.size();
	}

	public int getCardPoints() {
		int s = 0;
		for (Card c : cards) {
			s += c.getPoints();
		}
		return s;
	}

	public boolean hasCardValue(int value) {
		for (int i = Card.BLUE; i <= Card.YELLOW; i++) {
			if (hasCard(new Card(value, i))) {
				return true;
			}
		}
		return false;
	}

	public boolean hasCard(Card c) {
		return cards.contains(c);
	}

	public String toString() {
		String s = id + ": ";
		for (Card c : cards) {
			s += c.toString() + ", ";
		}
		return s;
	}

	public void setRuleChecker (RuleChecker rc) {
		ruleChecker = rc;
	}

	public abstract int getMahjongWish();

	public abstract boolean informFirstDeal();

	public abstract boolean informSecondDeal();

	public abstract void informTaipan(int playerID);

	public abstract void informGreatTaipan(int playerID);

	public abstract void informCardsGiven();

	public abstract void informCardsPlayed(CardSet cardSetPlayed, int playerID);

	public abstract void informTrickEnded();

	public abstract void informRoundEnded(int[] roundPoints);

	public abstract void informMahjongWish(int mahjongWish);

	public abstract Card[] giveCards();

	public abstract CardSet playCards() throws IllegalMoveException;

	public abstract CardSet bomb();

	public abstract int dragonToWhom();
}
