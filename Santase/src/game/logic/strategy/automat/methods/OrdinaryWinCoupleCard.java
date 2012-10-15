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
import game.logic.SantaseGame;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * OrdinaryWinCoupleCard class. PlayCardMethod which implements the logic of playing a win couple card.
 * 
 * @author Dimitar Karamanov
 */
public final class OrdinaryWinCoupleCard extends BaseMethod {
    /**
     * Helper method.
     */
    private final RandomCoupleCard randomCoupleCard;

    /**
     * Constructor.
     * 
     * @param game SantaseGame instance.
     */
    public OrdinaryWinCoupleCard(final Game game) {
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
        Card result = null;
        if (eventualCouplePoints(player.getCards()) + player.getPoints(game.getTrumpSuit()) >= SantaseGame.END_GAME_POINTS) {
            result = randomCoupleCard.getPlayMethodCard(player);
        }
        return result;
    }
}
