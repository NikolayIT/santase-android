/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.beans.pack;

import game.beans.pack.card.Card;
import game.beans.pack.card.rank.Rank;
import game.beans.pack.card.rank.RankIterator;
import game.beans.pack.card.suit.Suit;
import game.beans.pack.card.suit.SuitIterator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Pack class. Represents a collection of cards objects.
 * 
 * @author Dimitar Karamanov
 */
public final class Pack implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = -2712837552486272028L;

    /**
     * Shuffle count.
     */
    private static final int SHUFFLE_COUNT = 1000;

    /**
     * Internal container collection.
     */
    private final ArrayList<Card> cards = new ArrayList<Card>();

    /**
     * Cashed full pack.
     */
    private final static Pack fullPack = new Pack();
    
    private final static Random random = new Random();

    /**
     * Default constructor
     */
    public Pack() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param pack indicates if the pack will be filled with cards or empty
     */
    public Pack(Pack pack) {
        cards.addAll(pack.cards);
    }

    /**
     * Factory method which returns a new full pack.
     * 
     * @return Pack a full pack.
     */
    public static Pack createFullPack() {
        if (fullPack.isEmpty()) {
            fullPack.addAllCards();
        }
        return new Pack(fullPack);
    }

    /**
     * Clear and copy all elements from another pack.
     * 
     * @param pack a copy from pack.
     */
    public void fill(Pack pack) {
        cards.clear();
        cards.addAll(pack.cards);
    }

    /**
     * Returns true if the card is from couple, false otherwise.
     * 
     * @param card a checked card.
     * @return boolean true if the provided card is from a couple, false otherwise.
     */
    public boolean hasCouple(Card card) {
        if (card == null) {
            return false;
        }

        if (card.getRank().equals(Rank.Queen)) {
            return findCard(Rank.King, card.getSuit()) != null;
        }

        if (card.getRank().equals(Rank.King)) {
            return findCard(Rank.Queen, card.getSuit()) != null;
        }

        return false;
    }

    /**
     * Checks if the pack has a couple from provided suit.
     * 
     * @param suit checked suit.
     * @return boolean true if the pack has couple from the provided suit, false otherwise.
     */
    public boolean hasCouple(final Suit suit) {
        return findCard(Rank.Queen, suit) != null && findCard(Rank.King, suit) != null;
    }

    /**
     * Returns the number of cards with specified suit.
     * 
     * @param suit specified suit.
     * @return int the number of cards with specified suit.
     */
    public int getSuitCount(final Suit suit) {
        int result = 0;
        for (Card card : cards) {
            if (card.getSuit().equals(suit)) {
                result++;
            }
        }

        return result;
    }

    /**
     * Returns the number of the cards with the specified rank from the pack.
     * 
     * @param rank specified rank.
     * @return int the number of the cards with the specified rank from the pack.
     */
    public int getRankCount(final Rank rank) {
        int result = 0;
        for (Card card : cards) {
            if (card.getRank().equals(rank)) {
                result++;
            }
        }

        return result;
    }

    /**
     * Add all cards.
     */
    public void addAllCards() {
        cards.clear();
        for (final SuitIterator suitIterator = Suit.iterator(); suitIterator.hasNext();) {
            final Suit suit = suitIterator.next();
            for (final RankIterator rankIterator = Rank.iterator(); rankIterator.hasNext();) {
                final Rank rank = rankIterator.next();
                final Card card = new Card(suit, rank);
                cards.add(card);
            }
        }
    }

    /**
     * Shuffles the pack.
     */
    public void shuffle() {
        for (int i = 0; i < SHUFFLE_COUNT; i++) {
            int randomInt = random.nextInt();
            final int index = Math.abs(randomInt % cards.size());
            if (index != cards.size() - 1) {
                final Card indexCard = cards.remove(index);
                cards.add(indexCard);
            }
        }
    }

    /**
     * Returns a string representation of the object.
     * 
     * @return String a string representation of the object.
     */
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        for (Card card : cards) {
            if (sb.length() != 0) {
                sb.append(" : ");
            }
            sb.append(card.toString());
        }
        return sb.toString();
    }

    /**
     * Prints the pack.
     */
    public void printPack() {
        for (Card card : cards) {
            System.out.println(card);
        }
    }

    /**
     * Gets a card by index.
     * 
     * @param index of the card which to be returned.
     * @return Card a card instance.
     */
    public Card getCard(final int index) {
        return cards.get(index);
    }

    /**
     * Returns pack's size.
     * 
     * @return int the size of the pack.
     */
    public int getSize() {
        return cards.size();
    }

    /**
     * Returns true if the pack is empty false otherwise.
     * 
     * @return boolean true if the pack is empty false otherwise.
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Removes a card by index from the pack.
     * 
     * @param index of the card which to be removed.
     * @return Card the removed card.
     */
    public Card remove(final int index) {
        final Card result = (Card) cards.remove(index);
        return result;
    }

    /**
     * Removes the provided card from the pack.
     * 
     * @param card which to be removed.
     * @return Card the removed card.
     */
    public Card remove(final Card card) {
        final Card cardToRemove = findCard(card.getRank(), card.getSuit());
        if (cardToRemove != null) {
            cards.remove(cardToRemove);
            return cardToRemove;
        }
        return null;
    }

    /**
     * Adds the provided card to the pack.
     * 
     * @param card which to be added.
     */
    public void add(final Card card) {
        cards.add(card);
    }

    /**
     * Removes all cards from the pack, which are stored in the provided pack.
     * 
     * @param pack which cards will be removed from the current one.
     * @return boolean true if all cards were removed, false otherwise.
     */
    public boolean removeAll(final Pack pack) {
        boolean result = true;
        for (Card card : pack.cards) {
            final Card cardToRemove = findCard(card.getRank(), card.getSuit());
            if (cardToRemove == null) {
                result = false;
            } else {
                cards.remove(cardToRemove);
            }
        }

        return result;
    }

    /**
     * Clear the pack (Removes all cards from the pack).
     */
    public void clear() {
        cards.clear();
    }

    /**
     * Arranges the pack cards.
     */
    public void arrange() {
        boolean swap = true;
        for (int i = 0; i < cards.size() - 1 && swap; i++) {
            swap = false;

            for (int j = 0; j < cards.size() - 1 - i; j++) {
                final Card cPrev = getCard(j);
                final Card cNext = getCard(j + 1);
                if (cPrev.compareTo(cNext) < 0) {
                    swap = true;
                    cards.remove(j);
                    cards.remove(j);
                    cards.add(j, cNext);
                    cards.add(j + 1, cPrev);
                }
            }
        }
    }

    /**
     * Returns true if the pack has the next rank order card, otherwise false.
     * 
     * @param card which is checked.
     * @return boolean true if the pack has the next rank order card, otherwise false.
     */
    public boolean hasNextFromSameSuit(final Card card) {
        if (card.getRank().equals(Rank.Ace)) {
            return false;
        }
        return findCard(Rank.getNextRank(card.getRank()), card.getSuit()) != null;
    }

    /**
     * Returns true if the pack has the previous rank order card, otherwise false.
     * 
     * @param card base one.
     * @return boolean true if the pack has the previous rank order card, otherwise false.
     */
    public boolean hasPrevFromSameSuit(final Card card) {
        if (card.getRank().equals(Rank.Nine)) {
            return false;
        }
        return findCard(Rank.getPreviousRank(card.getRank()), card.getSuit()) != null;
    }

    /**
     * Returns specified by rank and suit card.
     * 
     * @param rank searching card's rank.
     * @param suit searching card's suit.
     * @return Card the searching card if the pack contains it, otherwise null.
     */
    public Card findCard(final Rank rank, final Suit suit) {
        for (Card currentCard : cards) {
            if (currentCard.getRank().equals(rank) && currentCard.getSuit().equals(suit)) {
                return currentCard;
            }
        }
        return null;
    }

    /**
     * Returns the card with the max rank from a specified suit.
     * 
     * @param suit the specified suit.
     * @return Card the max card or null.
     */
    public Card findMaxSuitCard(final Suit suit) {
        Card result = null;
        for (Card currentCard : cards) {
            if (currentCard.getSuit().equals(suit)) {
                if (result == null || result.getRank().compareTo(currentCard.getRank()) < 0) {
                    result = currentCard;
                }
            }
        }
        return result;
    }

    /**
     * Returns the card with the max rank smaller than a specified one.
     * 
     * @param card the specified card.
     * @return Card the max card or null.
     */
    public Card findMaxUnderCard(final Card card) {
        Card result = null;
        for (Card currentCard : cards) {
            if (currentCard.getSuit().equals(card.getSuit()) && currentCard.getRank().compareTo(card.getRank()) < 0) {
                if (result == null || result.getRank().compareTo(currentCard.getRank()) < 0) {
                    result = currentCard;
                }
            }
        }
        return result;
    }

    /**
     * Returns the card with the min rank bigger than a specified one.
     * 
     * @param card the specified card.
     * @return Card the min card or null.
     */
    public Card findMinAboveCard(final Card card) {
        Card result = null;
        for (Card currentCard : cards) {
            if (currentCard.getSuit().equals(card.getSuit()) && currentCard.getRank().compareTo(card.getRank()) > 0) {
                if (result == null || result.getRank().compareTo(currentCard.getRank()) > 0) {
                    result = currentCard;
                }
            }
        }
        return result;
    }

    /**
     * Returns the card with the min rank from a specified suit.
     * 
     * @param suit the specified suit.
     * @return Card the min card or null.
     */
    public Card findMinSuitCard(final Suit suit) {
        Card result = null;
        for (Card currentCard : cards) {
            if (currentCard.getSuit().equals(suit)) {
                if (result == null || result.getRank().compareTo(currentCard.getRank()) > 0) {
                    result = currentCard;
                }
            }
        }
        return result;
    }

    /**
     * Returns the card with the min rank from all cards.
     * 
     * @return Card the min card or null.
     */
    public Card findMinAllCard() {
        Card result = null;
        for (Card currentCard : cards) {
            if (result == null || result.getRank().compareTo(currentCard.getRank()) > 0) {
                result = currentCard;
            }
        }
        return result;
    }

    /**
     * Returns the pack iterator.
     * 
     * @return PackIterator the pack iterator.
     */
    public PackIterator iterator() {
        return new PackIteratorImpl(cards.iterator());
    }

    /**
     * PackIteratorImpl class. Implements PackIterator interface.
     */
    private static class PackIteratorImpl implements PackIterator {
        /**
         * The internal collection enumerator.
         */
        private final Iterator<Card> enumeration;

        /**
         * Constructor.
         * 
         * @param enumerator the internal collection enumerator.
         */
        private PackIteratorImpl(final Iterator<Card> enumeration) {
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
         * @return Card the next element in the iteration.
         */
        public Card next() {
            return enumeration.next();
        }
    }
}
