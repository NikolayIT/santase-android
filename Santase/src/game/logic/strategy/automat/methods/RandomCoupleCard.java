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
import game.beans.pack.card.suit.Suit;
import game.beans.pack.card.suit.SuitIterator;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * RandomCoupleCard class. PlayCardMethod which implements the logic of playing a random couple card.
 * 
 * @author Dimitar Karamanov
 */
public final class RandomCoupleCard extends BaseMethod {
    /**
     * Constructor.
     * 
     * @param game SantaseGame instance.
     */
    public RandomCoupleCard(final Game game) {
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

        for (SuitIterator iterator = Suit.iterator(); iterator.hasNext();) {
            final Suit suit = iterator.next();
            if (!game.getTrumpSuit().equals(suit) && player.hasCouple(suit)) {
                return player.getCards().findCard(Rank.Queen, suit);
            }
        }
        return null;
    }
}
