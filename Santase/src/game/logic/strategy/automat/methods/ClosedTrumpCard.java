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
import game.beans.pack.card.rank.Rank;
import game.beans.pack.card.rank.RankIterator;
import game.logic.SantaseGame;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * ClosedTrumpCard class. PlayCardMethod which implements the logic of playing a trump card in a cloased game.
 * 
 * @author Dimitar Karamanov
 */
public final class ClosedTrumpCard extends BaseMethod {
    /**
     * Constructor.
     * 
     * @param game SantaseGame instance.
     */
    public ClosedTrumpCard(final Game game) {
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
        Card result = null;

        final Card playerCard = player.getCards().findMaxSuitCard(game.getTrumpSuit());

        if (playerCard != null) {
            final int eventCouple = eventualCouplePoints(player.getCards());
            final int futurePoints = Card.getPoints(getRival(player).getPlayedCard()) + Card.getPoints(playerCard);

            final boolean hasEnoughPoints = eventCouple + futurePoints + player.getPoints(game.getTrumpSuit()) >= SantaseGame.END_GAME_POINTS;

            if (hasEnoughPoints) {
                return playerCard;
            }

            final int count = player.getCards().getSuitCount(game.getTrumpSuit());

            if (count == 3) {
                Card card = player.getCards().findMinSuitCard(game.getTrumpSuit());

                if (card != null) {
                    return player.getCards().findMinAboveCard(card);
                }
            }

            if (count == 2) {
                Card card = player.getCards().findMaxSuitCard(game.getTrumpSuit());

                if (card != null && !isTheBiggestSuitCardLeft(player, card)) {
                    return card;
                }
            }

            return player.getCards().findMinSuitCard(game.getTrumpSuit());
        }

        return result;
    }

    /**
     * Return true if the cards is the biggest not played card from its suit.
     * 
     * @param player AI player.
     * @param opposite player.
     * @param card which is checked.
     * @return true if the cards is the biggest not played card from its suit.
     */
    private boolean isTheBiggestSuitCardLeft(final Player player, final Card card) {
        final Pack pack = possibleEnemyCards(player, true);

        for (RankIterator iterator = Rank.iterator(); iterator.hasNext();) {
            final Rank rank = iterator.next();
            if (rank.compareTo(card.getRank()) > 0) {
                if (pack.findCard(rank, card.getSuit()) == null) {
                    return false;
                }
            }
        }
        return true;
    }
}
