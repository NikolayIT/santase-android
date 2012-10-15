/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic;

import game.beans.Game;
import game.beans.Player;
import game.beans.pack.Pack;
import game.beans.pack.card.Card;
import game.beans.pack.card.rank.Rank;
import game.beans.pack.card.suit.Suit;
import game.logic.strategy.BasicGameAdviser;
import game.logic.strategy.ClosedGameAdviser;
import game.logic.strategy.ObligatoryGameAdviser;
import game.logic.strategy.OrdinaryGameAdviser;
import game.logic.strategy.validator.ValidateCode;

/**
 * SantaseGame class represents a Facade which implements the basic logic
 * methods.
 * 
 * @author Dimitar Karamanov
 */
public final class SantaseGame {

	/**
	 * Points of the game and other constants
	 */
	public static final int END_GAME_POINTS = 66;

	public static final int POINTS_ZONE = 33;

	public static final int MAX_LITTLE_GAMES = 11;

	/**
	 * Obligatory game adviser.
	 */
	private BasicGameAdviser obligatoryGameAdviser;

	/**
	 * Ordinary game adviser.
	 */
	private BasicGameAdviser ordinaryGameAdviser;

	/**
	 * Closed game adviser.
	 */
	private BasicGameAdviser closedGameAdviser;
	
    /**
     * Game bean object.
     */
    private Game game;

	/**
	 * Game action constants.
	 */
	public final static int GA_NONE = 0x00;

	public final static int GA_CLOSE = 0x01;

	public final static int GA_COUPLE = 0x02;

	public final static int GA_CHANGE = 0x04;
	
	/**
	 * Constructor.
	 */
	public SantaseGame() {
		super();
		setGame(new Game());
	}
	
    public Pack getGameCards() {
    	return game.getGameCards();
    }
    
    public Player getPlayer() {
    	return game.human;
    }
    
    public Player getComputer() {
    	return game.computer;
    }
	
    public final void setGame(Game game) {
    	this.game = game;
		obligatoryGameAdviser = new ObligatoryGameAdviser(game);
		ordinaryGameAdviser = new OrdinaryGameAdviser(game);
		closedGameAdviser = new ClosedGameAdviser(game);
    }
    
    public final Game getGame() {
    	return game;
    }

	/**
	 * Returns tip message for the provided player.
	 * 
	 * @param gPlayer player.
	 * @return String representing tip message.
	 */
	public Card getTipMessageCard(Player player) {
		Card card = getCard(player);
		return card;
	}

	/**
	 * New game.
	 */
	public void newGame() {
		game.newGame();
	}

	/**
	 * New game.
	 * 
	 * @param player player.
	 */
	public void newGame(final Player player) {
		calculatePoints(player);
		newGame();
	}

	/**
	 * Returns play card for a player.
	 * 
	 * @param player for which returns card.
	 * @param stPlayer opposite player.
	 * @return Card instance.
	 */
	public Card getAICard(final Player player) {
		player.setPlayedCard(null);
		player.setPlayedCard(getCard(player));
		checkGameActionStatus(player);
		return player.getCards().remove(player.getPlayedCard());
	}

	/**
	 * Checks game action status for provided player.
	 * 
	 * @param player player.
	 */
	private void checkGameActionStatus(final Player player) {
		if ((game.getGameActionStatus() & SantaseGame.GA_CHANGE) == SantaseGame.GA_CHANGE) {
			changeTrumpCard(player.getCards().findCard(Rank.Nine, getTrumpSuit()), player);
		}

		if ((game.getGameActionStatus() & SantaseGame.GA_CLOSE) == SantaseGame.GA_CLOSE) {
			setClosedGame(player);
		}

		if ((game.getGameActionStatus() & SantaseGame.GA_COUPLE) == SantaseGame.GA_COUPLE) {
			player.getCouples().setCouple(player.getPlayedCard().getSuit());
		}
	}

	/**
	 * Next tour.
	 * @return boolean.
	 */
	public boolean nextTour() {
		return game.nextTour();
	}

	/**
	 * Returns trump suit.
	 * 
	 * @return trump suit.
	 */
	public Suit getTrumpSuit() {
		return game.getTrumpSuit();
	}

	/**
	 * Changes trump card.
	 * 
	 * @param card instance.
	 * @param gPlayer player.
	 */
	public void changeTrumpCard(final Card card, final Player player) {
		game.changeTrumpCard(card, player);
	}

	public void calculateFuturePoints(final Player player) {
		game.calculateFuturePoints(player);
	}

	private void calculatePoints(final Player player) {
		game.calculatePoints(player);
		//game.calculateFuturePoints(pMove);
	}

	public boolean isBothPlayed() {
		return game.isBothPlayed();
	}

	public boolean canClose() {
		return game.canClose();
	}

	public boolean canEndGame(final Player player) {
		return game.canEndGame(player);
	}

	public ValidateCode validatePlayerMove(Player player, Player attackPlayer, Card card) {
		return getGameAdviser().validatePlayerCard(player, card, attackPlayer.getPlayedCard(), getTrumpSuit());
	}

	// changed 03.06.2005
	public boolean isPlayerTurn(final Player player) {
		return game.isPlayerTurn(player);
	}

	public boolean isPlayerClosed(final Player player) {
		return game.isPlayerClosed(player);
	}

	public boolean isClosedGame() {
		return game.isClosedGame();
	}

	public boolean isNotClosedGame() {
		return game.isNotClosedGame();
	}

	public void setClosedGame(final Player player) {
		game.setClosedGame(player);
	}

	public boolean isObligatoryMode() {
		return game.isObligatoryMode();
	}

	private Card getCard(final Player player) {
		Card result = null;
		game.clearGameActionStatus();

		game.copyState(); // ???
		try {
			if (!player.getCards().isEmpty()) {
				result = getGameAdviser().getCard(player);
			}
		} finally {
			game.restoreState(); // /???
		}
		return result;
	}

	private BasicGameAdviser getGameAdviser() {
		if (isNotClosedGame()) {
			return isObligatoryMode() ? obligatoryGameAdviser : ordinaryGameAdviser;
		}
		return closedGameAdviser;
	}

	public boolean isBigNewGame() {
		return (game.human.getBigGames() != 0 || game.computer.getBigGames() != 0) && (game.human.getLittleGames() == 0 && game.computer.getLittleGames() == 0); 
	}
}
