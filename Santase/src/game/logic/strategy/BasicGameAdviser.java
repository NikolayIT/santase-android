/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy;

import game.beans.Game;
import game.beans.Player;
import game.beans.pack.card.Card;
import game.beans.pack.card.suit.Suit;
import game.logic.strategy.validator.BasicGameValidator;
import game.logic.strategy.validator.ValidateCode;

/**
 * BasicGameAdviser class.
 * 
 * @author Dimitar Karamanov
 */
public abstract class BasicGameAdviser {
	
	private final BasicGameValidator validator;
	
	/**
	 * SantaseGame instance.
	 */
	protected final Game game;

	/**
	 * Constructor.
	 * 
	 * @param game SantaseGme instance.
	 */
	public BasicGameAdviser(Game game, BasicGameValidator validator) {
		this.game = game;
		this.validator = validator;
	}

	/**
	 * Returns a player card.
	 * 
	 * @param aiPlayer
	 * @param stPlayer
	 * @return
	 */
	public final Card getCard(final Player player) {
		return game.getRival(player).getPlayedCard() == null ? getAttackCard(player) : getDefenseCard(player);
	}
	
	/**
	 * Return an attack card.
	 * 
	 * @param aiPlayer
	 * @param stPlayer
	 * @return
	 */
	abstract protected Card getAttackCard(final Player player);

	/**
	 * Returns a defense card.
	 * 
	 * @param aiPlayer
	 * @param stPlayer
	 * @return
	 */
	abstract protected Card getDefenseCard(final Player player);

	/**
	 * Validates a player played card.
	 * 
	 * @param player
	 * @param ownCard
	 * @param rivalCard
	 * @param trumpSuit
	 * @return
	 */
	public final ValidateCode validatePlayerCard(final Player player, final Card ownCard, final Card rivalCard, final Suit trumpSuit) {
		return validator.validatePlayerCard(player, ownCard, rivalCard, trumpSuit);
	}
}
