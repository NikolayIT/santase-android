/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.beans.pack.card.suit;

/**
 * Suit class Represents Card's suit.
 *
 * @author Dimitar Karamanov
 * @see Card
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public final class Suit implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 5752919335092570107L;

	/**
     * Suit's constants.
     */
    private final static int CLUB = 0;

    private final static int DIAMOND = 1;

    private final static int HEART = 2;

    private final static int SPADE = 3;

    /**
     * Suit constant objects.
     */
    public final static Suit Club = new Suit(CLUB);

    public final static Suit Diamond = new Suit(DIAMOND);

    public final static Suit Heart = new Suit(HEART);

    public final static Suit Spade = new Suit(SPADE);

    private final static ArrayList<Suit> suitList = new ArrayList<Suit>();

    static{
        suitList.add(Club);
        suitList.add(Diamond);
        suitList.add(Heart);
        suitList.add(Spade);
    }
    
    /**
     * Internal suit constant.
     */
    private final int suit;

    /**
     * Constructor.
     * @param suit suit constant.
     * @param name suit name.
     * @param image suit image.
     */
    private Suit(final int suit) {
        this.suit = suit;
    }

    /**
     * Returns suit's order.
     * @return int suit's order.
     */
    public int getSuitOrder() {
        return suit;
    }

    /**
     * Returns hash code.
     * @return int hash code.
     */
    public int hashCode() {
        return suit;
    }

    /**
     * Returns suit's count (4).
     * @return suit's count.
     */
    public static int getSuitCount() {
        return suitList.size();
    }

    /**
     * The method checks if this Suit and specified object (Suit) are equal.
     *
     * @param obj specified object.
     * @return boolean true if this Suit is equal to specified object and false otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Suit) {
            return suit == ((Suit) obj).suit;
        }

        return false;
    }

    /**
     * Returns suit iterator.
     *
     * @return SuitIterator iterator.
     */
    public static SuitIterator iterator() {
        return new SuitIteratorImpl(suitList.iterator());
    }

    /**
     * SuitIteratorImpl class.
     * Implements SuitIterator interface.
     */
    private static class SuitIteratorImpl implements SuitIterator {
    	
        /**
         * The internal collection enumerator.
         */
        private final Iterator<Suit> enumeration;

        /**
         * Constructor.
         *
         * @param enumerator the internal collection enumerator.
         */
        private SuitIteratorImpl(final Iterator<Suit> enumeration) {
            this.enumeration = enumeration;
        }

        /**
         * Returns true if the iteration has more elements.
         * @return boolean true if the iteration has more elements false otherwise.
         */
        public boolean hasNext() {
            return enumeration.hasNext();
        }

        /**
         * Returns the next element in the iteration.
         * @return Suit the next element in the iteration.
         */
        public Suit next() {
            return enumeration.next();
        }
    }
}
