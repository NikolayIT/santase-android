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
import game.beans.pack.card.rank.Rank;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * OrdinaryTrump4Couple class. PlayCardMethod which implements the logic of
 * playing a trump card to play after couple card.
 * 
 * @author Dimitar Karamanov
 */
public final class OrdinaryTrump4Couple extends BaseMethod {
	private final OrdinaryTrumpCard ordinaryTrumpCard;

	/**
	 * Constructor.
	 * 
	 * @param game SantaseGame instance.
	 */
	public OrdinaryTrump4Couple(final Game game) {
		super(game);
		ordinaryTrumpCard = new OrdinaryTrumpCard(game);
	}

	/**
	 * Returns player's card.
	 * 
	 * @param player AI player.
	 * @param opposite player.
	 * @return Card object instance or null.
	 */
	protected Card getPlayMethodCard(final Player player) {
		final Card result = getTrumpCard4TrumpCouple(player);
		if (result != null) {
			return result;
		}
		if (eventualCouplePoints(player.getCards()) > 0) {
			return ordinaryTrumpCard.getPlayerCard(player);
		}
		return null;
	}

	/**
	 * Returns player's card.
	 * 
	 * @param player AI player.
	 * @return Card object instance or null.
	 */
	private Card getTrumpCard4TrumpCouple(final Player player) {
		Card result = null;

		final boolean haveNine = player.getCards().findCard(Rank.Nine, game.getTrumpSuit()) != null;
		final boolean canClose = game.getGameCards().getSize() > 4;

		if (haveNine && canClose) {
			final boolean haveKingOrQueen = player.getCards().findCard(Rank.Queen, game.getTrumpSuit()) != null
					|| player.getCards().findCard(Rank.King, game.getTrumpSuit()) != null;

			final boolean lastCardIsKingOrQueen = game.getGameCards().getCard(game.getGameCards().getSize() - 1).isKingOrQueen();

			if (haveKingOrQueen && lastCardIsKingOrQueen) {
				if ((result = player.getCards().findCard(Rank.Jack, game.getTrumpSuit())) != null) {
					return result;
				}

				if ((result = player.getCards().findCard(Rank.Ten, game.getTrumpSuit())) != null) {
					return result;
				}

				if ((result = player.getCards().findCard(Rank.Ace, game.getTrumpSuit())) != null) {
					return result;
				}
			}
		}
		return result;
	}
}
