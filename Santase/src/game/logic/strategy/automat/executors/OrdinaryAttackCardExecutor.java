/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy.automat.executors;

import game.beans.Game;
import game.beans.Player;
import game.logic.strategy.automat.executors.base.PlayAttackCardExecutor;
import game.logic.strategy.automat.methods.CoupleCard;
import game.logic.strategy.automat.methods.OrdinaryCloseGameCard;
import game.logic.strategy.automat.methods.OrdinarySmallCard;
import game.logic.strategy.automat.methods.OrdinaryTrumpCard;
import game.logic.strategy.automat.methods.OrdinaryWinBestTrumpCard;
import game.logic.strategy.automat.methods.OrdinaryWinCoupleCard;
import game.logic.strategy.automat.methods.OrdinaryWinWithPowerTrumpsHands;
import game.logic.strategy.automat.methods.SmallestOfAll;

/**
 * OrdinaryAttackCardExecutor class executor.
 * @author Dimitar Karamanov
 */
public final class OrdinaryAttackCardExecutor extends PlayAttackCardExecutor {

    /**
     * Constructor.
     * @param game a BelotGame instance.
     */
    public OrdinaryAttackCardExecutor(final Game game) {
        super(game);
        // Register play card methods.
        register(new OrdinaryWinCoupleCard(game));
        register(new OrdinaryCloseGameCard(game));
        register(new OrdinaryWinBestTrumpCard(game));
        register(new OrdinaryWinWithPowerTrumpsHands(game));
        register(new CoupleCard(game));
        register(new OrdinarySmallCard(game));
        register(new OrdinaryTrumpCard(game));
        register(new SmallestOfAll(game));
    }

    /**
     * Handler method providing the user facility to check custom condition for methods executions.
     * @param player for which is called the executor.
     * @param opposite for which is called the executor.
     * @return true to process method execution false to not.
     */
    protected boolean fitPreCondition(final Player player) {
        game.changeTrumpCard(player);
        return super.fitPreCondition(player);
    }
}
