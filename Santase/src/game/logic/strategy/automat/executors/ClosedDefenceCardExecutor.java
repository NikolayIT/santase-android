/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy.automat.executors;

import game.beans.Game;
import game.logic.strategy.automat.executors.base.PlayCardExecutor;
import game.logic.strategy.automat.methods.ClosedBiggerCard;
import game.logic.strategy.automat.methods.ClosedTrumpCard;
import game.logic.strategy.automat.methods.ClosedTrumpCoupleCard;
import game.logic.strategy.automat.methods.SmallestOfAll;
import game.logic.strategy.automat.methods.SmallestOfSuit;

/**
 * ClosedDefenceInAttack class.
 * 
 * @author Dimitar Karamanov
 */
public final class ClosedDefenceCardExecutor extends PlayCardExecutor {
	/**
	 * Constructor.
	 * 
	 * @param game SantaseGame instance.
	 */
	public ClosedDefenceCardExecutor(final Game game) {
		super(game);
		register(new ClosedBiggerCard(game));
		register(new SmallestOfSuit(game));
		register(new ClosedTrumpCoupleCard(game));
		register(new ClosedTrumpCard(game));
		register(new SmallestOfAll(game));
	}
}