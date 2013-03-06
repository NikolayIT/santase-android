/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy.automat.methods;

import game.beans.Game;
import game.beans.Player;
import game.beans.pack.Pack;
import game.beans.pack.card.Card;
import game.beans.pack.card.suit.Suit;
import game.beans.pack.card.suit.SuitIterator;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * ObligatorySingleHand class. PlayCardMethod which implements the logic of playing a obligatory single hand card.
 * @author Dimitar Karamanov
 */
public final class ObligatorySingleHand extends BaseMethod {

    /**
     * Constructor.
     * @param game SantaseGame instance.
     */
    public ObligatorySingleHand(final Game game) {
        super(game);
    }

    /**
     * Returns player's card.
     * @param player AI player.
     * @param opposite standart player.
     * @return Card object instance or null.
     */
    protected Card getPlayMethodCard(final Player player) {
        final Pack playerPack = new Pack(player.getCards());
        final Pack oppositePack = new Pack(getRival(player).getCards());
        final Pack hands = new Pack();

        for (SuitIterator iterator = Suit.iterator(); iterator.hasNext();) {
            final Suit suit = iterator.next();
            if (singleHandPoints(playerPack, oppositePack, hands, suit) != 0) {
                return hands.getCard(0);
            }
        }
        return null;
    }
}
