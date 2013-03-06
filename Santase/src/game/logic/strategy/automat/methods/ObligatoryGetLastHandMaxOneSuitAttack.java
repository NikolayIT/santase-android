package game.logic.strategy.automat.methods;

import game.beans.Game;
import game.beans.Player;
import game.beans.pack.PackIterator;
import game.beans.pack.card.Card;
import game.beans.pack.card.suit.Suit;
import game.logic.strategy.automat.methods.base.BaseMethod;

public class ObligatoryGetLastHandMaxOneSuitAttack extends BaseMethod {

    public ObligatoryGetLastHandMaxOneSuitAttack(Game game) {
        super(game);
    }

    @Override
    protected Card getPlayMethodCard(Player player) {
        boolean sameSuit = true;
        Suit suit = null;

        for (PackIterator iterator = player.getCards().iterator(); iterator.hasNext();) {
            final Card card = iterator.next();
            if (suit != null && !suit.equals(card.getSuit())) {
                sameSuit = false;
                break;
            }
            suit = card.getSuit();
        }

        for (PackIterator iterator = game.getRival(player).getCards().iterator(); iterator.hasNext();) {
            final Card card = iterator.next();
            if (suit != null && !suit.equals(card.getSuit())) {
                sameSuit = false;
                break;
            }
            suit = card.getSuit();
        }

        if (sameSuit) {
            Card card = player.getCards().findMaxSuitCard(suit);
            if (card != null && isBestSuitCardLeft(player, card)) {
                return player.getCards().findMinSuitCard(suit);
            }
        }

        return null;
    }
}
