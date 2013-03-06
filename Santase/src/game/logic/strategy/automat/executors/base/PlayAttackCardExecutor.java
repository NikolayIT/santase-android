package game.logic.strategy.automat.executors.base;

import game.beans.Game;
import game.beans.Player;
import game.beans.pack.card.Card;

public abstract class PlayAttackCardExecutor extends PlayCardExecutor {

    /**
     * Constructor
     * @param game BelotGame instance.
     */
    public PlayAttackCardExecutor(final Game game) {
        super(game);
    }

    /**
     * Handler method providing the user to write additional code which is executed after the getPlayerCard(Player).
     * @param player for which is called the executor.
     * @param opposite player.
     * @param result the result of the method getPlayerCard(Player)
     */
    protected final void afterExecution(final Player player, final Card result) {
        if (player.getCards().hasCouple(result)) {
            game.setCoupleMessage(player, result);
        }
    }
}
