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
import game.logic.SantaseFacade;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * OrdinaryMaxTrumpCard class. PlayCardMethod which implements the logic of playing a max trump card to win the game.
 * 
 * @author Dimitar Karamanov
 */
public final class OrdinaryMaxTrumpCard extends BaseMethod {
    /**
     * Constructor.
     * 
     * @param game SantaseGame instance.
     */
    public OrdinaryMaxTrumpCard(final Game game) {
        super(game);
    }

    /**
     * Returns player's card.
     * 
     * @param player AI player.
     * @param opposite standard player.
     * @return Card object instance or null.
     */
    protected Card getPlayMethodCard(final Player player) {
        final Card result = player.getCards().findMaxSuitCard(game.getTrumpSuit());
        final Card rivalCard = getRival(player).getPlayedCard();

        if (result != null && rivalCard != null && (!rivalCard.getSuit().equals(game.getTrumpSuit()) || rivalCard.getRank().compareTo(result.getRank()) < 0)) {
            if (player.getPoints(game.getTrumpSuit()) + Card.getPoints(rivalCard) + Card.getPoints(result) >= SantaseFacade.END_GAME_POINTS) {
                return result;
            }
        }
        return null;
    }
}