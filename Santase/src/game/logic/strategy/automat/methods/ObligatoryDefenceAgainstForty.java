/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy.automat.methods;

import game.beans.Game;
import game.beans.Player;
import game.beans.pack.card.Card;
import game.logic.strategy.automat.base.PlayCardMethod;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * ObligatoryDefenceAgainstForty class. PlayCardMethod which implements the logic of playing a card in forty (trump couple) defence.
 * @author Dimitar Karamanov
 */
public final class ObligatoryDefenceAgainstForty extends BaseMethod {

    /**
     * Help play card method.
     */
    private final PlayCardMethod obligatorySingleHand;

    /**
     * Constructor.
     * @param game SantaseGame instance.
     */
    public ObligatoryDefenceAgainstForty(final Game game) {
        super(game);
        obligatorySingleHand = new ObligatorySingleHand(game);
    }

    /**
     * Returns player's card.
     * @param player AI player.
     * @param opposite player.
     * @return Card object instance or null.
     */
    protected Card getPlayMethodCard(final Player player) {
        if (getRival(player).hasCouple(game.getTrumpSuit())) {
            return obligatorySingleHand.getPlayerCard(player);
        }
        return null;
    }
}
