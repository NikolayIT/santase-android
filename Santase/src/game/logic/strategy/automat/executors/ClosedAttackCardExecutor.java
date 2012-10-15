/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy.automat.executors;

import game.beans.Game;
import game.logic.strategy.automat.executors.base.PlayAttackCardExecutor;
import game.logic.strategy.automat.methods.ClosedAceTrump;
import game.logic.strategy.automat.methods.ClosedAttackCardNoPowerTrumps;
import game.logic.strategy.automat.methods.ClosedAttackCardPowerTrumps;
import game.logic.strategy.automat.methods.ClosedTrumpCouple;
import game.logic.strategy.automat.methods.CoupleCard;
import game.logic.strategy.automat.methods.SmallestOfAll;

/**
 * ClosedAttackCardExecutor class.
 * 
 * @author Dimitar Karamanov
 */
public final class ClosedAttackCardExecutor extends PlayAttackCardExecutor {
	/**
	 * Constructor.
	 * 
	 * @param game a BelotGame instance.
	 */
	public ClosedAttackCardExecutor(final Game game) {
		super(game);
		// Register play card methods.
		register(new ClosedAceTrump(game));
		register(new ClosedTrumpCouple(game));
		register(new ClosedAttackCardPowerTrumps(game));
		register(new ClosedAttackCardNoPowerTrumps(game));
		register(new CoupleCard(game));
		register(new ClosedDefenceInAttackExecutor(game));
		register(new SmallestOfAll(game));
	}
}
