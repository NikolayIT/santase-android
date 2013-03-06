/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy.automat.methods;

import game.beans.Game;
import game.beans.Player;
import game.beans.pack.card.Card;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * ObligatoryBiggerCard class. PlayCardMethod which implements the logic of playing a obligatory bigger card.
 * @author Dimitar Karamanov
 */
public final class ObligatoryBiggerCard extends BaseMethod {

    /**
     * Constructor.
     * @param game SantaseGame instance.
     */
    public ObligatoryBiggerCard(final Game game) {
        super(game);
    }

    /**
     * Returns player's card.
     * @param player AI player.
     * @param opposite player.
     * @return Card object instance or null.
     */
    protected Card getPlayMethodCard(final Player player) {
        Card result;
        final Card rivalCard = getRival(player).getPlayedCard();

        if (rivalCard != null) {
            if ((result = player.getCards().findMaxSuitCard(rivalCard.getSuit())) == null) {
                return null;
            }

            if (getRival(player).getCards().getSuitCount(rivalCard.getSuit()) == 0) {
                if (result.getRank().compareTo(rivalCard.getRank()) > 0) {
                    return result;
                }
            }

            if (getRival(player).getCards().getSuitCount(rivalCard.getSuit()) >= ONE_CARD) {
                if (result.getRank().compareTo(getRival(player).getCards().findMaxSuitCard(getRival(player).getPlayedCard().getSuit()).getRank()) > 0) {
                    final Card bcSecond = player.getCards().findMaxUnderCard(result);
                    if (bcSecond != null && bcSecond.getRank().compareTo(rivalCard.getRank()) > 0) {
                        return bcSecond;
                    }
                } else {
                    boolean hook = getRival(player).getCards().hasPrevFromSameSuit(result) && getRival(player).getCards().hasNextFromSameSuit(result);
                    if (hook || player.getCards().getSuitCount(rivalCard.getSuit()) == TWO_CARDS) {
                        if (result.getRank().compareTo(rivalCard.getRank()) > 0) {
                            return result;
                        }
                    }
                }
            }
            return player.getCards().findMinAboveCard(rivalCard);
        }

        return null;
    }
}
