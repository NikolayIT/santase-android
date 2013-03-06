/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy.automat.executors;

import game.beans.Game;
import game.logic.strategy.automat.executors.base.PlayAttackCardExecutor;
import game.logic.strategy.automat.methods.CoupleCard;
import game.logic.strategy.automat.methods.ObligatoryDefenceAgainstForty;
import game.logic.strategy.automat.methods.ObligatoryDefenceInAttackWithoutLoose;
import game.logic.strategy.automat.methods.ObligatoryEnemyTotalAttack;
import game.logic.strategy.automat.methods.ObligatoryForceToPlayTrump;
import game.logic.strategy.automat.methods.ObligatoryGetLastHandAttack;
import game.logic.strategy.automat.methods.ObligatorySingleHand;
import game.logic.strategy.automat.methods.ObligatoryTotalAttack;
import game.logic.strategy.automat.methods.SmallestOfAll;

/**
 * ObligatoryAttackCardExecutor class.
 * @author Dimitar Karamanov.
 */
public final class ObligatoryAttackCardExecutor extends PlayAttackCardExecutor {

    /**
     * Constructor.
     * @param game a BelotGame instance.
     */
    public ObligatoryAttackCardExecutor(final Game game) {
        super(game);
        // Register play card methods.
        register(new ObligatoryTotalAttack(game));
        register(new ObligatoryEnemyTotalAttack(game));
        register(new ObligatoryGetLastHandAttack(game));
        register(new ObligatoryDefenceAgainstForty(game));
        register(new CoupleCard(game));
        register(new ObligatoryDefenceInAttackWithoutLoose(game));
        register(new ObligatorySingleHand(game));
        register(new ObligatoryForceToPlayTrump(game));
        register(new SmallestOfAll(game));
    }
}
