/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy.automat.executors;

import game.beans.Game;
import game.logic.strategy.automat.executors.base.PlayCardExecutor;
import game.logic.strategy.automat.methods.ObligatoryBiggerCard;
import game.logic.strategy.automat.methods.ObligatoryNoNeed;
import game.logic.strategy.automat.methods.ObligatoryTrumpCard;
import game.logic.strategy.automat.methods.ObligatoryTrumpCoupleCard;
import game.logic.strategy.automat.methods.SmallestOfAll;
import game.logic.strategy.automat.methods.SmallestOfSuit;

/**
 * ObligatoryDefenceCardExecutor class.
 * 
 * @author Dimitar Karamanov
 */
public final class ObligatoryDefenceCardExecutor extends PlayCardExecutor {

	/**
	 * Constructor.
	 * @param game a BelotGame instance.
	 */
	public ObligatoryDefenceCardExecutor(final Game game) {
		super(game);
		// Register play card methods.
		register(new ObligatoryBiggerCard(game));
		register(new SmallestOfSuit(game));
		register(new ObligatoryTrumpCoupleCard(game));
		register(new ObligatoryTrumpCard(game));
		register(new ObligatoryNoNeed(game));
		register(new SmallestOfAll(game));
	}
}
