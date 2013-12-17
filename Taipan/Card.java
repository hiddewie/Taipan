package Taipan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Card implements Comparable<Card> {
	public static int
			TWO = 2,
			THREE = 3,
			FOUR = 4,
			FIVE = 5,
			SIX = 6,
			SEVEN = 7,
			EIGHT = 8,
			NINE = 9,
			TEN = 10,
			JACK = 11,
			QUEEN = 12,
			KING = 13,
			ACE = 14,
			DOG = 0,
			MAHJONG = 1,
			PHOENIX = 15,
			DRAGON = 16;
	public static int
			SPECIAL = 0,
			BLUE = 1,
			GREY = 2,
			RED = 3,
			YELLOW = 4;
	public static char[]
			types = {' ', 'B', 'G', 'R', 'Y'},
			values = {'D', 'M', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A', 'P', 'G'};

	private int value, type;

	public Card(int value, int type) {
		this.value = value;
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public int getValue() {
		return value;
	}

	public int getPoints () {
		if (value == DRAGON) {
			return 25;
		} else if (value == PHOENIX) {
			return -25;
		} else if (value == FIVE) {
			return 5;
		} else if (value == KING || value == TEN) {
			return 10;
		}
		return 0;
	}

	public static ArrayList<Card> makeDeck() {
		ArrayList<Card> deck = new ArrayList<Card>(13 * 4 + 4);
		for (int v = Card.TWO; v <= Card.ACE; v++) {
			for (int t = Card.BLUE; t <= Card.YELLOW; t++) {
				deck.add(new Card(v, t));
			}
		}
		deck.add(new Card(Card.DRAGON, Card.SPECIAL));
		deck.add(new Card(Card.PHOENIX, Card.SPECIAL));
		deck.add(new Card(Card.DOG, Card.SPECIAL));
		deck.add(new Card(Card.MAHJONG, Card.SPECIAL));
		Collections.shuffle(deck, new Random(System.nanoTime()));
		return deck;
	}

	public int compareTo(Card other) {
		if (equals(other)) {
			return 0;
		}
		if (value > other.getValue()) {
			return 1;
		} else if (value < other.getValue()) {
			return -1;
		} else {
			return type - other.getType();
		}
	}

	public boolean equals(Object b) {
		if (!(b instanceof Card)) {
			return false;
		}
		Card c = (Card) b;
		return value == c.getValue() && type == c.getType();
	}

	public int hashCode() {
		return 100 * type + value;
	}

	public String toString() {
		return String.valueOf(values[value]);
	}
}
