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
import game.logic.strategy.automat.base.PlayCardMethod;
import game.logic.strategy.automat.methods.base.BaseMethod;

/**
 * ClosedAttackCardPowerTrumps class. PlayCardMethod which implements the logic of playing a card when the player has power trumps.
 * 
 * @author Dimitar Karamanov
 */
public final class ClosedAttackCardPowerTrumps extends BaseMethod {
    /**
     * Help play card method.
     */
    private final PlayCardMethod closedNoTrumpAttackCardPowerTrumps;

    /**
     * Constructor.
     * 
     * @param game SantaseGame instance.
     */
    public ClosedAttackCardPowerTrumps(final Game game) {
        super(game);
        closedNoTrumpAttackCardPowerTrumps = new ClosedNoTrumpAttackCardPowerTrumps(game);
    }

    /**
     * Returns player's card.
     * 
     * @param player AI player.
     * @param opposite player.
     * @return Card object instance or null.
     */
    protected Card getPlayMethodCard(final Player player) {
        Card result = null;
        if (hasPowerTrumps(player, true)) {
            result = player.getCards().findMaxSuitCard(game.getTrumpSuit());
            if (result == null) {
                result = closedNoTrumpAttackCardPowerTrumps.getPlayerCard(player);
            }
        }
        return result;
    }
}