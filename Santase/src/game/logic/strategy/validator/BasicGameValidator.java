package game.logic.strategy.validator;

import game.beans.Player;
import game.beans.pack.card.Card;
import game.beans.pack.card.suit.Suit;

public abstract class BasicGameValidator {
	
	/**
	 * Validates a player played card.
	 * 
	 * @param player
	 * @param ownCard
	 * @param rivalCard
	 * @param trumpSuit
	 * @return
	 */
	public abstract ValidateCode validatePlayerCard(final Player player, final Card ownCard, final Card rivalCard, final Suit trumpSuit);
}
