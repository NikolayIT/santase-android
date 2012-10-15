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
import game.logic.strategy.automat.base.PlayCardMethod;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * ObligatoryEnemyTotalAttack class. PlayCardMethod which implements the logic
 * of playing a obligatory card when enemy can win.
 * 
 * @author Dimitar Karamanov
 */
public final class ObligatoryEnemyTotalAttack extends BaseMethod {
	/**
	 * Help play card method.
	 */
	private final PlayCardMethod obligatoryTotalAttack;

	/**
	 * Help play card method.
	 */
	private final PlayCardMethod obligatorySingleHand;

	/**
	 * Constructor.
	 * 
	 * @param game SantaseGame instance.
	 */
	public ObligatoryEnemyTotalAttack(final Game game) {
		super(game);
		obligatoryTotalAttack = new ObligatoryTotalAttack(game);
		obligatorySingleHand = new ObligatorySingleHand(game);
	}

	/**
	 * Returns player's card.
	 * 
	 * @param player AI player.
	 * @param opposite standart player.
	 * @return Card object instance or null.
	 */
	protected Card getPlayMethodCard(final Player player) {
		Card card = null;
		if (obligatoryTotalAttack.getPlayerCard(player) != null) {
			card = obligatorySingleHand.getPlayerCard(player);
		}
		return card;
	}
}
