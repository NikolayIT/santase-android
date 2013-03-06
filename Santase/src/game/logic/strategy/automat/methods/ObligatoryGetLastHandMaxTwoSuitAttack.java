package game.logic.strategy.automat.methods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import game.beans.Game;
import game.beans.Player;
import game.beans.pack.PackIterator;
import game.beans.pack.card.Card;
import game.beans.pack.card.suit.Suit;
import game.logic.strategy.automat.methods.base.BaseMethod;

public class ObligatoryGetLastHandMaxTwoSuitAttack extends BaseMethod {

    public ObligatoryGetLastHandMaxTwoSuitAttack(Game game) {
        super(game);
    }

    @Override
    protected Card getPlayMethodCard(Player player) {
        Player rival = game.getRival(player);

        List<Suit> pSuits = getSuitList(player);
        List<Suit> rSuits = getSuitList(rival);

        if (pSuits.size() == 2) {

            Card maxSuitCard = null;
            if (equal(pSuits, rSuits)) {
                for (Iterator<Suit> iterator = pSuits.iterator(); iterator.hasNext();) {
                    Suit suit = iterator.next();
                    Card card = player.getCards().findMaxSuitCard(suit);
                    if (card != null && isBestSuitCardLeft(player, card)) {
                        maxSuitCard = card;
                    }
                }

                if (maxSuitCard != null) {
                    for (Iterator<Suit> iterator = pSuits.iterator(); iterator.hasNext();) {
                        Suit suit = iterator.next();
                        if (!maxSuitCard.getSuit().equals(suit)) {
                            return player.getCards().findMinSuitCard(suit);
                        }
                    }
                }
            }
        }

        return null;
    }

    private List<Suit> getSuitList(Player player) {
        List<Suit> result = new ArrayList<Suit>();
        for (PackIterator iterator = player.getCards().iterator(); iterator.hasNext();) {
            final Card card = iterator.next();
            if (!result.contains(card.getSuit())) {
                result.add(card.getSuit());
            }
        }
        return result;
    }

    private boolean equal(List<Suit> pSuits, List<Suit> rSuits) {
        if (pSuits.size() == rSuits.size()) {
            for (Iterator<Suit> iterator = pSuits.iterator(); iterator.hasNext();) {
                Suit suit = iterator.next();
                if (!rSuits.contains(suit)) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }
}
