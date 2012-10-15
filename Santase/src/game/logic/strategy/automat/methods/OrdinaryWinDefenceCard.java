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
 * OrdinaryWinDefenceCard class. PlayCardMethod which implements the logic of playing a win defence card.
 * 
 * @author Dimitar Karamanov
 */
public class OrdinaryWinDefenceCard extends BaseMethod {
    final private BestHandCard bestHandCard;

    /**
     * Constructor.
     * 
     * @param game SantaseGame instance.
     */
    public OrdinaryWinDefenceCard(final Game game) {
        super(game);
        bestHandCard = new BestHandCard(game);
    }

    /**
     * Returns player's card.
     * 
     * @param player AI player.
     * @param opposite player.
     * @return Card object instance or null.
     */
    protected Card getPlayMethodCard(final Player player) {
        final Card result = bestHandCard.getPlayMethodCard(player);
        if (result != null) {
            final int handPoints = Card.getPoints(result) + Card.getPoints(getRival(player).getPlayedCard());
            if (handPoints + eventualCouplePoints(player.getCards()) + player.getPoints(game.getTrumpSuit()) >= SantaseGame.END_GAME_POINTS) {
                return result;
            }
        }
        return null;
    }
}
