/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy.automat.methods;

import game.beans.Game;
import game.beans.Player;
import game.beans.pack.PackIterator;
import game.beans.pack.card.Card;
import game.beans.pack.card.rank.Rank;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * OrdinaryTrumpCard class. PlayCardMethod which implements the logic of playing a ordinary trump card.
 * @author Dimitar Karamanov
 */
public final class OrdinaryTrumpCard extends BaseMethod {

    /**
     * Constructor.
     * @param game SantaseGame instance.
     */
    public OrdinaryTrumpCard(final Game game) {
        super(game);
    }

    /**
     * Returns player's card.
     * @param player AI player.
     * @param opposite player.
     * @return Card object instance or null.
     */
    protected Card getPlayMethodCard(final Player player) {
        Card result = player.getCards().findCard(Rank.Jack, game.getTrumpSuit());
        if (result != null && (result.isFromSameSuitAndBigger(getRival(player).getPlayedCard()) || !isTrumpCard(getRival(player).getPlayedCard()))) {
            return result;
        }

        result = player.getCards().findCard(Rank.Nine, game.getTrumpSuit());
        if (result != null && (result.isFromSameSuitAndBigger(getRival(player).getPlayedCard()) || !isTrumpCard(getRival(player).getPlayedCard()))) {
            if (game.getGameCards().getCard(game.getGameCards().getSize() - 1).getRank().equals(Rank.Jack) || game.getGameCards().getSize() <= 4) {
                return result;
            }
            if (player.getCards().hasCouple(game.getTrumpSuit())) {
                return result;
            }
        }

        result = player.getCards().findCard(Rank.Queen, game.getTrumpSuit());
        if (result != null && noPossibleCoupleCard(player, result)
                && (result.isFromSameSuitAndBigger(getRival(player).getPlayedCard()) || !isTrumpCard(getRival(player).getPlayedCard()))) {
            return result;
        }

        result = player.getCards().findCard(Rank.King, game.getTrumpSuit());
        if (result != null && noPossibleCoupleCard(player, result)
                && (result.isFromSameSuitAndBigger(getRival(player).getPlayedCard()) || !isTrumpCard(getRival(player).getPlayedCard()))) {
            return result;
        }

        result = player.getCards().findCard(Rank.Ten, game.getTrumpSuit());
        if (result != null && (result.isFromSameSuitAndBigger(getRival(player).getPlayedCard()) || !isTrumpCard(getRival(player).getPlayedCard()))) {
            if (getRival(player).getPlayedCard() == null) {
                if (player.getCards().findCard(Rank.Ace, game.getTrumpSuit()) != null) {
                    return result;
                }

                if (isBestSuitCardLeft(player, result)) {
                    return result;
                }

                final Card otherTen = getTheSameRankFromOtherSuit(player, result);

                if (otherTen != null) {
                    return otherTen;
                }
            }

            return result;
        }

        result = player.getCards().findCard(Rank.Ace, game.getTrumpSuit());
        if (result != null) {
            return result;
        }

        return null;
    }

    /**
     * Returns cards with same rank from different suit.
     * @param human AI player.
     * @param card which rank and different suit is searching for.
     * @return Card object instance or null.
     */
    private Card getTheSameRankFromOtherSuit(final Player aiPlayer, final Card card) {
        for (PackIterator iterator = aiPlayer.getCards().iterator(); iterator.hasNext();) {
            Card current = iterator.next();
            if (current.getRank().equals(card.getRank()) && !current.getSuit().equals(card.getSuit())) {
                return current;
            }
        }
        return null;
    }
}
