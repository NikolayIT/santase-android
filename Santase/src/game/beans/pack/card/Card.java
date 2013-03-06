/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.beans.pack.card;

import java.io.Serializable;

import game.beans.pack.card.rank.Rank;
import game.beans.pack.card.suit.Suit;

/**
 * Card class.
 * @autor Dimitar Karamanov
 */
public final class Card implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1912595385718674682L;

    /**
     * Card's Suit {Spade, Heart, Diamond, Club}
     */
    private final Suit suit;

    /**
     * Card's Rank {9, 10, J, Q, K, A}
     */
    private final Rank rank;

    /**
     * HashValue
     */
    private volatile int hash;

    /**
     * Constructor.
     * @param suit - card's suit
     * @param rank - card's rank
     */
    public Card(final Suit suit, final Rank rank) {
        this.suit = suit;
        this.rank = rank;
        hash = Rank.getRankCount() * suit.getSuitOrder() + rank.getRankOrder();
    }

    /**
     * The method return card's points.
     * @param card instance.
     * @return int card's points as a integer value.
     */
    public static int getPoints(final Card card) {
        if (card == null) {
            return 0;
        }

        return card.getRank().getPoints();
    }

    /**
     * Returns card's rank.
     * @return Rank the card's rank.
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Returns card's suit.
     * @return Suit the card's suit.
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Compares this card with the specified object(card) for order.
     * @param obj specified object (card).
     * @return int value which may be: = 0 if this card and the specified object(card) are equal > 0 if this card is bigger than the specified object(card) < 0
     *         if this card is less than the specified object(card)
     */
    public int compareTo(Object obj) {
        Card compCard = (Card) obj;
        return hash - compCard.hash;
    }

    /**
     * The method checks if this card and specified object (card) are equal.
     * @param obj specified object.
     * @return boolean true if this card is equal to specified object and false otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Card) {
            Card card = (Card) obj;
            return suit.equals(card.suit) && rank.equals(card.rank);
        }
        return false;
    }

    /**
     * The method returns card's hash code.
     * @return int card's hash code value.
     */
    public int hashCode() {
        return hash;
    }

    /**
     * Returns a string representation of the object.
     * @return String a string representation of the object.
     */
    public String toString() {
        return rank.toString() + " " + suit.toString();
    }

    /**
     * Checks if this card rank is Rank.Queen or Rank.King.
     * @return boolean true if this card rank is Rank.Queen or Rank.King false otherwise.
     */
    public boolean isKingOrQueen() {
        return rank.equals(Rank.King) || rank.equals(Rank.Queen);
    }

    /**
     * Checks if this card and specified card are from same suit and this card rank is bigger then specified card's rank.
     * @param card with which is checked
     * @return boolean true if this card and specified card suit are equal and this card rank is bigger than specified card's rank false otherwise.
     */
    public boolean isFromSameSuitAndBigger(Card card) {
        return card == null || (card.getSuit().equals(suit) && rank.compareTo(card.getRank()) > 0);
    }
}
