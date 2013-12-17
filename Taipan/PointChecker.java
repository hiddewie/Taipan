package Taipan;

import java.util.ArrayList;

public class PointChecker {
	private int[] gamePoints = {0, 0};
	private int[] roundPoints = {0, 0, 0, 0};
	boolean[] taipan = {false, false, false, false};
	boolean[] greatTaipan = {false, false, false, false};
	boolean[] hasPlayedCards = {false, false, false, false};
	private int trickPoints = 0;
	private int dragonTrickPlayerID = -1;
	int playingPlayers = 4;
	boolean[] playerIsPlaying = {true, true, true, true};
	int passes = 0;
	int currentPlayer = 0;
	ArrayList<Integer> donePlayers = new ArrayList<Integer>();

	public void setStartingPlayer(int startingPlayer) {
		currentPlayer = startingPlayer;
	}

	public int[] getGamePoints() {
		return gamePoints;
	}

	public int[] getRoundPoints() {
		return roundPoints;
	}

	public int getTrickPoints() {
		return trickPoints;
	}

	public void dragonTrickToPlayer(int playerID) {
		dragonTrickPlayerID = playerID;
	}

	public void trickEnded(int endingPlayerID) {
		if (dragonTrickPlayerID != -1) {
			roundPoints[dragonTrickPlayerID] += trickPoints;
		} else {
			roundPoints[endingPlayerID] += trickPoints;
		}
		trickPoints = 0;
		dragonTrickPlayerID = -1;

		passes = 0;
	}

	public void roundEnded() {
		if ((donePlayers.get(0) + donePlayers.get(1)) % 2 == 0) { // Same team
			gamePoints[donePlayers.get(0) % 2] += 200;
		}
	}

	public void cardsPlayed(CardSet cards, Player player) {
		hasPlayedCards[player.getID()] = true;
		trickPoints += cards.getPoints();
		passes = 0;
		if (player.numberOfCards() == 0) {
			int playerID = player.getID();
			System.out.println(playerID + " is done");
			if (donePlayers.size() == 0) {
				if (greatTaipan[playerID]) {
					System.out.println("Player " + playerID + " succeeded a great taipan");
					gamePoints[playerID % 2] += 200;
				} else if (taipan[playerID]) {
					System.out.println("Player " + playerID + " succeeded a taipan");
					gamePoints[playerID % 2] += 100;
				}
			} else {
				if (greatTaipan[playerID]) {
					System.out.println("Player " + playerID + " failed a great taipan");
					gamePoints[playerID % 2] -= 200;
				} else if (taipan[playerID]) {
					System.out.println("Player " + playerID + " failed a taipan");
					gamePoints[playerID % 2] -= 100;
				}
			}
			donePlayers.add(new Integer(playerID));
			playingPlayers--;
			playerIsPlaying[playerID] = false;
		}
	}

	public void pass() {
		passes++;
	}

	public int nextPlayer() {
		currentPlayer = (currentPlayer + 1) % 4;
		return currentPlayer;
	}

	public int nextPlayingPlayer() {
		do {
			nextPlayer();
		} while (!playerIsPlaying[currentPlayer]);
		return currentPlayer;
	}

	public boolean roundKeepPlaying() {
		return playingPlayers > 1 && playingPlayers > 1 &&
				(playerIsPlaying[0] || playerIsPlaying[2])
				&& (playerIsPlaying[1] || playerIsPlaying[3]);
	}

	public boolean trickKeepPlaying() {
		return passes < playingPlayers - 1 && playingPlayers > 1 &&
				((playerIsPlaying[0] || playerIsPlaying[2])
						&& (playerIsPlaying[1] || playerIsPlaying[3]));
	}

	public int getCurrentPlayerIndex() {
		return currentPlayer;
	}

	public void greatTaipan(int playerID) throws IllegalMoveException {
		if (hasPlayedCards[playerID]) {
			throw new IllegalMoveException();
		}
		greatTaipan[playerID] = true;
	}

	public void taipan(int playerID) throws IllegalMoveException {
		if (hasPlayedCards[playerID]) {
			throw new IllegalMoveException();
		}
		taipan[playerID] = true;
	}
}
