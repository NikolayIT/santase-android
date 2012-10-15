/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy.automat.executors;

import game.beans.Game;
import game.logic.strategy.automat.executors.base.PlayCardExecutor;
import game.logic.strategy.automat.methods.ClosedDoubleSuitCard;
import game.logic.strategy.automat.methods.ClosedLongestSuitCard;

/**
 * ClosedDefenceInAttack class.
 * @author Dimitar Karamanov
 */
public final class ClosedDefenceInAttackExecutor extends PlayCardExecutor {

    /**
     * Constructor.
     * @param game SantaseGame instance.
     */
    public ClosedDefenceInAttackExecutor(final Game game) {
        super(game);
        register(new ClosedDoubleSuitCard(game));
        register(new ClosedLongestSuitCard(game));
    }
}