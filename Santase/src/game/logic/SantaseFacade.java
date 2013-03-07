/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic;

import game.beans.Game;
import game.beans.Player;
import game.beans.pack.card.Card;
import game.beans.pack.card.rank.Rank;
import game.logic.strategy.BasicGameAdviser;
import game.logic.strategy.ClosedGameAdviser;
import game.logic.strategy.ObligatoryGameAdviser;
import game.logic.strategy.OrdinaryGameAdviser;
import game.logic.strategy.validator.ValidateCode;

/**
 * SantaseGame class represents a Facade which implements the basic logic methods.
 * @author Dimitar Karamanov
 */
public final class SantaseFacade {

    /**
     * Points of the game and other constants
     */
    public static final int END_GAME_POINTS = 66;

    public static final int POINTS_ZONE = 33;

    public static final int MAX_LITTLE_GAMES = 11;

    /**
     * Obligatory game adviser.
     */
    private BasicGameAdviser obligatoryGameAdviser;

    /**
     * Ordinary game adviser.
     */
    private BasicGameAdviser ordinaryGameAdviser;

    /**
     * Closed game adviser.
     */
    private BasicGameAdviser closedGameAdviser;

    /**
     * Game bean object.
     */
    private Game game;

    /**
     * Constructor.
     */
    public SantaseFacade() {
        super();
        setGame(new Game());
    }

    public final void setGame(Game game) {
        this.game = game;
        obligatoryGameAdviser = new ObligatoryGameAdviser(game);
        ordinaryGameAdviser = new OrdinaryGameAdviser(game);
        closedGameAdviser = new ClosedGameAdviser(game);
    }

    public final Game getGame() {
        return game;
    }

    /**
     * Returns tip message for the provided player.
     * @param gPlayer player.
     * @return String representing tip message.
     */
    public final Card getTipMessageCard(Player player) {
        Card card = getCard(player);
        return card;
    }

    /**
     * Returns play card for a player.
     * @param player for which returns card.
     * @return Card instance.
     */
    public Card getAICard(final Player player) {
        player.setPlayedCard(null);// first null -> getCard() ?
        Card card = getCard(player);
        player.setPlayedCard(card);
        checkGameActionStatus(player);
        Card result = player.getCards().remove(player.getPlayedCard());
        return result;
    }

    /**
     * Checks game action status for provided player.
     * @param player player.
     */
    private void checkGameActionStatus(final Player player) {
        if (game.containActionStatus(Game.GA_CHANGE)) {
            game.changeTrumpCard(player.getCards().findCard(Rank.Nine, game.getTrumpSuit()), player);
        }

        if (game.containActionStatus(Game.GA_CLOSE)) {
            game.setClosedGame(player);
        }

        if (game.containActionStatus(Game.GA_COUPLE)) {
            player.getCouples().setCouple(player.getPlayedCard().getSuit());
        }
    }

    public ValidateCode validatePlayerCard(Player player, Player attackPlayer, Card card) {
        return getGameAdviser().validatePlayerCard(player, card, attackPlayer.getPlayedCard(), game.getTrumpSuit());
    }

    private Card getCard(final Player player) {
        Card result = null;
        game.clearGameActionStatus();

        game.copyState();
        try {
            if (!player.getCards().isEmpty()) {
                result = getGameAdviser().getCard(player);
            }
        } finally {
            game.restoreState();
        }
        return result;
    }

    private BasicGameAdviser getGameAdviser() {
        if (game.isNotClosedGame()) {
            return game.isObligatoryMode() ? obligatoryGameAdviser : ordinaryGameAdviser;
        }
        return closedGameAdviser;
    }

    public boolean isHumanTurn() {
        return game.isPlayerTurn(game.getHuman());
    }
}
