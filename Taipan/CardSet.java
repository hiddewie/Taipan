package Taipan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class CardSet implements Comparable<CardSet> {
	public static int
			SINGLE = 1,
			DOUBLE = 2,
			TRIPLE = 3,
			DOUBLESEQ = 4,
			FULLHOUSE = 5,
			STREET = 6,
			BOMB = 7,
			STREETBOMB = 8;
	public static String[] types = {
			"",
			"Single",
			"Double",
			"Triple",
			"DoubleSeq",
			"FullHouse",
			"Street",
			"Bomb",
			"StreetBomb",
	};

	private int type;
	private boolean pheonixInCardSet = false;
	private int param = -1, param2 = -1;
	int[] count = new int[Card.DRAGON + 1];
	private ArrayList<Card> cards;

	public CardSet(ArrayList<Card> cards) throws IllegalMoveException {
		Collections.sort(cards);
		this.cards = cards;
		Arrays.fill(count, 0);
		for (Card c : cards) {
			count[c.getValue()]++;
		}
		pheonixInCardSet = cardInCardSet(new Card(Card.PHOENIX, Card.SPECIAL));
		type = getType(cards);
	}

	private int getType(ArrayList<Card> cards) throws IllegalMoveException {
		int n = cards.size();
		if (n == 0) {
			throw new IllegalMoveException();
		} else if (n == 1) {
			return SINGLE;
		} else if (n > 1) {
			if (cards.get(0).getValue() == Card.MAHJONG) {
				if (!pheonixInCardSet) {
					int k = 0;
					for (int i = 1; i < n - 1; i++) {
						if (cards.get(i).getValue() + 1 == cards.get(i + 1).getValue()) {
							k++;
						}
					}
					if (k == n - 2) {
						return STREET;
					}
				} else {
					int k = 0;
					int v = 0;
					for (int i = 1; i < n - 1; i++) {
						if (cards.get(i).getValue() + 1 == cards.get(i + 1).getValue()) {
							k++;
						}
						if (cards.get(i).getValue() + 2 == cards.get(i + 1).getValue()) {
							v++;
						}
					}
					if (k == n - 3 || (k == n - 4 && v == 1)) {
						return STREET;
					}
				}
			}
			if (cardValueInSet(new Card(Card.DRAGON, Card.SPECIAL))
					|| cardValueInSet(new Card(Card.DOG, Card.SPECIAL))
					|| cardValueInSet(new Card(Card.MAHJONG, Card.SPECIAL))) {
				throw new IllegalMoveException();
			}

			if (n == 2) {
				if (pheonixInCardSet || cards.get(0).getValue() == cards.get(1).getValue()) {
					return DOUBLE;
				}
				throw new IllegalMoveException();
			} else if (n == 3) {
				int v = cards.get(0).getValue();
				if ((pheonixInCardSet && count[v] == 2)
						|| (!pheonixInCardSet && count[v] == 3)) {
					return TRIPLE;
				}
				throw new IllegalMoveException();
			} else if (n == 4) {
				if (count[cards.get(0).getValue()] == 4) {
					return BOMB;
				}
			} else if (n == 5) {
				if (pheonixInCardSet) {
					int twoFound = 0, threeFound = 0, highCard = -1, lowCard = -1;
					for (int i = 0; i < count.length; i++) {
						if (count[i] == 3) {
							threeFound++;
							lowCard = highCard;
							highCard = i;
						} else if (count[i] == 2) {
							twoFound++;
							if (threeFound == 0 || i > highCard) {
								lowCard = highCard;
								highCard = i;
							} else {
								lowCard = i;
							}
						}
					}
					if (twoFound == 2 || threeFound == 1) {
						param = highCard;
						param2 = lowCard;
						return FULLHOUSE;
					}
				} else if (!pheonixInCardSet) {
					int twoFound = 0, threeFound = 0, highCard = -1, lowCard = -1;
					for (int i = 0; i < count.length; i++) {
						if (count[i] == 3) {
							threeFound++;
							highCard = i;
						} else if (count[i] == 2) {
							twoFound++;
							lowCard = i;
						}
					}
					if (twoFound == 1 && threeFound == 1) {
						param = highCard;
						param2 = lowCard;
						return FULLHOUSE;
					}
				}

				int k = 0;
				for (int i = 0; i < n; i++) {
					if (count[cards.get(i).getValue()] > 1) {
						throw new IllegalMoveException();
					} else if (count[cards.get(i).getValue()] == 1) {
						k++;
					}
				}
				if (pheonixInCardSet && k == 5
						&& (cards.get(0).getValue() + 3 == cards.get(3).getValue()
						|| cards.get(0).getValue() + 4 == cards.get(3).getValue())) {
					return STREET;
				} else if (k == 5 && cards.get(0).getValue() + 4 == cards.get(4).getValue()) {
					if (cards.get(0).getType() == cards.get(1).getType()
							&& cards.get(1).getType() == cards.get(2).getType()
							&& cards.get(2).getType() == cards.get(3).getType()
							&& cards.get(3).getType() == cards.get(4).getType()) {
						return STREETBOMB;
					} else {
						return STREET;
					}
				}
				throw new IllegalMoveException();
			} else if (n % 2 == 0) {
				if (!pheonixInCardSet) {
					int k = 0;
					for (int i = 0; i < n; i++) {
						if (count[cards.get(i).getValue()] == 2) {
							k++;
						}
					}
					if (k == n) {
						param = n / 2;
						return DOUBLESEQ;
					}
				} else {
					int k = 0;
					for (int i = 0; i < n - 1; i++) {
						if (cards.get(i).getValue() == cards.get(i + 1).getValue()) {
							k += 1;
						}
					}
					if (k == n / 2 - 1) {
						param = n / 2;
						return DOUBLESEQ;
					}
				}
			}
			if (n > 5) {
				if (pheonixInCardSet) {
					if (cards.get(0).getValue() + (n - 1) != cards.get(n - 2).getValue()
							&& cards.get(0).getValue() + (n - 2) != cards.get(n - 2).getValue()) {
						throw new IllegalMoveException();
					}
					int k = 0;
					int v;
					for (int i = 1; i < n - 1; i++) {
						v = cards.get(i).getValue() - cards.get(i - 1).getValue();
						if (v == 2) {
							k++;
						}
						if (v < 1 || v > 2) {
							throw new IllegalMoveException();
						}
					}
					if (k >= 2) {
						throw new IllegalMoveException();
					}
					return STREET;
				} else {
					if (cards.get(0).getValue() + (n - 1) != cards.get(n - 1).getValue()) {
						throw new IllegalMoveException();
					}
					boolean sameTypeSoFar = true;
					for (int i = 1; i < n; i++) {
						if (cards.get(i).getValue() - cards.get(i - 1).getValue() != 1) {
							throw new IllegalMoveException();
						}
						if (cards.get(i).getType() != cards.get(i - 1).getType()) {
							sameTypeSoFar = false;
						}
					}
					if (sameTypeSoFar) {
						return STREETBOMB;
					} else {
						return STREET;
					}
				}
			}
		}
		throw new IllegalMoveException();
	}

	public boolean cardValueInSet(Card d) {
		return count[d.getValue()] > 0;
	}

	public boolean cardInCardSet(Card d) {
		for (Card c : cards) {
			if (c.equals(d)) {
				return true;
			}
		}
		return false;
	}

	public int getType() {
		return type;
	}

	public boolean hasCard(Card c) {
		return cards.contains(c);
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public int getPoints() {
		int s = 0;
		for (Card c : cards) {
			s += c.getPoints();
		}
		return s;
	}

	public int getRating() {
		int rating = 0, size = cards.size();
		for (Card c : cards) {
			rating += c.getRatingValue();
		}
		if (type == BOMB) {
			rating *= 2;
		}
		if (type == STREET) {
			rating *= 2;
		}
		//System.out.println(cards + ": " + rating * size);
		return rating * /*size * */size;
	}

	public int compareTo(CardSet other) {
		if (type != other.type) {
			return other.type > type ? -1 : 1;
		} else if (type == SINGLE || type == DOUBLE || type == TRIPLE || type == BOMB) {
			return cards.get(0).compareTo(other.cards.get(0));
		} else if (type == FULLHOUSE) {
			if (param == other.param) {
				return param2 > other.param2 ? 1 : -1;
			} else {
				return param > other.param ? 1 : -1;
			}
		} else if (type == STREETBOMB) {
			if (other.cards.size() != cards.size()) {
				return other.cards.size() > cards.size() ? -1 : 1;
			} else {
				return cards.get(0).compareTo(other.cards.get(0));
			}
		} else if (type == DOUBLESEQ) {
			if (param == other.param) {
				return cards.get(0).compareTo(other.cards.get(0));
			} else {
				return param > other.param ? 1 : -1;
			}
		}
		return -1;
	}

	public String toString() {
		return types[type] + " " + cards.toString();
	}
}
