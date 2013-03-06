/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.beans;

import java.io.Serializable;

import game.beans.pack.card.suit.Suit;
import game.beans.pack.card.suit.SuitIterator;

/**
 * Couples class.
 * @author Dimitar Karamanov
 */
public final class Couples implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -712912771062404563L;

    /**
     * Array couples count constant.
     */
    private static final int COUPLES_COUNT = Suit.getSuitCount();

    /**
     * Internal container.
     */
    private final boolean coupleSuit[] = new boolean[Couples.COUPLES_COUNT];

    /**
     * Internal copy container.
     */
    private final boolean copyCoupleSuit[] = new boolean[Couples.COUPLES_COUNT];

    /**
     * Constructor.
     */
    public Couples() {
        super();
    }

    /**
     * Sets couple for provided suit.
     * @param suit provided suit.
     */
    public void setCouple(final Suit suit) {
        coupleSuit[suit.getSuitOrder()] = true;
    }

    /**
     * Returns couples points.
     * @param trumpSuit trump suit.
     * @return int couples points.
     */
    public int getCouplePoints(final Suit trumpSuit) {
        int result = 0;
        for (SuitIterator suitIterator = Suit.iterator(); suitIterator.hasNext();) {
            result += getCouplePoints(suitIterator.next(), trumpSuit);
        }
        return result;
    }

    /**
     * Returns couples count.
     * @return int couples count.
     */
    public int getCoupleCount() {
        int result = 0;
        for (SuitIterator suitIterator = Suit.iterator(); suitIterator.hasNext();) {
            Suit suit = suitIterator.next();
            if (hasCouple(suit)) {
                result++;
            }
        }
        return result;
    }

    /**
     * Returns true if has couple from provided suit.
     * @param suit provided suit.
     * @return boolean true if has couple from provided suit.
     */
    public boolean hasCouple(final Suit suit) {
        return coupleSuit[suit.getSuitOrder()];
    }

    /**
     * Returns couple points for specified suit.
     * @param suit specified suit.
     * @return int couple points for specified suit.
     */
    private int getCouplePoints(final Suit suit, final Suit trumpSuit) {
        if (coupleSuit[suit.getSuitOrder()]) {
            return trumpSuit.equals(suit) ? 40 : 20;
        }
        return 0;
    }

    /**
     * Clears the couples points.
     */
    public void clear() {
        for (int suit = 0; suit < coupleSuit.length; suit++) {
            coupleSuit[suit] = false;
        }
    }

    /**
     * Copies state.
     */
    public void copyState() {
        for (int suit = 0; suit < coupleSuit.length; suit++) {
            copyCoupleSuit[suit] = coupleSuit[suit];
        }
    }

    /**
     * Restores state.
     */
    public void restoreState() {
        for (int suit = 0; suit < copyCoupleSuit.length; suit++) {
            coupleSuit[suit] = copyCoupleSuit[suit];
        }
    }
}