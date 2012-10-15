/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy.automat.base;

import game.beans.pack.card.Card;
import game.beans.Player;

/**
 * PlayCardMethod interface.
 * 
 * @author Dimitar Karamanov
 */
public interface PlayCardMethod {
    /**
     * Returns player's card.
     * 
     * @param player who is on turn.
     * @return Card object instance or null.
     */
    Card getPlayerCard(final Player player);
}
