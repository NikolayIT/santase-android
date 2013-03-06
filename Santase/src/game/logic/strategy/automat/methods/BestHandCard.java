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
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * BestHandCard class. PlayCardMethod which implements the logic of playing the biggest by Rank hand card. The return card can be trump one or from the same
 * suit.
 * @author Dimitar Karamanov
 */
public final class BestHandCard extends BaseMethod {

    /**
     * Constructor.
     * @param game SantaseGame instance.
     */
    public BestHandCard(final Game game) {
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
        Card rivalCard = getRival(player).getPlayedCard();

        if (rivalCard != null) {
            final Card maxTrumpCard = player.getCards().findMaxSuitCard(game.getTrumpSuit());
            final Card maxSuitCard = player.getCards().findMaxSuitCard(getRival(player).getPlayedCard().getSuit());

            if (maxSuitCard != null && maxSuitCard.getRank().compareTo(rivalCard.getRank()) > 0) {
                result = maxSuitCard;
            }

            if (!rivalCard.getSuit().equals(game.getTrumpSuit()) && maxTrumpCard != null
                    && (maxSuitCard == null || maxSuitCard.getRank().compareTo(maxTrumpCard.getRank()) < 0)) {
                result = maxTrumpCard;
            }
        }
        return result;
    }
}