/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy;

import game.beans.Game;
import game.beans.Player;
import game.beans.pack.card.Card;
import game.logic.strategy.automat.base.PlayCardMethod;
import game.logic.strategy.automat.executors.ClosedAttackCardExecutor;
import game.logic.strategy.automat.executors.ClosedDefenceCardExecutor;
import game.logic.strategy.validator.ObligatoryGameValidator;

/**
 * ClosedGameAdviser class.
 * @author Dimitar Karamanov
 */
public final class ClosedGameAdviser extends BasicGameAdviser {

    /**
     * ClosedAttackCardExecutor helper.
     */
    private final PlayCardMethod closedAttackCardExecutor;

    /**
     * ClosedDefenceCardExecutor helper.
     */
    private final PlayCardMethod closedDefenceCardExecutor;

    /**
     * Constructor.
     * @param game SantaseGame instance.
     */
    public ClosedGameAdviser(Game game) {
        super(game, new ObligatoryGameValidator());
        closedAttackCardExecutor = new ClosedAttackCardExecutor(game);
        closedDefenceCardExecutor = new ClosedDefenceCardExecutor(game);
    }

    /**
     * Return an attack card.
     * @param aiPlayer
     * @param stPlayer
     * @return
     */
    protected Card getAttackCard(final Player player) {
        return closedAttackCardExecutor.getPlayerCard(player);
    }

    /**
     * Return a defense card.
     * @param aiPlayer
     * @param stPlayer
     * @return
     */
    protected Card getDefenseCard(final Player player) {
        return closedDefenceCardExecutor.getPlayerCard(player);
    }
}
