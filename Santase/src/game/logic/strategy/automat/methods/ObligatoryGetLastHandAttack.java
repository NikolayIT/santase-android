package game.logic.strategy.automat.methods;

import game.beans.Game;
import game.beans.Player;
import game.logic.strategy.automat.executors.base.PlayAttackCardExecutor;

public class ObligatoryGetLastHandAttack extends PlayAttackCardExecutor {

    public ObligatoryGetLastHandAttack(Game game) {
        super(game);

        register(new ObligatoryGetLastHandMaxTrumpAttack(game));
        register(new ObligatoryGetLastHandMaxOneSuitAttack(game));
        register(new ObligatoryGetLastHandMaxTwoSuitAttack(game));
        register(new ObligatoryForceToPlayTrump(game));
    }

    /**
     * Handler method providing the user facility to check custom condtion for methods executions.
     * @param player for which is called the executor
     * @param opposite player.
     * @return true to process method execution false to not.
     */
    protected boolean fitPreCondition(final Player player) {
        Player rival = game.getRival(player);
        return player.getCards().getSize() == 2 && rival.getCards().getSize() == 2 && player.getPoints(game.getTrumpSuit()) >= Game.POINTS_ZONE;
    }

}
