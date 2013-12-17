package Taipan;

import SmartRandom.SmartRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Game<P1 extends Player, P2 extends Player, P3 extends Player, P4 extends Player> {
	private ArrayList<Player> players = new ArrayList<Player>();
	private int[] gamePoints = {0, 0};

	public Game(Class<P1> p1, Class<P2> p2, Class<P3> p3, Class<P4> p4) throws InstantiationException,
			IllegalAccessException {
		Player.resetID();
		players.add(p1.newInstance());
		players.add(p2.newInstance());
		players.add(p3.newInstance());
		players.add(p4.newInstance());
	}

	public void play() {
		System.out.println("Starting the game");
		long startTime = System.nanoTime();
		try {
			round();
		} catch (IllegalMoveException e) {
			e.printStackTrace();
			System.out.println("Game ended with an illegal move: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Game ended with an error: " + e.getMessage());
		}
		System.out.println("Game ended in " + (System.nanoTime() - startTime) / 1000000.0 + " ms");
	}

	public void round() throws IllegalMoveException {
		RuleChecker rc = new RuleChecker();
		for (Player p: players) {
			p.setRuleChecker(rc);
		}
		dealCards(rc);
		giveCards(rc);

		return;

		/*rc.setStartingPlayer(getMahjongPlayer());

		while (rc.roundKeepPlaying()) {
			trick(rc);
		}
		rc.roundEnded();

		int[] roundPoints = rc.getGamePoints();
		for (Player p : players) {
			p.informRoundEnded(roundPoints);
		}
		gamePoints[0] += roundPoints[0];
		gamePoints[1] += roundPoints[1];
		System.out.println("Game points: " + Arrays.toString(gamePoints));*/
	}

	public void trick(RuleChecker rc) throws IllegalMoveException {
		boolean noCardsPlayed = true;
		boolean lastCardDragon = true;
		int currentPlayerIndex = -1;
		while (rc.trickKeepPlaying() /* TODO */) {
			currentPlayerIndex = rc.getCurrentPlayerIndex();
			Player curPlayer = players.get(currentPlayerIndex);
			CardSet playedCards = curPlayer.playCards();

			if (playedCards != null) {
				curPlayer.removeCards(playedCards);
				rc.cardsPlayed(playedCards, curPlayer);
				System.out.println(currentPlayerIndex + ": " + playedCards);
				for (Player p : players) {
					p.informCardsPlayed(playedCards, currentPlayerIndex);
				}
				// Mahjong played
				if (playedCards.cardInCardSet(new Card(Card.MAHJONG, Card.SPECIAL))) {
					int mahjongWish = curPlayer.getMahjongWish();
					rc.mahjongWish(mahjongWish);
					for (Player p : players) {
						p.informMahjongWish(mahjongWish);
					}
				}
				if (playedCards.cardInCardSet(new Card(Card.DRAGON, Card.SPECIAL))) {
					lastCardDragon = true;
				} else {
					lastCardDragon = false;
				}

				noCardsPlayed = false;
				if (playedCards.cardInCardSet(new Card(Card.DOG, Card.SPECIAL))) {
					rc.nextPlayer();
				}
			} else {
				if (noCardsPlayed) {
					throw new IllegalMoveException();
				}
				rc.pass();
			}

			rc.nextPlayingPlayer();
		}
		currentPlayerIndex = rc.getCurrentPlayerIndex();
		if (lastCardDragon) {
			rc.dragonTrickToPlayer((currentPlayerIndex + players.get(currentPlayerIndex).dragonToWhom() + 4) % 4);
		}

		for (Player p : players) {
			p.informTrickEnded();
		}

		System.out.println("Trick ended with player " + currentPlayerIndex + " and " + rc.getTrickPoints() + " " +
				"points");
		rc.trickEnded(currentPlayerIndex);
	}

	private void dealCards(RuleChecker rc) throws IllegalMoveException{
		ArrayList<Card> deck = Card.makeDeck();
		for (int i = 0; i < 8 * 4; i++) {
			players.get(i % 4).giveCard(deck.get(i));
		}
		for (Player p : players) {
			boolean greatTaipan = p.informFirstDeal();
			if (greatTaipan) {
				rc.greatTaipan(p.getID());
				for (Player q : players) {
					q.informGreatTaipan(p.getID());
				}
			}
		}
		for (int i = 8 * 4; i < 14 * 4; i++) {
			players.get(i % 4).giveCard(deck.get(i));
		}
		for (Player p : players) {
			boolean taipan = p.informSecondDeal();
			if (taipan) {
				rc.taipan(p.getID());
				for (Player q : players) {
					q.informTaipan(p.getID());
				}
			}
		}
	}

	private void giveCards(RuleChecker rc) {
		Card[][] giveCards = new Card[4][];
		for (Player p : players) {
			giveCards[p.getID()] = p.giveCards();
			p.removeCards(giveCards[p.getID()]);
		}
		for (Player p : players) {
			// TODO: give to the right players
			Card[] cardsToGive = giveCards[(p.getID() + 4 - 1) % 4];
			p.giveCards(cardsToGive);
		}
		for (Player p : players) {
			p.informCardsGiven();
		}
		/*for (Player p : players) {
			System.out.println(p);
		}*/
	}

	private int getMahjongPlayer() throws IllegalMoveException {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).hasCard(new Card(Card.MAHJONG, Card.SPECIAL))) {
				return i;
			}
		}
		throw new IllegalMoveException("No mahjong player");
	}
}
