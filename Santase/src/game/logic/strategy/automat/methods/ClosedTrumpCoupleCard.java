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
 * ClosedTrumpCoupleCard class. PlayCardMethod which implements the logic of playing a trump card for trump couple annoince.
 * @author Dimitar Karamanov
 */
public final class ClosedTrumpCoupleCard extends BaseMethod {

    /**
     * Constructor.
     * @param game SantaseGame instance.
     */
    public ClosedTrumpCoupleCard(final Game game) {
        super(game);
    }

    /**
     * Returns player's card.
     * @param player AI player.
     * @param opposite player.
     * @return Card object instance or null.
     */
    protected Card getPlayMethodCard(final Player player) {
        Card result = null;

        if (player.getCards().hasCouple(game.getTrumpSuit())) {
            if ((result = player.getCards().findCard(Rank.Ten, game.getTrumpSuit())) != null) {
                return result;
            }

            if ((result = player.getCards().findCard(Rank.Nine, game.getTrumpSuit())) != null) {
                return result;
            }

            if ((result = player.getCards().findCard(Rank.Jack, game.getTrumpSuit())) != null) {
                return result;
            }

            if ((result = player.getCards().findCard(Rank.Ace, game.getTrumpSuit())) != null) {
                return result;
            }
        }
        return result;
    }
}
