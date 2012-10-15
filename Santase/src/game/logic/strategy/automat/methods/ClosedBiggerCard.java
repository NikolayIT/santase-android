/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy.automat.methods;

import game.beans.Game;
import game.beans.Player;
import game.beans.pack.card.Card;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * ClosedBiggerCard class. PlayCardMethod which implements the logic of playing
 * a obligatory bigger card in a closed game.
 * 
 * @author Dimitar Karamanov
 */
public final class ClosedBiggerCard extends BaseMethod {
	/**
	 * Constructor.
	 * 
	 * @param game SantaseGame instance.
	 */
	public ClosedBiggerCard(final Game game) {
		super(game);
	}

	/**
	 * Returns player's card.
	 * 
	 * @param player AI player.
	 * @param opposite player.
	 * @return Card object instance or null.
	 */
	protected Card getPlayMethodCard(final Player player) {
		if (getRival(player).getPlayedCard() != null) {
			final Card result = player.getCards().findMaxSuitCard(getRival(player).getPlayedCard().getSuit());
			if (result != null && result.getRank().compareTo(getRival(player).getPlayedCard().getRank()) > 0) {
				return result;
			}
		}
		return null;
	}
}
