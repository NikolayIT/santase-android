package game.logic.strategy.validator;

import game.beans.Player;
import game.beans.pack.card.Card;
import game.beans.pack.card.suit.Suit;

public class OrdinaryGameValidator extends BasicGameValidator {

    @Override
    public ValidateCode validatePlayerCard(Player player, Card ownCard, Card rivalCard, Suit trumpSuit) {
        if (ownCard == null) {
            return ValidateCode.PLAYER_NOT_VALID_CARD;
        }
        return ValidateCode.PLAYER_CAN_PLAY;
    }

}
