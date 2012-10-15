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
import game.beans.pack.card.suit.Suit;
import game.beans.pack.card.suit.SuitIterator;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * ClosedLongestSuitCard class. PlayCardMethod which implements the logic of playing a card from the longest suit.
 * 
 * @author Dimitar Karamanov
 */
public final class ClosedLongestSuitCard extends BaseMethod {
    /**
     * Constructor.
     * 
     * @param game SantaseGame instance.
     */
    public ClosedLongestSuitCard(final Game game) {
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
        Card result = null;
        Suit suit = null;
        int suitCount = -1;

        for (SuitIterator it = Suit.iterator(); it.hasNext();) {
            final Suit current = it.next();
            final int count = player.getCards().getSuitCount(current);

            if (count > suitCount) {
                suit = current;
                suitCount = count;
            }
        }

        if (suit != null) {
            result = player.getCards().findMinSuitCard(suit);
        }

        return result;
    }
}