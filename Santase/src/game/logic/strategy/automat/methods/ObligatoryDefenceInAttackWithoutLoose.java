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
import game.logic.SantaseGame;
import game.logic.strategy.automat.base.PlayCardMethod;
import game.logic.strategy.automat.executors.ObligatoryDefenceCardExecutor;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * ObligatoryDefenceInAttackWithoutLoose class. PlayCardMethod which implements the logic of playing a obligatory defense in attack card whithout loose.
 * 
 * @author Dimitar Karamanov
 */
public final class ObligatoryDefenceInAttackWithoutLoose extends BaseMethod {
    /**
     * Help play card method.
     */
    private final PlayCardMethod obligatoryForceToPlayTrump;

    /**
     * Help play card method.
     */
    private final PlayCardMethod obligatoryFindSingleLoose;

    /**
     * Help play card method.
     */
    private final PlayCardMethod obligatoryDefenceCardExecutor;

    /**
     * Constructor.
     * 
     * @param game SantaseGame instance.
     */
    public ObligatoryDefenceInAttackWithoutLoose(final Game game) {
        super(game);
        obligatoryForceToPlayTrump = new ObligatoryForceToPlayTrump(game);
        obligatoryFindSingleLoose = new ObligatorySingleLooser(game);
        obligatoryDefenceCardExecutor = new ObligatoryDefenceCardExecutor(game);
    }

    /**
     * Returns player's card.
     * 
     * @param player AI player.
     * @param opposite player.
     * @return Card object instance or null.
     */
    protected Card getPlayMethodCard(final Player player) {
        final Card result = getDefenseInAttack(player);

        if (result != null && canPlayObligatoryAttackWithoutLoose(player, result)) {
            return result;
        }
        return null;
    }

    /**
     * Returns defence card on attack player.
     * 
     * @param player AI player.
     * @param opposite player.
     * @return defence in attack card.
     */
    private Card getDefenseInAttack(final Player player) {
        Card result = null;
        if ((result = obligatoryForceToPlayTrump.getPlayerCard(player)) != null) {
            return result;
        }

        // Vzima ot boja kadeto naj-goljamata e po < ot min ot bojata
        for (PackIterator iterator = player.getCards().iterator(); iterator.hasNext();) {
            final Card card = iterator.next();
            final Card oppositeCard = getRival(player).getCards().findMinSuitCard(card.getSuit());
            final Card playerCard = player.getCards().findMaxSuitCard(card.getSuit());

            if (playerCard != null && oppositeCard != null && oppositeCard.getRank().compareTo(playerCard.getRank()) > 0) {
                return player.getCards().findMinSuitCard(card.getSuit());
            }
        }

        // DA NAMIRA naj-malkata kojato e dvojna (sirech ima i sledvastata ot
        // sastata boja) (Pack hasNextSuit hasPrevSuit
        for (PackIterator iterator = player.getCards().iterator(); iterator.hasNext();) {
            final Card card = iterator.next();
            if (player.getCards().hasNextFromSameSuit(card)) {
                if (result == null || result.getRank().compareTo(card.getRank()) > 0) {
                    result = card;
                }
            }
        }

        if (result == null) {
            result = obligatoryFindSingleLoose.getPlayerCard(player);
        }

        return result;
    }

    /**
     * Returns true if can play without game loose, false otherwise.
     * 
     * @param player AI player.
     * @param opposite player.
     * @param card played one.
     * @return true if can play without game loose, false otherwise.
     */
    private boolean canPlayObligatoryAttackWithoutLoose(final Player player, final Card card) {
        if (!getRival(player).getCards().isEmpty()) {
            game.copyState();
            try {
                player.setPlayedCard(card);

                final Card futureCard = obligatoryDefenceCardExecutor.getPlayerCard(getRival(player));

                if ((futureCard.getSuit().equals(card.getSuit()) && futureCard.getRank().compareTo(card.getRank()) > 0)
                        || futureCard.getSuit().equals(game.getTrumpSuit())) {
                    final int eventCouple = eventualCouplePoints(getRival(player).getCards());
                    int futurePoints = Card.getPoints(futureCard) + Card.getPoints(card) + eventCouple;

                    if (futurePoints + getRival(player).getPoints(game.getTrumpSuit()) >= SantaseGame.END_GAME_POINTS) {
                        return false;
                    }

                    player.getCards().remove(card);
                    getRival(player).getCards().remove(futureCard);

                    final Pack hands = new Pack();
                    futurePoints += sureHandsPoints(getRival(player).getCards(), player.getCards(), hands);
                    futurePoints += getHandsNoTrump(getRival(player).getCards(), player.getCards(), hands);

                    return futurePoints + getRival(player).getPoints(game.getTrumpSuit()) < SantaseGame.END_GAME_POINTS;
                }
                return true;
            } finally {
                game.restoreState();
            }
        }
        return false;
    }
}
