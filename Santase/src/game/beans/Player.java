/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.beans;

import java.io.Serializable;

import game.beans.pack.Pack;
import game.beans.pack.card.Card;
import game.beans.pack.card.suit.Suit;

/**
 * GamePlayer class.
 * @author Dimitar Karamanov
 */
public final class Player implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -617238319488864541L;

    /**
     * Player's cards
     */
    private final Pack cards = new Pack();

    /**
     * Player's hands
     */
    private final Pack hands = new Pack();

    /**
     * Player's copy cards
     */
    private final Pack copyCards = new Pack();

    /**
     * Player's copy hands
     */
    private final Pack copyHands = new Pack();

    /**
     * Player's couples
     */
    private final Couples couples = new Couples();

    /**
     * Player's wins little games
     */
    private int littleGames = 0;

    /**
     * Player's wins big games
     */
    private int bigGames = 0;

    /**
     * Player's played card
     */
    private Card playedCard = null;

    /**
     * Player's copy played card
     */
    private Card copyPlayedCard;

    private Card selectedCard;

    /**
     * Cards count constant
     */
    private final static int CARD_COUNT = 6;

    /**
     * Initialization of new game
     * @param gameCards pack
     */
    public void newGame(final Pack gameCards) {
        getCards().clear();
        getHands().clear();
        getCouples().clear();
        for (int i = 0; i < CARD_COUNT; i++) {
            getCards().add(gameCards.remove(0));
        }
        getCards().arrange();
        setPlayedCard(null);
    }

    /**
     * Copy internal state.
     */
    public void copyState() {
        copyCards.fill(getCards());
        copyHands.fill(getHands());
        getCouples().copyState();
        copyPlayedCard = getPlayedCard();
    }

    /**
     * Restore internal state.
     */
    public void restoreState() {
        getCards().fill(copyCards);
        getHands().fill(copyHands);
        getCouples().restoreState();
        setPlayedCard(copyPlayedCard);
    }

    /**
     * Returns player points.
     * @param trumpSuit trump suit
     * @return players points
     */
    public int getPoints(final Suit trumpSuit) {
        int result = 0;
        for (int i = 0; i < getHands().getSize(); i++) {
            result += Card.getPoints(getHands().getCard(i));
        }
        if (trumpSuit != null && getHands().getSize() > 0) {
            result += getCouples().getCouplePoints(trumpSuit);
        }
        return result;
    }

    /**
     * Return true if the player has a couple of a provided suit false otherwise.
     * @param suit which will be cheked for a couple
     * @return boolean true or false
     */
    public boolean hasCouple(final Suit suit) {
        return getCards().hasCouple(suit);
    }

    public Pack getCards() {
        return cards;
    }

    public Pack getHands() {
        return hands;
    }

    public Couples getCouples() {
        return couples;
    }

    public int getLittleGames() {
        return littleGames;
    }

    public void setLittleGames(int littleGames) {
        this.littleGames = littleGames;
    }

    public int getBigGames() {
        return bigGames;
    }

    public void setBigGames(int bigGames) {
        this.bigGames = bigGames;
    }

    public Card getPlayedCard() {
        return playedCard;
    }

    public void setPlayedCard(Card playedCard) {
        this.playedCard = playedCard;
    }

    /**
     * @return the humanSelectedCard
     */
    public Card getSelectedCard() {
        return selectedCard;
    }

    /**
     * Sets player selected card.
     * @param selectedCard the player selected one.
     */
    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
    }
}
