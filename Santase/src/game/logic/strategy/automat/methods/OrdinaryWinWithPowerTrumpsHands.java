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
import game.logic.SantaseFacade;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * OrdinaryWinWithPowerTrumpsHands class. PlayCardMethod which implements the logic of playing a card to win the game with power trumps.
 * @author Dimitar Karamanov
 */
public final class OrdinaryWinWithPowerTrumpsHands extends BaseMethod {

    final static int POSSIBLE_MORE_POINTS = 6;

    /**
     * Constructor.
     * @param game SantaseGame instance.
     */
    public OrdinaryWinWithPowerTrumpsHands(final Game game) {
        super(game);
    }

    /**
     * Returns player's card.
     * @param player AI player.
     * @param opposite standart player.
     * @return Card object instance or null.
     */
    protected Card getPlayMethodCard(final Player player) {
        if (hasPowerTrumps(player, false) && getPowerTrumpHandsPoints(player) >= (SantaseFacade.END_GAME_POINTS - POSSIBLE_MORE_POINTS)) {
            return player.getCards().findMaxSuitCard(game.getTrumpSuit());
        }
        return null;
    }

    /**
     * Returns power trumps hands points.
     * @param player AI player.
     * @param opposite player.
     * @return int the power trump hands points.
     */
    private int getPowerTrumpHandsPoints(final Player player) {
        int result = 0;

        if (player.getCards().getSuitCount(game.getTrumpSuit()) == 0) {
            return result;
        }

        final Pack copyPack = new Pack(player.getCards());
        final Pack possibleOppositeCards = possibleEnemyCards(player, true);

        int points = 0;
        while ((points = getPowerTrumpSingleHandPoints(copyPack, possibleOppositeCards)) > 0) {
            result += points;
        }

        result += eventualCouplePoints(player.getCards());
        result += player.getPoints(game.getTrumpSuit());

        return result;
    }

    /**
     * Returns power trumps single hand points.
     * @param copyPack copy of AI player pack.
     * @param possibleOppositeCards possible cards of opposite player.
     * @return int the power trump single hand points.
     */
    private int getPowerTrumpSingleHandPoints(final Pack copyPack, final Pack possibleOppositeCards) {
        for (PackIterator iterator = copyPack.iterator(); iterator.hasNext();) {
            final Card card = iterator.next();
            if (card.getSuit().equals(game.getTrumpSuit())) {
                final Card smallestlCard = possibleOppositeCards.findMinAllCard();

                copyPack.remove(card);
                if (smallestlCard != null) {
                    possibleOppositeCards.remove(smallestlCard);
                }
                return Card.getPoints(smallestlCard) + Card.getPoints(card);
            }
        }
        return 0;
    }
}
