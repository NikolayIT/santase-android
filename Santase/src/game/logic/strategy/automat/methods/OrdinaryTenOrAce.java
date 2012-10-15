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
 * OrdinaryTenOrAce class. PlayCardMethod which implements the logic of playing
 * a ten or ace rank card.
 * 
 * @author Dimitar Karamanov
 */
public final class OrdinaryTenOrAce extends BaseMethod {
	/**
	 * Constructor.
	 * 
	 * @param game SantaseGame instance.
	 */
	public OrdinaryTenOrAce(final Game game) {
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
		// check for Ten trump if player played
		if (getRival(player).getPlayedCard().getRank().equals(Rank.Ten) && getRival(player).getPlayedCard().getSuit().equals(game.getTrumpSuit())) {
			final Card AceTrump = player.getCards().findCard(Rank.Ace, game.getTrumpSuit());
			if (AceTrump != null) {
				return AceTrump;
			}
		}

		// check for Ten or ACE not TRUMP
		if ((getRival(player).getPlayedCard().getRank().equals(Rank.Ace) || getRival(player).getPlayedCard().getRank().equals(Rank.Ten))
				&& !getRival(player).getPlayedCard().getSuit().equals(game.getTrumpSuit())) {
			Card result = player.getCards().findCard(Rank.Jack, game.getTrumpSuit());
			if (result != null) {
				return result;
			}

			result = player.getCards().findCard(Rank.Nine, game.getTrumpSuit());
			if (result != null && (game.getGameCards().getCard(game.getGameCards().getSize() - 1).getRank().equals(Rank.Jack) || game.getGameCards().getSize() == 4)) {
				return result;
			}

			if (result != null && player.getCards().hasCouple(game.getTrumpSuit())) {
				return result;
			}

			result = player.getCards().findCard(Rank.Queen, game.getTrumpSuit());
			if (result != null && noPossibleCoupleCard(player, result)) {
				return result;
			}

			result = player.getCards().findCard(Rank.King, game.getTrumpSuit());
			if (result != null && noPossibleCoupleCard(player, result)) {
				return result;
			}

			result = player.getCards().findCard(Rank.Ten, game.getTrumpSuit());
			if (result != null) {
				return result;
			}

			result = player.getCards().findCard(Rank.Ace, game.getTrumpSuit());
			if (result != null) {
				return result;
			}
		}
		return null;
	}
}