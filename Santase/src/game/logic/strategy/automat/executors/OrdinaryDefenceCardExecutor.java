/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy.automat.executors;

import game.beans.Game;
import game.logic.strategy.automat.executors.base.PlayCardExecutor;
import game.logic.strategy.automat.methods.OrdinaryHandCard;
import game.logic.strategy.automat.methods.OrdinaryLeftTwoCard;
import game.logic.strategy.automat.methods.OrdinaryMaxTrumpCard;
import game.logic.strategy.automat.methods.OrdinarySmallCard;
import game.logic.strategy.automat.methods.OrdinaryTenOrAce;
import game.logic.strategy.automat.methods.OrdinaryTrump4Couple;
import game.logic.strategy.automat.methods.OrdinaryTrumpCard;
import game.logic.strategy.automat.methods.OrdinaryWinDefenceCard;
import game.logic.strategy.automat.methods.OrdinaryWinWithPowerTrumpsHands;
import game.logic.strategy.automat.methods.SmallestOfAll;

/**
 * OrdinaryAttackCardExecutor executor.
 * @author Dimitar Karamanov
 */
public final class OrdinaryDefenceCardExecutor extends PlayCardExecutor {
	
	/**
	 * Constructor.
	 * @param game a BelotGame instance.
	 */
	public OrdinaryDefenceCardExecutor(final Game game) {
		super(game);
		// Register play card methods.
		register(new OrdinaryWinDefenceCard(game));
		register(new OrdinaryMaxTrumpCard(game));
		register(new OrdinaryWinWithPowerTrumpsHands(game));
		register(new OrdinaryLeftTwoCard(game));
		register(new OrdinaryHandCard(game));
		register(new OrdinaryTenOrAce(game));
		register(new OrdinaryTrump4Couple(game));
		register(new OrdinarySmallCard(game));
		register(new OrdinaryTrumpCard(game));
		register(new SmallestOfAll(game));
	}
}
