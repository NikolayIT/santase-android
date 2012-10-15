/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy.automat.methods.base;

import game.beans.Game;
import game.beans.Player;
import game.beans.pack.Pack;
import game.beans.pack.PackIterator;
import game.beans.pack.card.Card;
import game.beans.pack.card.rank.Rank;
import game.beans.pack.card.rank.RankIterator;
import game.beans.pack.card.suit.Suit;
import game.beans.pack.card.suit.SuitIterator;
import game.logic.SantaseGame;
import game.logic.strategy.automat.base.PlayCardMethod;

/**
 * BaseMethod class. Based class of all AI methods. Contains several helper methods used in the concrete methods.
 * 
 * @author Dimitar Karamanov
 */
public abstract class BaseMethod implements PlayCardMethod {
    /**
     * Belot game internal object.
     */
    protected final Game game;

    /**
     * Constructor.
     * 
     * @param game SantaseGame instance.
     */
    public BaseMethod(final Game game) {
        this.game = game;
    }

    /**
     * Returns player's card.
     * 
     * @param player who is on turn.
     * @param opposite player.
     * @return Card object instance or null.
     */
    public final Card getPlayerCard(final Player player) {
        final Card result = getPlayMethodCard(player);
        // Handle event.
        return result;
    }

    /**
     * Returns player's card.
     * 
     * @param player AI player.
     * @param opposite player.
     * @return Card object instance or null.
     */
    protected abstract Card getPlayMethodCard(final Player player);

    /**
     * Returns eventual couple points.
     * 
     * @param pack players plack.
     * @return 0 or the max eventual couple points.
     */
    protected int eventualCouplePoints(Pack pack) {
        int result = 0;

        for (SuitIterator suitIterator = Suit.iterator(); suitIterator.hasNext();) {
            final Suit suit = suitIterator.next();
            if (pack.hasCouple(suit)) {
                final int points = getCouplePoints(suit);
                if (points > result) {
                    result = points;
                }
            }
        }
        return result;
    }

    /**
     * Returns a pack with possible enemy cards.
     * 
     * @param player AI player.
     * @param opposite player.
     * @param withLastCard
     * @return a pack with possible enemy cards.
     */
    protected Pack possibleEnemyCards(final Player player, boolean withLastCard) {
        final Pack result = Pack.createFullPack();
        result.removeAll(getRival(player).getHands());
        result.removeAll(player.getHands());
        result.removeAll(player.getCards());
        if (withLastCard && game.getGameCards().getSize() > 0) {
            result.remove(game.getGameCards().getCard(game.getGameCards().getSize() - 1));
        }
        return result;
    }

    /**
     * Returns true if has power trumps, false otherwise.
     * 
     * @param player AI player.
     * @param opposite player.
     * @param withLastCard
     * @return true if has power trumps, false otherwise.
     */
    protected boolean hasPowerTrumps(final Player player, boolean withLastCard) {
        final Pack possibleCards = possibleEnemyCards(player, withLastCard);
        final Pack copy = new Pack(player.getCards());

        Card playerTrumpCard = null;
        Card oppositeTrumpCard = null;

        do {
            playerTrumpCard = copy.findMaxSuitCard(game.getTrumpSuit());
            oppositeTrumpCard = possibleCards.findMaxSuitCard(game.getTrumpSuit());

            if (playerTrumpCard == null) {
                return oppositeTrumpCard == null;
            }

            if (oppositeTrumpCard == null) {
                return true;
            }

            if (oppositeTrumpCard.getRank().compareTo(playerTrumpCard.getRank()) > 0) {
                return false;
            }

            copy.remove(playerTrumpCard);
            possibleCards.remove(oppositeTrumpCard);
        } while (playerTrumpCard != null);

        return true;
    }

    /**
     * Returns if the provided card is from trump suit.
     * 
     * @param card provided one.
     * @return boolean true if the card is not null and trump false otherwise.
     */
    protected boolean isTrumpCard(Card card) {
        return card != null && card.getSuit().equals(game.getTrumpSuit());
    }

    /**
     * Returns if the card can be part of future couple or not.
     * 
     * @param player which plays the cards.
     * @param opposite player.
     * @param card the played one.
     * @return boolean true if is not expected, false otherwise.
     */
    protected boolean noPossibleCoupleCard(final Player player, final Card card) {
        Rank rank = null;
        if (card.getRank().equals(Rank.Queen)) {
            rank = Rank.King;
        }
        if (card.getRank().equals(Rank.King)) {
            rank = Rank.Queen;
        }
        if (rank == null) {
            return true;
        }

        boolean playerPlayed = player.getHands().findCard(rank, card.getSuit()) != null;
        boolean oppositePlayed = getRival(player).getHands().findCard(rank, card.getSuit()) != null;
        boolean oppositePlayedCard = getRival(player).getPlayedCard() != null && getRival(player).getPlayedCard().getRank().equals(rank)
                && getRival(player).getPlayedCard().getSuit().equals(card.getSuit());

        return playerPlayed || oppositePlayed || oppositePlayedCard;
    }

    /**
     * Returns true if can play without loose (check opposite player)
     * @param opposite player.
     * @param card played by AI player.
     * @return boolean true if can play without loose (check opposite player) or false.
     */
    protected boolean canPlayWithoutLoose(final Player opposite, final Card card) {
        return opposite.getPoints(game.getTrumpSuit()) + Card.getPoints(card) + Card.getPoints(opposite.getPlayedCard()) < SantaseGame.END_GAME_POINTS;
    }

    /**
     * Returns couples points for provided suit.
     * 
     * @param suit provided one.
     * @return couple points.
     */
    protected int getCouplePoints(final Suit suit) {
        return suit.equals(game.getTrumpSuit()) ? 40 : 20;
    }

    /**
     * Returns sure hand points.
     * 
     * @param playerPack AI player pack.
     * @param oppositePack opposite player pack.
     * @param hands hands pack.
     * @return sure hand points.
     */
    protected int sureHandsPoints(final Pack playerPack, final Pack oppositePack, final Pack hands) {
        int result = 0;

        for (SuitIterator suitIterator = Suit.iterator(); suitIterator.hasNext();) {
            final Suit suit = suitIterator.next();
            int singleHandPoints;
            while ((singleHandPoints = singleHandPoints(playerPack, oppositePack, hands, suit)) != 0) {
                result += singleHandPoints;
            }
        }

        return result;
    }

    /**
     * Returns a single hand points from provided suit.
     * 
     * @param playerPack AI player pack.
     * @param oppositePack opposite player pack.
     * @param hands pack.
     * @param suit provided one.
     * @return a single hand points from provided suit.
     */
    protected int singleHandPoints(final Pack playerPack, final Pack oppositePack, final Pack hands, final Suit suit) {
        int result = 0;
        final Card playerCard = playerPack.findMaxSuitCard(suit);
        Card oppositeCard = oppositePack.findMaxSuitCard(suit);

        if (playerCard != null && oppositeCard != null && playerCard.getRank().compareTo(oppositeCard.getRank()) > 0) {
            oppositeCard = oppositePack.findMinSuitCard(suit);
            result = Card.getPoints(playerCard) + Card.getPoints(oppositeCard);

            if (playerPack.hasCouple(suit) && playerCard.isKingOrQueen()) {
                result += getCouplePoints(suit);
            }

            playerPack.remove(playerCard);
            hands.add(playerCard);
            oppositePack.remove(oppositeCard);
            hands.add(oppositeCard);
        }

        return result;
    }

    /**
     * Returns points of hands when the opposite player has no trump suit cards.
     * 
     * @param playerPack AI player pack.
     * @param oppositePack opposite player pack.
     * @param hands pack.
     * @return points of hands when the opposite player has no trump suit cards.
     */
    protected int getHandsNoTrump(final Pack playerPack, final Pack oppositePack, final Pack hands) {
        int result = 0;
        if (oppositePack.findMinSuitCard(game.getTrumpSuit()) == null) {
            Pack meterCards = new Pack();
            for (PackIterator iterator = playerPack.iterator(); iterator.hasNext();) {
                final Card card = iterator.next();
                final Card oppositeCard = oppositePack.findMaxSuitCard(card.getSuit());
                if (oppositeCard == null) {
                    meterCards.add(card);
                }
            }

            for (PackIterator iterator = meterCards.iterator(); iterator.hasNext();) {
                final Card card = iterator.next();
                final Card oppositeSmallestCard = oppositePack.findMinAllCard();
                result += Card.getPoints(card) + Card.getPoints(oppositeSmallestCard);

                if (playerPack.hasCouple(card.getSuit()) && card.isKingOrQueen()) {
                    result += getCouplePoints(card.getSuit());
                }

                hands.add(playerPack.remove(card));
                oppositePack.remove(oppositeSmallestCard);
                hands.add(oppositeSmallestCard);
            }
        }
        return result;
    }

    /**
     * Returns if the provided card is the best suit card or not.
     * 
     * @param player AI player.
     * @param opposite player.
     * @param card which is checked.
     * @return boolean result.
     */
    protected boolean isBestSuitCardLeft(final Player player, Card card) {
        for (RankIterator iterator = Rank.iterator(); iterator.hasNext();) {
            final Rank rank = iterator.next();
            if (rank.compareTo(card.getRank()) > 0) {
                final Card last = game.getGameCards().getCard(game.getGameCards().getSize() - 1);
                final boolean isTheLastCard = last.getRank().equals(rank) && last.getSuit().equals(card.getSuit());

                if (!isTheLastCard && getRival(player).getHands().findCard(rank, card.getSuit()) == null
                        && player.getHands().findCard(rank, card.getSuit()) == null) {
                    return false;
                }
            }
        }

        return true;
    }

    protected Player getRival(Player player) {
        return game.getRival(player);
    }
}
