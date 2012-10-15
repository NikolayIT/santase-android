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
import game.logic.strategy.automat.executors.OrdinaryAttackCardExecutor;
import game.logic.strategy.automat.executors.OrdinaryDefenceCardExecutor;
import game.logic.strategy.validator.OrdinaryGameValidator;

/**
 * OrdinaryGameAdviser class.
 * 
 * @author Dimitar Karamanov
 */
public final class OrdinaryGameAdviser extends BasicGameAdviser {
    /**
     * OrdinaryAttackCardExecutor helper.
     */
    private final PlayCardMethod ordinaryAttackCardExecutor;

    /**
     * OrdinaryDefenceCardExecutor helper.
     */
    private final PlayCardMethod ordinaryDefenceCardExecutor;

    /**
     * Constructor.
     * 
     * @param game SantaseGame instance.
     */
    public OrdinaryGameAdviser(Game game) {
        super(game, new OrdinaryGameValidator());
        ordinaryAttackCardExecutor = new OrdinaryAttackCardExecutor(game);
        ordinaryDefenceCardExecutor = new OrdinaryDefenceCardExecutor(game);
    }

    /**
     * Returns an attack card.
     * 
     * @param aiPlayer
     * @param stPlayer
     * @return
     */
    protected Card getAttackCard(final Player player) {
        return ordinaryAttackCardExecutor.getPlayerCard(player);
    }

    /**
     * Returns a defense card.
     * 
     * @param aiPlayer
     * @param stPlayer
     * @return
     */
    protected Card getDefenseCard(final Player player) {
        return ordinaryDefenceCardExecutor.getPlayerCard(player);
    }
}
