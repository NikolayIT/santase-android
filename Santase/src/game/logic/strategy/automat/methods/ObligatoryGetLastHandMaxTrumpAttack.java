package game.logic.strategy.automat.methods;

import game.beans.Game;
import game.beans.Player;
import game.beans.pack.PackIterator;
import game.beans.pack.card.Card;
import game.logic.strategy.automat.methods.base.BaseMethod;

public class ObligatoryGetLastHandMaxTrumpAttack extends BaseMethod {

    public ObligatoryGetLastHandMaxTrumpAttack(Game game) {
        super(game);
    }

    @Override
    protected Card getPlayMethodCard(Player player) {
        Card result = null;
        boolean hasBestTrumpCard = false;
        for (PackIterator iterator = player.getCards().iterator(); iterator.hasNext();) {
            final Card card = iterator.next();
            if (isTrumpCard(card) && isBestSuitCardLeft(player, card)) {
                hasBestTrumpCard = true;
                break;
            }
        }

        if (hasBestTrumpCard) {
            for (PackIterator iterator = player.getCards().iterator(); iterator.hasNext();) {
                final Card card = iterator.next();
                if (!isTrumpCard(card) || !isBestSuitCardLeft(player, card)) {
                    return card;
                }
            }
        }

        return result;
    }
}
