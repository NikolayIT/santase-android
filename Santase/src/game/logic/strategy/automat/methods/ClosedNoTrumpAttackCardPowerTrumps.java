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
 * ClosedNoTrumpAttackCardPowerTrumps class. PlayCardMethod which implements the logic of playing a power trumps no trump card.
 * 
 * @author Dimitar Karamanov
 */
public final class ClosedNoTrumpAttackCardPowerTrumps extends BaseMethod {
    /**
     * Constructor.
     * 
     * @param game SantaseGame instance.
     */
    public ClosedNoTrumpAttackCardPowerTrumps(final Game game) {
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
        final Pack posEnemyCards = possibleEnemyCards(player, true);

        for (PackIterator iterator = player.getCards().iterator(); iterator.hasNext();) {
            final Card card = iterator.next();
            final Card biggestSuitCard = player.getCards().findMaxSuitCard(card.getSuit());

            if (card.getRank().equals(biggestSuitCard.getRank())) {
                final Card biggestEnemySuitCard = posEnemyCards.findMaxSuitCard(card.getSuit());
                if (biggestEnemySuitCard == null || biggestSuitCard.getRank().compareTo(biggestEnemySuitCard.getRank()) > 0) {
                    return biggestSuitCard;
                }
            }
        }
        return null;
    }
}