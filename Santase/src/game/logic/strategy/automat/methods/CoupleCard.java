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
import game.logic.strategy.automat.base.PlayCardMethod;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * CoupleCard class. PlayCardMethod which implements the logic of playing a couple card.
 * 
 * @author Dimitar Karamanov
 */
public final class CoupleCard extends BaseMethod {
    /**
     * Helper method.
     */
    private final PlayCardMethod randomCoupleCard;

    /**
     * Constructor.
     * 
     * @param game SantaseGame instance.
     */
    public CoupleCard(final Game game) {
        super(game);
        randomCoupleCard = new RandomCoupleCard(game);
    }

    /**
     * Returns player's card.
     * 
     * @param player AI player.
     * @param opposite player.
     * @return Card object instance or null.
     */
    protected Card getPlayMethodCard(final Player player) {
        return randomCoupleCard.getPlayerCard(player);
    }
}
