/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy.automat.methods;

import game.beans.Game;
import game.beans.Player;
import game.beans.pack.PackIterator;
import game.beans.pack.card.Card;
import game.beans.pack.card.rank.Rank;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * OrdinaryHandCard class. PlayCardMethod which implements the logic of playing a ordinary hand card.
 * @author Dimitar Karamanov
 */
public final class OrdinaryHandCard extends BaseMethod {

    /**
     * Constructor.
     * @param game SantaseGame instance.
     */
    public OrdinaryHandCard(final Game game) {
        super(game);
    }

    /**
     * Returns player's card.
     * @param player AI player.
     * @param opposite player.
     * @return Card object instance or null.
     */
    protected Card getPlayMethodCard(final Player player) {
        final Card rivalCard = getRival(player).getPlayedCard();
        if (rivalCard != null) {
            if (!rivalCard.getSuit().equals(game.getTrumpSuit()) || rivalCard.getRank().equals(Rank.Ten)) {
                return getBiggerCard(player);
            }
        }
        return null;
    }

    /**
     * Returns player's bigger card them opposite player.
     * @param human AI player.
     * @param opposite standart player.
     * @return Card object instance or null.
     */
    private Card getBiggerCard(final Player player) {
        Card result = null;
        for (PackIterator iterator = player.getCards().iterator(); iterator.hasNext();) {
            final Card card = iterator.next();
            if (card.getSuit().equals(getRival(player).getPlayedCard().getSuit()) && card.getRank().compareTo(getRival(player).getPlayedCard().getRank()) > 0
                    && noPossibleCoupleCard(player, card)) {
                if (result == null || result.getRank().compareTo(card.getRank()) < 0) {
                    result = card;
                }
            }
        }
        return result;
    }
}