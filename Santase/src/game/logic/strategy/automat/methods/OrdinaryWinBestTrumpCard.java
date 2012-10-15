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
import game.logic.SantaseGame;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * OrdinaryWinBestTrumpCard class. PlayCardMethod which implements the logic of playing a the best trump card to win the game.
 * 
 * @author Dimitar Karamanov
 */
public final class OrdinaryWinBestTrumpCard extends BaseMethod {
    /**
     * Constructor.
     * 
     * @param game SantaseGame instance.
     */
    public OrdinaryWinBestTrumpCard(final Game game) {
        super(game);
    }

    /**
     * Returns player's card.
     * 
     * @param player AI player.
     * @param opposite player.
     * @return Card object instance or null.
     */
    protected Card getPlayMethodCard(final Player player) {
        final Card result = player.getCards().findMaxSuitCard(game.getTrumpSuit());
        final Card card = getRival(player).getCards().findMinAllCard();

        if (result != null && isBestSuitCardLeft(player, result)) {
            final Pack pack = super.possibleEnemyCards(player, true);
            final Card smallestCard = pack.findMinAllCard();

            final int cardsPoints = Card.getPoints(result) + Card.getPoints(card) + Card.getPoints(smallestCard);

            if (cardsPoints + eventualCouplePoints(player.getCards()) + player.getPoints(game.getTrumpSuit()) >= SantaseGame.END_GAME_POINTS) {
                return result;
            }
        }
        return null;
    }
}
