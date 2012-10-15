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
import game.beans.pack.card.rank.Rank;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * ClosedTrumpCouple class. PlayCardMethod which implements the logic of playing a card ....
 * 
 * @author Dimitar Karamanov
 */
public final class ClosedTrumpCouple extends BaseMethod {
    /**
     * Constructor.
     * 
     * @param game SantaseGame instance.
     */
    public ClosedTrumpCouple(final Game game) {
        super(game);
    }

    /**
     * Returns player's card.
     * 
     * @param player AI player.
     * @param opposite player.
     * @return Card object instance or null.
     */
    protected Card getPlayMethodCard(final Player player) {
        if (player.hasCouple(game.getTrumpSuit())) {
            return player.getCards().findCard(Rank.Queen, game.getTrumpSuit());
        }
        return null;
    }
}