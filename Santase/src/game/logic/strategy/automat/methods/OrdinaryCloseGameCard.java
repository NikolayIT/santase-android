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
import game.beans.pack.card.rank.Rank;
import game.logic.SantaseGame;
import game.logic.strategy.automat.executors.ClosedAttackCardExecutor;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * OrdinaryCloseGameCard class. PlayCardMethod which implements the logic of playing a card for closing the game.
 * 
 * @author Dimitar Karamanov
 */
public final class OrdinaryCloseGameCard extends BaseMethod {
    /**
     * 
     */
    private final static int POSSIBLE_MORE_POINTS = 6;

    /**
     * Constructor.
     * 
     * @param game SantaseGame instance.
     */
    public OrdinaryCloseGameCard(final Game game) {
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
        if (game.canClose() && isSuitableToClose(player)) {
            game.setClosedGame(player);
            return new ClosedAttackCardExecutor(game).getPlayerCard(player);
            // return game.getGameAdviser().getCard(player);
        }
        return null;
    }

    /**
     * Returns if the player can close the game.
     * 
     * @param player AI player.
     * @param opposite player.
     * @return boolean true if can, false otherwise.
     */
    private boolean isSuitableToClose(final Player player) {
        if (isSuitableToCloseByForty(player)) {
            return true;
        }

        if (hasPowerToClose(player)) {
            return true;
        }

        if (hasLastCloseChance(player)) {
            return true;
        }

        return false;
    }

    /**
     * Returns if the player can close the game by power forty cards.
     * 
     * @param human AI player.
     * @param opposite player.
     * @return boolean true if can, false otherwise.
     */
    private boolean isSuitableToCloseByForty(final Player player) {
        if (getRival(player).getPoints(game.getTrumpSuit()) < SantaseGame.POINTS_ZONE) {
            final boolean AceTrump = player.getCards().findCard(Rank.Ace, game.getTrumpSuit()) != null;
            final boolean TenTrump = player.getCards().findCard(Rank.Ten, game.getTrumpSuit()) != null;
            final boolean hasCouple = player.getCards().hasCouple(game.getTrumpSuit());

            int aceCount = player.getCards().getRankCount(Rank.Ace);

            if (AceTrump) {
                aceCount--;
            }

            if ((AceTrump || TenTrump) && hasCouple && aceCount > 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns if the player can close the game.
     * 
     * @param human AI player.
     * @param opposite player.
     * @return boolean true if can, false otherwise.
     */
    private boolean hasPowerToClose(final Player player) {
        return hasPowerTrumps(player, true) && getHandsPoints(player) >= (SantaseGame.END_GAME_POINTS - POSSIBLE_MORE_POINTS);
    }

    /**
     * Returns potentional hands points.
     * 
     * @param player AI player.
     * @param opposite player.
     * @return boolean true if can, false otherwise.
     */
    private int getHandsPoints(final Player player) {
        int result = 0;
        final Pack copyPack = new Pack(player.getCards());
        final Pack oppositePointsCards = possibleEnemyCards(player, true);
        final Pack oppositeSuitCards = possibleEnemyCards(player, true);

        int points = 0;
        while ((points = getSingleHandPoints(copyPack, oppositePointsCards, oppositeSuitCards)) > 0) {
            result += points;
        }

        result += eventualCouplePoints(player.getCards());
        result += player.getPoints(game.getTrumpSuit());

        return result;
    }

    /**
     * Returns a single hand minimum possible points.
     * 
     * @param copyPack copy pack of the AI player.
     * @param oppositePointsCards copy pack of the opposite player - used for minimum points card calculation.
     * @param oppositeSuitCards copy pack of the opposite player.
     * @return a single hand minimum possible points.
     */
    private int getSingleHandPoints(final Pack copyPack, final Pack oppositePointsCards, final Pack oppositeSuitCards) {
        for (PackIterator iterator = copyPack.iterator(); iterator.hasNext();) {
            final Card card = iterator.next();
            final Card bestSuitCard = copyPack.findMaxSuitCard(card.getSuit());
            if (card.equals(bestSuitCard)) {
                final Card bestOppositeSuitCard = oppositeSuitCards.findMaxSuitCard(card.getSuit());

                if ((bestOppositeSuitCard == null) || (bestSuitCard.compareTo(bestOppositeSuitCard) > 0)) {
                    final Card smallestCard = oppositePointsCards.findMinAllCard();

                    copyPack.remove(bestSuitCard);

                    final Card smallestSuitCard = oppositeSuitCards.findMinSuitCard(card.getSuit());
                    if (smallestSuitCard != null) {
                        oppositeSuitCards.remove(smallestSuitCard);
                    }

                    if (smallestCard != null) {
                        oppositePointsCards.remove(smallestCard);
                    }

                    return Card.getPoints(smallestCard) + Card.getPoints(bestSuitCard);
                }
            }
        }
        return 0;
    }

    /**
     * Returns true if the player has last close chance or not.
     * 
     * @param human AI player.
     * @param opposite player.
     * @return boolean true if has, false otherwise.
     */
    private boolean hasLastCloseChance(final Player player) {
        return getHandsPointsLastChance(player) >= SantaseGame.END_GAME_POINTS;
    }

    /**
     * Returns the potental points if closed the game on last chance.
     * 
     * @param player AI player.
     * @param opposite player.
     * @return the potental points if closed the game on last chance.
     */
    private int getHandsPointsLastChance(final Player player) {
        int result = 0;
        final Pack copyPack = new Pack(player.getCards());
        final Pack possibleOppositeCards = possibleEnemyCards(player, true);

        int points = 0;
        while ((points = getSingleHandPointsLastChance(copyPack, possibleOppositeCards)) > 0) {
            result += points;
        }

        result += eventualCouplePoints(player.getCards());
        result += player.getPoints(game.getTrumpSuit());

        return result;
    }

    /**
     * Returns the potential points if closed the game on last chance.
     * 
     * @param copyPack copy pack of the player.
     * @param possibleOppositeCards possible opposite player cards.
     * @return the potential points of single hand.
     */
    private int getSingleHandPointsLastChance(final Pack copyPack, final Pack possibleOppositeCards) {
        for (PackIterator iterator = copyPack.iterator(); iterator.hasNext();) {
            final Card card = iterator.next();
            final Card bestSuitCard = copyPack.findMaxSuitCard(card.getSuit());
            if (card == bestSuitCard) {
                final Card bestOppositeSuitCard = possibleOppositeCards.findMaxSuitCard(card.getSuit());
                final int enemySuitCount = possibleOppositeCards.getSuitCount(card.getSuit());

                if ((bestOppositeSuitCard != null) && (bestSuitCard.compareTo(bestOppositeSuitCard) > 0) && (enemySuitCount > 2)) {
                    Card smallestOfSuitCard = possibleOppositeCards.findMinSuitCard(card.getSuit());

                    copyPack.remove(bestSuitCard);

                    if (smallestOfSuitCard != null) {
                        possibleOppositeCards.remove(smallestOfSuitCard);
                    }

                    return Card.getPoints(smallestOfSuitCard) + Card.getPoints(bestSuitCard);
                }
            }
        }
        return 0;
    }
}
