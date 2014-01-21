import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

import SmartRandom.SmartRandom;
import SmartRandom.CardsRater;
import Taipan.*;

class Main {
	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		//startGame();
		test();
	}

	private void startGame () {
		try {
			new Game<SmartRandom, SmartRandom, SmartRandom, SmartRandom>(SmartRandom.class, SmartRandom.class,
					SmartRandom.class, SmartRandom.class).play();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	void test() {
		// TODO: [D, M, 2, 3, 5, 5, 8, 9, T, J, A]

		TreeSet<Card> t = new TreeSet<Card>();
		int J = Card.JACK, Q = Card.QUEEN, K = Card.KING, A = Card.ACE, P = Card.PHOENIX, D = Card.DOG,
				G = Card.DRAGON, M = Card.MAHJONG, T = Card.TEN;
		int[] nums = new int[] {M, 3, 4, 5, 7, 7, 8, 9, T, T, Q, Q, A, P};

		int[] colors = new int[Card.DRAGON + 1];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = Card.BLUE;
		}
		for (int i: nums) {
			if (i == G || i == D || i == P || i == M) {
				t.add(new Card(i, Card.SPECIAL));
			} else {
				t.add(new Card(i, colors[i]));
				colors[i] ++;
			}
		}

		SmartRandom sr = new SmartRandom();
		for(Card c: t) {
			sr.giveCard(c);
		}
		System.out.println(t + ", " + Arrays.toString(sr.giveCards()));
	}
}

