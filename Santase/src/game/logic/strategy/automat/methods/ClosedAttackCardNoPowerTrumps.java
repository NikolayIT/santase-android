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
import game.logic.strategy.automat.base.PlayCardMethod;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * ClosedAttackCardNoPowerTrumps class. PlayCardMethod which implements the logic of playing an attack card when no power trumps in closed game.
 * 
 * @author Dimitar Karamanov
 */
public final class ClosedAttackCardNoPowerTrumps extends BaseMethod {
    /**
     * Help play card method.
     */
    private final PlayCardMethod closedNoTrumpAttackCardPowerTrumps;

    /**
     * Constructor.
     * 
     * @param game SantaseGame instance.
     */
    public ClosedAttackCardNoPowerTrumps(final Game game) {
        super(game);
        closedNoTrumpAttackCardPowerTrumps = new ClosedNoTrumpAttackCardPowerTrumps(game);
    }

    /**
     * Returns player's card.
     * 
     * @param player AI player.
     * @param opposite player.
     * @return Card object instance or null.
     */
    protected Card getPlayMethodCard(final Player player) {
        if (!hasPowerTrumps(player, true)) {
            Card result;
            if ((result = getSuitAttackCardNoPowerTrumps(player)) != null) {
                return result;
            }
            if ((result = closedNoTrumpAttackCardPowerTrumps.getPlayerCard(player)) != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns an attack card when there is no power trump cards.
     * 
     * @param player AI player.
     * @param opposite player.
     * @return Card object instnace or null.
     */
    private Card getSuitAttackCardNoPowerTrumps(final Player player) {
        Pack posEnemyCards = possibleEnemyCards(player, true);

        for (PackIterator iterator = player.getCards().iterator(); iterator.hasNext();) {
            final Card card = iterator.next();
            final Card biggestSuitCard = player.getCards().findMaxSuitCard(card.getSuit());
            if (card.getRank().equals(biggestSuitCard.getRank())) {
                final Card biggestEnemySuitCard = posEnemyCards.findMaxSuitCard(card.getSuit());
                final int enemySuitCount = posEnemyCards.getSuitCount(card.getSuit());

                if (biggestEnemySuitCard != null && biggestSuitCard.getRank().compareTo(biggestEnemySuitCard.getRank()) > 0 && enemySuitCount > 2) {
                    return biggestSuitCard;
                }
            }
        }
        return null;
    }
}