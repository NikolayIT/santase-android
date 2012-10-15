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
 * ObligatoryTrumpCard class. PlayCardMethod which implements the logic of
 * playing a obligatory trump card.
 * 
 * @author Dimitar Karamanov
 */
public final class ObligatoryTrumpCard extends BaseMethod {
	/**
	 * Constructor.
	 * 
	 * @param game SantaseGame instance.
	 */
	public ObligatoryTrumpCard(final Game game) {
		super(game);
	}

	/**
	 * Returns player's card.
	 * 
	 * @param player AI player.
	 * @param opposite standard player.
	 * @return Card object instance or null.
	 */
	protected Card getPlayMethodCard(final Player player) {
		Card result = null;
		final Card playerCard = player.getCards().findMaxSuitCard(game.getTrumpSuit());

		if (playerCard != null) {
			final Card oppositeCard = getRival(player).getCards().findMaxSuitCard(game.getTrumpSuit());

			if (oppositeCard != null && oppositeCard.getRank().compareTo(playerCard.getRank()) < 0) {
				return player.getCards().findMinSuitCard(game.getTrumpSuit());
			}

			if (oppositeCard != null && oppositeCard.getRank().compareTo(playerCard.getRank()) > 0) {
				if (player.getCards().getSuitCount(game.getTrumpSuit()) == 2) {
					return playerCard;
				}

				// The code below will pass if the computer has more than 2
				// trump cards
				result = player.getCards().findMinSuitCard(game.getTrumpSuit());
				if (player.getCards().findMinAboveCard(result) != null) {
					return player.getCards().findMinAboveCard(result);
				}
				return result;
			}

			return player.getCards().findMinSuitCard(game.getTrumpSuit());
		}
		return result;
	}
}
