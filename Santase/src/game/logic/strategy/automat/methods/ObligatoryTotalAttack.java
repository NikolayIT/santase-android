/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy.automat.methods;

import game.beans.Game;
import game.beans.Player;
import game.beans.pack.Pack;
import game.beans.pack.card.Card;
import game.logic.SantaseGame;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * ObligatoryTotalAttack class. PlayCardMethod which implements the logic of
 * playing a card in obligatory when the player can win the game.
 * 
 * @author Dimitar Karamanov
 */
public final class ObligatoryTotalAttack extends BaseMethod {
	/**
	 * Constructor.
	 * 
	 * @param game SantaseGame instance.
	 */
	public ObligatoryTotalAttack(final Game game) {
		super(game);
	}

	/**
	 * Returns player's card.
	 * 
	 * @param player AI player.
	 * @param opposite standart player.
	 * @return Card object instance or null.
	 */
	protected Card getPlayMethodCard(final Player player) {
		int futurePoints = 0;
		final Pack aiPack = new Pack(player.getCards());
		final Pack stPack = new Pack(getRival(player).getCards());
		final Pack hands = new Pack();

		futurePoints += sureHandsPoints(aiPack, stPack, hands);
		futurePoints += getHandsNoTrump(aiPack, stPack, hands);

		final int eventCouple = eventualCouplePoints(aiPack);
		final boolean hasEnoughPoints = futurePoints + eventCouple + player.getPoints(game.getTrumpSuit()) >= SantaseGame.END_GAME_POINTS && !hands.isEmpty();

		if (hasEnoughPoints || stPack.isEmpty()) {
			return getSuitableCard(hands);
		}
		return null;
	}

	/**
	 * Returns a sutable card from the pack.
	 * 
	 * @param hands Pack with hand cards.
	 * @return Card object instance or null.
	 */
	private Card getSuitableCard(final Pack hands) {
		Card result = hands.findMaxSuitCard(game.getTrumpSuit());
		if (result == null) {
			final Card card = hands.getCard(0);
			result = hands.findMaxSuitCard(card.getSuit());
		}
		return result;
	}
}