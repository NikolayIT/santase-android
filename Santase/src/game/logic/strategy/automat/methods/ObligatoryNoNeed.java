/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy.automat.methods;

import game.beans.Game;
import game.beans.Player;
import game.beans.pack.PackIterator;
import game.beans.pack.card.Card;
import game.logic.strategy.automat.base.PlayCardMethod;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * ObligatoryNoNeed class. PlayCardMethod which implements the logic of playing a card ....
 * 
 * @author Dimitar Karamanov
 */
public final class ObligatoryNoNeed extends BaseMethod {
    
    private final PlayCardMethod obligatoryFindSingleLoose;

    /**
     * Constructor.
     * 
     * @param game SantaseGame instance.
     */
    public ObligatoryNoNeed(final Game game) {
        super(game);
        obligatoryFindSingleLoose = new ObligatorySingleLooser(game);
    }

    /**
     * Returns player's card.
     * 
     * @param player AI player.
     * @param opposite player.
     * @return Card object instance or null.
     */
    protected Card getPlayMethodCard(final Player player) {
        Card result = getLooserCard(player);
        if (result == null) {
            result = obligatoryFindSingleLoose.getPlayerCard(player);
        }
        return canPlayWithoutLoose(getRival(player), result) ? result : null;
    }

    /**
     * Returns a looser card.
     * 
     * @param player AI player.
     * @param opposite player.
     * @return Card object instance or null.
     */
    private Card getLooserCard(final Player player) {
        Card result = null;
        for (PackIterator iterator = player.getCards().iterator(); iterator.hasNext();) {
            final Card card = iterator.next();
            final Card playerCard = player.getCards().findMaxSuitCard(card.getSuit());
            final Card oppositeCard = getRival(player).getCards().findMaxSuitCard(card.getSuit());
            if (oppositeCard == null || oppositeCard.getRank().compareTo(playerCard.getRank()) < 0) {
                if (result == null || result.getRank().compareTo(card.getRank()) > 0) {
                    result = card;
                }
            }
        }
        return result;
    }
}
