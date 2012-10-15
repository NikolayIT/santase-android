/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.beans.pack.card.rank;

import game.beans.pack.card.Card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Rank class Represents card's rank which has one of the following values 9, 10, J, Q, K, A.
 * 
 * @author Dimitar Karamanov
 * @see Card
 */
public final class Rank implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3769538778938708716L;

    /**
     * Ranks constants.
     */
    private static final int NINE = 0;

    private static final int JACK = 1;

    private static final int QUEEN = 2;

    private static final int KING = 3;

    private static final int TEN = 4;

    private static final int ACE = 5;

    /**
     * Rank's long name.
     */
    private final String longName;

    /**
     * Rank's short name.
     */
    private final String shortName;

    /**
     * Rank's points.
     */
    private final int points;

    /**
     * Ranks objects.
     */
    public static final Rank Nine = new Rank(NINE, "Nine", "9", 0);

    public static final Rank Jack = new Rank(JACK, "Jack", "J", 2);

    public static final Rank Queen = new Rank(QUEEN, "Queen", "Q", 3);

    public static final Rank King = new Rank(KING, "King", "K", 4);

    public static final Rank Ten = new Rank(TEN, "Ten", "10", 10);

    public static final Rank Ace = new Rank(ACE, "Ace", "A", 11);

    private final static ArrayList<Rank> rankList = new ArrayList<Rank>();

    static {
        rankList.add(Nine);
        rankList.add(Jack);
        rankList.add(Queen);
        rankList.add(King);
        rankList.add(Ten);
        rankList.add(Ace);
    }
    /**
     * Internal ID
     */
    private final int rank;

    /**
     * Constructor.
     * 
     * @param rank internal rank value.
     * @param longName long name.
     * @param shortName short name.
     * @param points rank's points.
     */
    private Rank(final int rank, final String longName, final String shortName, final int points) {
        this.rank = rank;
        this.longName = longName;
        this.shortName = shortName;
        this.points = points;
    }

    /**
     * Returns a string representation of the object.
     * 
     * @return String a string representation of the object.
     */
    public String toString() {
        return longName;
    }

    /**
     * Returns a rank's string sign representation.
     * 
     * @return String a rank's string sign representation.
     */
    public String getRankSign() {
        return shortName;
    }

    /**
     * Returns hash code.
     * 
     * @return hash code.
     */
    public int hashCode() {
        return rank;
    }

    /**
     * The method checks if this rank and specified object (rank) are equal.
     * 
     * @param obj specified object.
     * @return boolean true if this rank is equal to specified object and false otherwise.
     */
    public boolean equals(final Object obj) {
        if (obj instanceof Rank) {
            return rank == ((Rank) obj).rank;
        }
        return false;
    }

    /**
     * Compares this rank with the specified rank for order using standard rank's value.
     * 
     * @param obj specified object.
     * @return int value which may be: = 0 if this rank and the specified rank are equal > 0 if this rank is bigger than the specified rank < 0 if this rank is
     *         less than the specified rank
     */
    public int compareTo(final Object obj) {
        final Rank compRank = (Rank) obj;
        return rank - compRank.rank;
    }

    /**
     * Returns rank points.
     * 
     * @return int rank's points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Returns rank order.
     * 
     * @return int rank's order.
     */
    public int getRankOrder() {
        return rank;
    }

    /**
     * Returns rank iterator.
     * 
     * @return RankIterator rank iterator.
     */
    public static RankIterator iterator() {
        return new RankIteratorImpl(rankList.iterator());
    }

    /**
     * Returns rank count (6).
     * 
     * @return int rank count.
     */
    public static int getRankCount() {
        return rankList.size();
    }

    /**
     * Returns next rank using rank order.
     * 
     * @param rank specified rank.
     * @return Rank next rank.
     */
    public static Rank getNextRank(final Rank rank) {
        for (final RankIterator rankIterator = iterator(); rankIterator.hasNext();) {
            final Rank compRank = rankIterator.next();
            if (compRank.compareTo(rank) == 1) {
                return compRank;
            }
        }

        return Rank.Ace;
    }

    /**
     * Returns previous rank using rank order.
     * 
     * @param rank specified rank.
     * @return Rank previous rank.
     */
    public static Rank getPreviousRank(final Rank rank) {
        for (final RankIterator rankIterator = iterator(); rankIterator.hasNext();) {
            final Rank compRank = rankIterator.next();
            if (compRank.compareTo(rank) == -1) {
                return compRank;
            }
        }

        return Rank.Nine;
    }

    /**
     * RankIteratorImpl class Implements RankIterator interface.
     */
    private static class RankIteratorImpl implements RankIterator {
        /**
         * The internal collection enumerator.
         */
        private final Iterator<Rank> enumeration;

        /**
         * Constructor.
         * 
         * @param enumerator the internal collection enumerator.
         */
        private RankIteratorImpl(final Iterator<Rank> enumeration) {
            this.enumeration = enumeration;
        }

        /**
         * Returns true if the iteration has more elements.
         * 
         * @return boolean true if the iteration has more elements false otherwise.
         */
        public boolean hasNext() {
            return enumeration.hasNext();
        }

        /**
         * Returns the next element in the iteration.
         * 
         * @return Rank the next element in the iteration.
         */
        public Rank next() {
            return enumeration.next();
        }
    }
}
