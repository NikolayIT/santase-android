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
import game.beans.pack.PackIterator;
import game.beans.pack.card.Card;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * OrdinarySingleLoose class. PlayCardMethod which implements the logic of playing a ordinary single loose card.
 * @author Dimitar Karamanov
 */
public final class OrdinarySingleLoose extends BaseMethod {

    /**
     * Constructor.
     * @param game SantaseGame instance.
     */
    public OrdinarySingleLoose(final Game game) {
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
        for (PackIterator iterator = player.getCards().iterator(); iterator.hasNext();) {
            final Card card = iterator.next();
            final Pack pack = possibleEnemyCards(player, true);

            final Card bestSuitCard = pack.findMaxSuitCard(card.getSuit());
            final boolean isSingleCard = player.getCards().getSuitCount(card.getSuit()) == 1;

            if (isSingleCard && bestSuitCard != null && bestSuitCard.getRank().compareTo(card.getRank()) > 0) {
                if (result == null || result.getRank().compareTo(card.getRank()) > 0) {
                    result = card;
                }
            }
        }
        return canPlayWithoutLoose(getRival(player), result) ? result : null;
    }
}
