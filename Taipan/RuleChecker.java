package Taipan;

import java.util.ArrayList;

public class RuleChecker {
	private static int NULL_CARDSET_TYPE = -100, NULL_CARD_VALUE = -100, NULL_PLAYER_VALUE = -1;
	private ArrayList<CardSet> playedCards = new ArrayList<CardSet>();
	private int mahjongPlayerID = NULL_PLAYER_VALUE;
	private boolean trickStarted = false;
	private boolean roundStarted = false;
	private int mahjongWish = NULL_CARD_VALUE;
	private int trickType = NULL_CARDSET_TYPE;
	private CardSet lastPlayed = null;
	private PointChecker pointChecker = new PointChecker();

	public void setStartingPlayer(int startingPlayer) {
		pointChecker.setStartingPlayer(startingPlayer);
	}

	public void mahjongWish(int mahjongWish) {
		this.mahjongWish = mahjongWish;
	}

	public void cardsPlayed(CardSet cards, Player player) throws IllegalMoveException {
		if (!trickStarted) {
			trickType = cards.getType();
			trickStarted = true;
		}
		if ((trickType != CardSet.STREETBOMB && cards.getType() == CardSet.BOMB)
				|| cards.getType() == CardSet.STREETBOMB) {
			trickType = cards.getType();
		}
		if (cards.getType() != trickType) {
			throw new IllegalMoveException();
		}
		if (lastPlayed != null && cards.compareTo(lastPlayed) < 0) {
			throw new IllegalMoveException();
		}
		if (mahjongWish != NULL_CARD_VALUE) {
			if (player.hasCardValue(mahjongWish)) {
				//TODO: throw new IllegalMoveException();
			} else {

			}
		}

		pointChecker.cardsPlayed(cards, player);

		lastPlayed = cards;
	}

	public void trickEnded(int trickWinner) {
		trickType = 0;
		trickStarted = false;
		lastPlayed = null;

		pointChecker.trickEnded(trickWinner);
	}

	public void roundEnded() {
		roundStarted = false;
		pointChecker.roundEnded();
	}

	public void dragonTrickToPlayer(int playerID) {
		pointChecker.dragonTrickToPlayer(playerID);
	}

	public int[] getRoundPoints() {
		return pointChecker.getRoundPoints();
	}

	public boolean roundKeepPlaying() {
		return pointChecker.roundKeepPlaying();
	}

	public boolean trickKeepPlaying() {
		return pointChecker.trickKeepPlaying();
	}

	public int nextPlayer() {
		return pointChecker.nextPlayer();
	}

	public int nextPlayingPlayer() {
		return pointChecker.nextPlayingPlayer();
	}

	public void pass() {
		pointChecker.pass();
	}

	/**
	 * @return The current player index
	 */
	public int getCurrentPlayerIndex() {
		return pointChecker.getCurrentPlayerIndex();
	}

	public int getTrickPoints() {
		return pointChecker.getTrickPoints();
	}

	public int[] getGamePoints() {
		return pointChecker.getGamePoints();
	}

	public void greatTaipan(int playerID) throws IllegalMoveException {
		System.out.println("Player " + playerID + " great-taipans");
		pointChecker.greatTaipan(playerID);
	}

	public void taipan(int playerID) throws IllegalMoveException {
		System.out.println("Player " + playerID + " taipans");
		pointChecker.taipan(playerID);
	}
}
