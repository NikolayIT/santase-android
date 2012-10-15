package game.logic.strategy.validator;

import game.beans.Player;
import game.beans.pack.card.Card;
import game.beans.pack.card.suit.Suit;

public class ObligatoryGameValidator extends BasicGameValidator {
	
	@Override
	public ValidateCode validatePlayerCard(Player player, Card ownCard, Card rivalCard, Suit trumpSuit) {
		if (ownCard == null) {
			return ValidateCode.PLAYER_NOT_VALID_CARD;
		}

		if (rivalCard == null) {
			return ValidateCode.PLAYER_CAN_PLAY;	
		}
		
		if (ownCard.getSuit().equals(rivalCard.getSuit()) && ownCard.getRank().compareTo(rivalCard.getRank()) > 0) {
			return ValidateCode.PLAYER_CAN_PLAY;
		}

		boolean haveSameColor = false;
		boolean haveBigger = false;
		boolean haveTrump = false;

		for (int i = 0; i < player.getCards().getSize(); i++) {
			if (player.getCards().getCard(i).getSuit().equals(rivalCard.getSuit())) {
				haveSameColor = true;
				if (player.getCards().getCard(i).getRank().compareTo(rivalCard.getRank()) > 0) {
					haveBigger = true;
				}
			}
			if (player.getCards().getCard(i).getSuit().equals(trumpSuit)) {
				haveTrump = true;
			}
		}

		if (!ownCard.getSuit().equals(rivalCard.getSuit()) && haveSameColor) {
			return ValidateCode.SAME_COLOR_ERROR;
		}

		if (ownCard.getSuit().equals(rivalCard.getSuit()) && haveBigger) {
			return ValidateCode.HAVE_BIGGER_ERROR;
		}

		if (!haveSameColor && haveTrump && !ownCard.getSuit().equals(trumpSuit)) {
			return ValidateCode.NOT_PLAY_TRUMP_ERROR;
		}

		return ValidateCode.PLAYER_CAN_PLAY;
	}
}
