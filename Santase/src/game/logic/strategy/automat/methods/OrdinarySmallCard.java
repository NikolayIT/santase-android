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
import game.logic.SantaseGame;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * OrdinarySmallCard class. PlayCardMethod which implements the logic of playing a small card.
 * 
 * @author Dimitar Karamanov
 */
public final class OrdinarySmallCard extends BaseMethod {
    /**
     * Constructor.
     * 
     * @param game SantaseGame instance.
     */
    public OrdinarySmallCard(final Game game) {
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
        for (PackIterator iterator = player.getCards().iterator(); iterator.hasNext();) {
            final Card card = iterator.next();
            if (!card.getRank().equals(Rank.Ace) && !card.getRank().equals(Rank.Ten) && !card.getSuit().equals(game.getTrumpSuit())) {
                if (player.getCards().hasCouple(card)) {
                    continue;
                }

                if (result == null || result.getRank().compareTo(card.getRank()) > 0) {
                    result = card;
                }
            }
        }

        // Ako ne e koz i e Pop i Imame damata ot sastata boja ja davame neja
        // !!!
        if (getRival(player).getPlayedCard() != null && getRival(player).getPlayedCard().getRank().equals(Rank.King) && result != null
                && result.getRank().equals(Rank.Queen) && !getRival(player).getPlayedCard().getSuit().equals(game.getTrumpSuit())) {
            final Card queen = player.getCards().findCard(Rank.Queen, getRival(player).getPlayedCard().getSuit());
            if (queen != null) {
                result = queen;
            }
        }

        // KOLIZIJA !!!
        if (getRival(player).getPoints(game.getTrumpSuit()) < SantaseGame.END_GAME_POINTS) {
            if (result != null
                    && getRival(player).getPoints(game.getTrumpSuit()) + Card.getPoints(result) + Card.getPoints(getRival(player).getPlayedCard()) >= SantaseGame.END_GAME_POINTS) {
                final Card smallest = player.getCards().findMinAllCard();
                if (smallest != null && smallest.getRank().compareTo(result.getRank()) < 0) {
                    return null;
                }
            }
        }

        result = checkForExitResponse(getRival(player), result);
        return result;
    }

    /**
     * Check if the player has enough points to stop the game.
     * 
     * @param player AI player.
     * @param card played one.
     * @return Card object.
     */
    private Card checkForExitResponse(final Player player, final Card card) {
        if (player.getPoints(game.getTrumpSuit()) < SantaseGame.END_GAME_POINTS) {
            if (player.getPoints(game.getTrumpSuit()) + Card.getPoints(card) + Card.getPoints(player.getPlayedCard()) >= SantaseGame.END_GAME_POINTS) {
                return null;
            }
        }
        return card;
    }
}