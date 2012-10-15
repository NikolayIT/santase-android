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
import game.logic.strategy.automat.executors.ObligatoryAttackCardExecutor;
import game.logic.strategy.automat.executors.ObligatoryDefenceCardExecutor;
import game.logic.strategy.validator.ObligatoryGameValidator;

/**
 * ObligatoryGameAdviser class.
 * 
 * @author Dimitar Karamanov
 */
public final class ObligatoryGameAdviser extends BasicGameAdviser {

    /**
     * ObligatoryAttackCardExecutor helper.
     */
    private final PlayCardMethod obligatoryAttackCardExecutor;

    /**
     * ObligatoryDefenceCardExecutor helper.
     */
    private final PlayCardMethod obligatoryDefenceCardExecutor;

    /**
     * Constructor.
     * 
     * @param game SantaseGame.
     */
    public ObligatoryGameAdviser(Game game) {
        super(game, new ObligatoryGameValidator());
        obligatoryAttackCardExecutor = new ObligatoryAttackCardExecutor(game);
        obligatoryDefenceCardExecutor = new ObligatoryDefenceCardExecutor(game);
    }

    /**
     * Returns an attack card.
     * 
     * @param aiPlayer
     * @param stPlayer
     * @return
     */
    protected Card getAttackCard(final Player player) {
        return obligatoryAttackCardExecutor.getPlayerCard(player);
    }

    /**
     * Returns a defense card.
     * 
     * @param aiPlayer
     * @param stPlayer
     * @return
     */
    protected Card getDefenseCard(final Player player) {
        return obligatoryDefenceCardExecutor.getPlayerCard(player);
    }
}
