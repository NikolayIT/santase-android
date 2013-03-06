/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy.automat.executors.base;

import game.logic.strategy.automat.base.PlayCardMethod;

/**
 * PlayCardMethodIterator interface.
 * @author Dimitar Karamanov
 */
public interface PlayCardMethodIterator {

    /**
     * Returns true if the iteration has more elements.
     * @return boolean true if the iteration has more elements false otherwise.
     */
    boolean hasNext();

    /**
     * Returns the next element in the iteration.
     * @return PlayCardMethod the next element in the iteration.
     */
    PlayCardMethod next();
}
