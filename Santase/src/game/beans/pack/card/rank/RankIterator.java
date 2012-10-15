/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.beans.pack.card.rank;

/**
 * RankIterator interface.
 * 
 * @author Dimitar Karamanov
 */
public interface RankIterator {
    /**
     * Returns true if the iteration has more elements.
     * 
     * @return boolean true if the iteration has more elements false otherwise
     */
    boolean hasNext();

    /**
     * Returns the next element in the iteration.
     * 
     * @return Rank the next element in the iteration.
     */
    Rank next();
}
