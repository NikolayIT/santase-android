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
 * OrdinaryLeftTwoCard class. PlayCardMethod which implements the logic of playing a card when left only two cards to (one hand) till obligatory game mode.
 * 
 * @author Dimitar Karamanov
 */
public final class OrdinaryLeftTwoCard extends BaseMethod {
    
    private final OrdinarySingleLoose ordinarySingleLoose;

    /**
     * Constructor.
     * 
     * @param game SantaseGame instance.
     */
    public OrdinaryLeftTwoCard(final Game game) {
        super(game);
        ordinarySingleLoose = new OrdinarySingleLoose(game);
    }

    /**
     * Returns player's card.
     * 
     * @param player AI player.
     * @param opposite player.
     * @return Card object instance or null.
     */
    protected Card getPlayMethodCard(final Player player) {
        if (game.getGameCards().getSize() == TWO_CARDS) {
            final Card result = ordinarySingleLoose.getPlayMethodCard(player);
            if (result != null && result.getSuit().equals(game.getTrumpSuit())) {
                return null;
            }
            return canPlayWithoutLoose(getRival(player), result) ? result : null;
        }
        return null;
    }
}