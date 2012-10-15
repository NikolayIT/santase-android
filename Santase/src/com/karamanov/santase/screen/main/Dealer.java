package com.karamanov.santase.screen.main;

import game.beans.Player;
import game.beans.pack.card.Card;
import game.beans.pack.card.rank.Rank;
import game.beans.pack.card.suit.Suit;
import game.logic.SantaseGame;
import game.logic.strategy.validator.ValidateCode;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;

import com.karamanov.santase.R;
import com.karamanov.santase.Santase;
import com.karamanov.santase.graphics.ImagePosition;
import com.karamanov.santase.graphics.ImageUtil;
import com.karamanov.santase.graphics.Rectangle;
import com.karamanov.santase.screen.base.GameActivity;
import com.karamanov.santase.screen.base.SantasePainter;
import com.karamanov.santase.screen.main.message.MessageData;
import com.karamanov.santase.screen.main.message.MessageScreen;
import com.karamanov.santase.text.TextDecorator;

public class Dealer {

	private static final String TRUMP = "TRUMP";

	private final Handler handler;

	/**
	 * Standard card delay on painting (effect).
	 */
	public static final int STANDARD_CARD_DELAY = 30;

	/**
	 * Belot painter. (All drawing functionality is in it).
	 */
	public final SantasePainter santasePainter;
	
	private MessageScreen messageScreen;
	
	public static final int NAV_PRESS = -1;
	public static final int NAV_LEFT = -2;
	public static final int NAV_RIGHT = -3;

	private static final long PLAY_DELAY = 200;

	private final GameActivity context;

	private final SantaseGame game;

	private final SantaseView santasePanel;

	private final TextDecorator textDecorator;
	
	public boolean endGameActivity = true;

	public Dealer(GameActivity context, SantaseGame game, SantaseView belotPanel) {
		this.context = context;
		this.game = game;
		this.santasePanel = belotPanel;

		santasePainter = new SantasePainter(context);
		handler = new Handler();
		textDecorator = new TextDecorator(context);
	}

	public void checkClick(float x, float y) {
		// if Player on Move and click card
		if (checkClickForNextTourOrEndGame()) {
			return;
		}

		if (checkClickForPlay() && isClickedSelectedCard(x, y)) {
			if (validatePlayerMove()) {
				playSelectedPlayerCard();
			}
			return;
		}

		if (game.isPlayerTurn(game.getPlayer()) && isMouseOverTrumpCard(x, y)) {
			changeTrumpCard();
			return;
		}
		
		if (game.isPlayerTurn(game.getPlayer()) && game.canClose() && isMouseOverCloseCard(x, y)) {
			showCloseGameDialog();
			return;
		}

		Card card = null;
		if (game.isPlayerTurn(game.getPlayer()) && (card = getHumanCardUnderPointer(x, y)) != null) {
			if (game.validatePlayerMove(game.getPlayer(), game.getComputer(), card).equals(ValidateCode.PLAYER_CAN_PLAY)) {
				game.getPlayer().setSelectedCard(card);
				invalidateGame();
				return;
			}
		}
	}
	
	private void showCloseGameDialog() {
		final com.karamanov.santase.screen.base.BooleanFlag wait = new com.karamanov.santase.screen.base.BooleanFlag();
		handler.post(new Runnable() {
			public void run() {
				AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
				myAlertDialog.setTitle(context.getString(R.string.Confirm));
				myAlertDialog.setMessage(context.getString(R.string.CloseGameQuestion));
				myAlertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						turnToPlayerClosedMode();
						wait.setFalse();
					}
				});
				myAlertDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						wait.setFalse();
					}
				});
				myAlertDialog.show();
			}
		});

		while (wait.getValue()) {
			invalidateGame();
			sleep(PLAY_DELAY);
		}
	}

	private boolean isMouseOverTrumpCard(float x, float y) {
		// Case when there is no Trump Card (Not Visible)
		if (game.getGameCards().getSize() == 0 || game.isClosedGame()) {
			return false;
		}
		return santasePainter.getTrumpCardRectangle(santasePanel).include((int) x, (int) y);
	}
	
	private boolean isMouseOverCloseCard(float x, float y) {
		// Case when there is no Trump Card (Not Visible)
		if (game.getGameCards().getSize() == 0 || game.isClosedGame()) {
			return false;
		}
		return santasePainter.getCloseCardRectangle(santasePanel).include((int) x, (int) y);
	}

	private boolean isClickedSelectedCard(float x, float y) {
		return game.getPlayer().getSelectedCard() != null && game.getPlayer().getSelectedCard().equals(getHumanCardUnderPointer(x, y));
	}

	public void turnToPlayerClosedMode() {
		game.setClosedGame(game.getPlayer());
		invalidateGame();
	}

	private void newGame(final Player player) {
		game.getPlayer().setSelectedCard(null);
		game.newGame(player);

		invalidateGame(STANDARD_CARD_DELAY);
		// ako e v newGame ste e malko shibano s prechertavaneto
		if (game.getComputer().equals(game.getGame().getTrickAttackPlayer())) {
			getAICard();
		}
		
		if (game.isBigNewGame()) {
			String message;
			Bitmap bitmap;
			if (game.getComputer().equals(player)) {
				message = context.getString(R.string.AndroidWinSeries);
				bitmap = santasePainter.getUnhappy();
			} else {
				message = context.getString(R.string.PlayerWinSeries);
				bitmap = santasePainter.getHappy();
			}
			ArrayList<MessageData> list = new ArrayList<MessageData>();
			list.add(new MessageData(bitmap, message));
			displayMessage(list);
		}
		
		invalidateGame();
	}

	private void newPlayerGame() {
		newGame(game.getPlayer());
	}
	
	public boolean playerCanChangeTrumpCard(ArrayList<MessageData> result, Card PlayerCheckedCard) {
		if (PlayerCheckedCard == null || !PlayerCheckedCard.getSuit().equals(game.getTrumpSuit()) || !PlayerCheckedCard.getRank().equals(Rank.Nine)) {
			String errorStr = context.getString(R.string.YouCanChangeWith9Trump);
			result.add(new MessageData(null, errorStr.replace(TRUMP, textDecorator.getSuit(game.getTrumpSuit()))));
			
			return false;
		}

		if (game.getGameCards().getSize() == 12 || game.getPlayer().getHands().getSize() == 0) {
			result.add(new MessageData(null, context.getString(R.string.YouCanChangeHands)));
			return false;
		}

		if (game.getGameCards().getSize() <= 2) {
			result.add(new MessageData(null, context.getString(R.string.TwoCardsLeft)));
			return false;
		}

		if (game.getComputer().getPlayedCard() != null) {
			result.add(new MessageData(null, context.getString(R.string.YouCannotChange)));
			return false;
		}

		return true;
	}

	private void changeTrumpCard() {
		ArrayList<MessageData> result = new ArrayList<MessageData>();
		if (playerCanChangeTrumpCard(result, game.getPlayer().getSelectedCard())) {
			game.changeTrumpCard(game.getPlayer().getSelectedCard(), game.getPlayer());
			game.getPlayer().setSelectedCard(null);
			invalidateGame();
		} else {
			showError(result);
		}
	}

	private void showError(ArrayList<MessageData> result) {
		displayMessage(result);
	}

	/**
	 * Displays a message.
	 * 
	 * @param player which call the message function.
	 * @param card played by player.
	 */
	private void displayMessage(final ArrayList<MessageData> messages) {
		final com.karamanov.santase.screen.base.BooleanFlag wait = new com.karamanov.santase.screen.base.BooleanFlag();
		handler.post(new Runnable() {
			public void run() {
				messageScreen = new MessageScreen(context, messages, wait);
				messageScreen.show();
			}
		});

		while (wait.getValue()) {
			invalidateGame();
			sleep(PLAY_DELAY);
		}
	}

	private void playSelectedPlayerCard() {
		if (endGameWithCouple()) {
			//moveSelectedPlayerCard();
			newPlayerGame();
		} else {
			moveSelectedPlayerCard();
			game.getGame().setTrickAttackPlayer(game.getComputer());

			if (game.getComputer().getPlayedCard() == null) {
				getAICard();
			}

			invalidateGame();
		}
	}

	private void moveSelectedPlayerCard() {
		game.getPlayer().getCards().remove(game.getPlayer().getSelectedCard());
		game.getPlayer().setPlayedCard(game.getPlayer().getSelectedCard());
		game.getPlayer().setSelectedCard(null);

		invalidateGame();
	}

	private boolean endGameWithCouple() {
		return game.getComputer().getPlayedCard() == null && game.getPlayer().getCards().hasCouple(game.getPlayer().getSelectedCard()) && playCoupleAndStop(game.getPlayer().getSelectedCard());
	}

	private boolean playCoupleAndStop(Card pc) {
		game.getPlayer().getCouples().setCouple(pc.getSuit());
		
		boolean result = game.getPlayer().getPoints(game.getTrumpSuit()) >= SantaseGame.END_GAME_POINTS;

		if (result) {
			moveSelectedPlayerCard(); //Refactor
			invalidateGame();
			displayPlayerCoupleMessageExit(pc.getSuit());
		} else {
			invalidateGame();
			displayCoupleMessagePlayer(pc.getSuit());
		}

		return result;
	}

	private boolean validatePlayerMove() {
		return game.validatePlayerMove(game.getPlayer(), game.getComputer(), game.getPlayer().getSelectedCard()).equals(ValidateCode.PLAYER_CAN_PLAY);
	}

	private boolean checkClickForPlay() {
		return game.getPlayer().getSelectedCard() != null && game.isPlayerTurn(game.getPlayer());
	}

	private boolean checkClickForNextTourOrEndGame() {
		if (game.isBothPlayed()) {
			invalidateGame();

			if (game.nextTour()) {
				return checkClickForNextTour();
			} else {
				return checkClickForEndGame();
			}
		}
		return false;
	}

	private boolean checkClickForNextTour() {
		invalidateGame();

		if (game.getPlayer().equals(game.getGame().getTrickAttackPlayer())) {
			if (game.canEndGame(game.getPlayer())) {
				displayEndGameMessage(game.getPlayer());
				newPlayerGame();
				return true;
			}
		}

		if (game.getComputer().equals(game.getGame().getTrickAttackPlayer())) {
			if (game.canEndGame(game.getComputer())) {
				displayEndGameMessage(game.getComputer());
				newComputerGame();
				return true;
			}

			getAICard();
			// Check for end with couple
			if (game.canEndGame(game.getComputer()))// ??? I think is necessary ||
												// game.player.cards.getSize()
												// == 0)
			{
				newComputerGame();
				return true;
			}

			invalidateGame();
		}
		return true;
	}

	private void displayEndGameMessage(final Player gPlayer) {
		game.calculateFuturePoints(gPlayer);

		if (gPlayer.equals(game.getComputer())) {
			showInfo(context.getString(R.string.AndroidMore66));
		} else {
			showInfo(context.getString(R.string.HumanMore66));
		}
	}

	private void showInfo(String infoString) {
		showInfo(infoString, null, null);
	}

	private void showInfo(String infoString, Bitmap image, ImagePosition position) {
		ArrayList<MessageData> list = new ArrayList<MessageData>();
		list.add(new MessageData(null, infoString));
		displayMessage(list);
	}

	private void newComputerGame() {
		newGame(game.getComputer());
	}

	private void getAICard() {
		game.getGame().setTrickAttackPlayer(game.getPlayer());
		game.getAICard(game.getComputer());
		checkGameActionStatus(game.getComputer());
	}

	private void checkGameActionStatus(final Player aiPlayer) {

		if ((game.getGame().getGameActionStatus() & SantaseGame.GA_CHANGE) == SantaseGame.GA_CHANGE) {
			displayChangeTrumpCard();
		}

		if ((game.getGame().getGameActionStatus() & SantaseGame.GA_CLOSE) == SantaseGame.GA_CLOSE) {
			displayCloseGame();
		}

		if ((game.getGame().getGameActionStatus() & SantaseGame.GA_COUPLE) == SantaseGame.GA_COUPLE) {

			if (game.getComputer().getPoints(game.getTrumpSuit()) >= SantaseGame.END_GAME_POINTS) {
				game.calculateFuturePoints(game.getComputer());
				invalidateGame();
				displayCoupleMessageExit(aiPlayer.getPlayedCard().getSuit());
			} else {
				invalidateGame();
				displayCoupleMessage(aiPlayer.getPlayedCard().getSuit());
			}
		}
	}

	public void displayChangeTrumpCard() {
		showInfo(context.getString(R.string.AnnounceChangeTrumpCard));
	}

	private void displayCloseGame() {
		showInfo(context.getString(R.string.AnnounceCloseGame));
	}

	private void displayCoupleMessageExit(Suit suit) {
		String message = context.getString(R.string.AndroidHasACoupleExit);
		message = textDecorator.replaceSuit(suit, message);
		message = textDecorator.translateCouple(suit, game.getTrumpSuit(), message);
		showInfo(message, ImageUtil.transformToDisabledImage(santasePainter.getSuitImage(suit)), ImagePosition.Right);
	}

	private void displayPlayerCoupleMessageExit(Suit suit) {
		String message = context.getString(R.string.HumanHasACoupleExit);
		message = textDecorator.replaceSuit(suit, message);
		message = textDecorator.translateCouple(suit, game.getTrumpSuit(), message);
		showInfo(message, ImageUtil.transformToDisabledImage(santasePainter.getSuitImage(suit)), ImagePosition.Right);
	}

	private void displayCoupleMessagePlayer(Suit suit) {
		String message = context.getString(R.string.HumanHasCoupleOf);
		message = textDecorator.replaceSuit(suit, message);
		message = textDecorator.translateCouple(suit, game.getTrumpSuit(), message);
		showInfo(message, ImageUtil.transformToDisabledImage(santasePainter.getSuitImage(suit)), ImagePosition.Right);
	}

	private void displayCoupleMessage(Suit suit) {
		String message = context.getString(R.string.AndroidHasCoupleOf);
		message = textDecorator.replaceSuit(suit, message);
		message = textDecorator.translateCouple(suit, game.getTrumpSuit(), message);
		showInfo(message, ImageUtil.transformToDisabledImage(santasePainter.getSuitImage(suit)), ImagePosition.Right);
	}

	private Card getHumanCardUnderPointer(final float x, final float y) {
		Player player = game.getPlayer();

		for (int i = 0; i < Rank.getRankCount(); i++) {
			if (i < player.getCards().getSize()) {
				Rectangle rec = santasePainter.getPlayerCardRectangle(game, i, santasePanel);
				if (rec.include((int) x, (int) y)) {
					Card card = player.getCards().getCard(i);
					return card;
				}
			}
		}
		return null;
	}

	/**
	 * Sleeps for provided millisecond.
	 * @param ms provided millisecond.
	 */
	public final void sleep(final long ms) {
		if (ms > 0) {
			try {
				Thread.sleep(ms);
			} catch (InterruptedException ex) {
				// ex.printStackTrace();
			}
		}
	}

	/**
	 * Checks key click.
	 * 
	 * @param keyCode pressed key code.
	 * @param gameAction status.
	 */
	public void checkKeyPressed(int keyCode) {
		checkKeyPressedPlayGameMode(keyCode);
	}

	private void checkKeyPressedPlayGameMode(int keyCode) {
		if (keyCode == NAV_PRESS) {
			if (canPlayerPlaySelectedCard()) {
				if (validatePlayerMove()) {
					playSelectedPlayerCard();
					return;
				}
			}
		} else if (keyCode == NAV_LEFT && game.isPlayerTurn(game.getPlayer())) {
			if (canSelectPlayersCard()) {
				Card card = selectNextLeftCard();
				processSelectCard(card);
				return;
			}
		} else if (keyCode == NAV_RIGHT && game.isPlayerTurn(game.getPlayer())) {
			if (canSelectPlayersCard()) {
				Card card = selectNextRightCard();
				processSelectCard(card);
				return;
			}
		}		
		if (checkClickForNextTourOrEndGame()) {
			return;
		}
	}
	
	/**
	 * Process the selected card.
	 * 
	 * @param card selected one.
	 */
	private void processSelectCard(final Card card) {
		if (card != null) {
			if (!card.equals(game.getPlayer().getSelectedCard())) {
				game.getPlayer().setSelectedCard(card);
				invalidateGame();
			}
		}
	}
	
	/**
     * Returns the position of the first left card.
     * @return int the position of the first left card.
     */
    private int getFirstLeftCardIndex() {
        if (game.getPlayer().getCards().getSize() > 0) {
            return 0;
        }
        return -1;
    }
	
	/**
     * Returns next right card index.
     * @param current card index.
     * @return next right card index.
     */
    private int getNextRightCardIndex(int current) {
        if (current < 0) {
            return getFirstLeftCardIndex();
        } else {
            if (current == game.getPlayer().getCards().getSize() - 1) {
                return getFirstLeftCardIndex();
            }
            return current + 1;
        }
    }
	
	/**
     * Selects next right card.
     * @return the selected card.
     */
    public Card selectNextRightCard() {
        int index = getHumanSelectedCardIndex();
        Card card;
        do {
            index = getNextRightCardIndex(index);
            card = game.getPlayer().getCards().getCard(index);
        } while (!game.validatePlayerMove(game.getPlayer(), game.getComputer(), card).equals(ValidateCode.PLAYER_CAN_PLAY));

        return card;
    }
	
	/**
     * Returns human selected card index.
     * @return human selected card index.
     */
    private int getHumanSelectedCardIndex() {
        if (game.getPlayer().getSelectedCard() != null) {
            for (int i = 0; i < game.getPlayer().getCards().getSize(); i++) {
                final Card card = game.getPlayer().getCards().getCard(i);

                if (game.getPlayer().getSelectedCard().equals(card)) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    /**
     * Returns the position of the first right card.
     * @return int the position of the first right card.
     */
    private int getFirstRightCardIndex() {
        if (game.getPlayer().getCards().getSize() > 0) {
            return game.getPlayer().getCards().getSize() - 1;
        }
        return -1;
    }
    
    /**
     * Returns next left card index.
     * @param current card index.
     * @return next left card index.
     */
    private int getNextLeftCardIndex(int current) {
        if (current < 1) {
            return getFirstRightCardIndex();
        } else {
            return current - 1;
        }
    }
	
	/**
     * Selects next left card.
     * @return the selected card.
     */
    public Card selectNextLeftCard() {
        int index = getHumanSelectedCardIndex();
        Card card;
        do {
            index = getNextLeftCardIndex(index);
            card = game.getPlayer().getCards().getCard(index);
        } while (!game.validatePlayerMove(game.getPlayer(), game.getComputer(), card).equals(ValidateCode.PLAYER_CAN_PLAY));

        return card;
    }

	public void invalidateGame() {
		invalidateGame(0);
	}

	public void invalidateGame(int delay) {
		Canvas canvas = santasePanel.getBufferedCanvas();
		if (canvas != null) {
			santasePainter.drawGame(santasePanel.getBufferedCanvas(), game,
					santasePanel, delay);
			santasePanel.refresh();
		}
	}

	private boolean canSelectPlayersCard() {
		return game.isPlayerTurn(game.getPlayer());
	}

	private boolean canPlayerPlaySelectedCard() {
		return game.getPlayer().getSelectedCard() != null && game.isPlayerTurn(game.getPlayer());
	}

	private boolean checkClickForEndGame() {
		if (game.isObligatoryMode() && (game.getComputer().getCards().getSize() != 0)) {
			displayEndGameMessage(game.getGame().getTrickAttackPlayer());
		} else {
			displayEndGameMessageOnLastHand(game.getGame().getTrickAttackPlayer());
		}

		newGame(game.getGame().getTrickAttackPlayer());

		return true;
	}

	private void displayEndGameMessageOnLastHand(final Player gPlayer) {
		String text;

		if (game.isClosedGame() && (gPlayer.equals(game.getGame().getPlayerClosedGame()) && gPlayer.getPoints(game.getTrumpSuit()) < SantaseGame.END_GAME_POINTS)) {
			text = gPlayer.equals(game.getComputer()) ? context.getString(R.string.AndroidClosedAndLostTheGame) : context.getString(R.string.HumanClosedAndLostTheGame);
		} else {
			text = gPlayer.equals(game.getComputer()) ?  context.getString(R.string.AndroidGotLastHand) : context.getString(R.string.HumanGotLastHand);
		}

		showInfo(text);
	}
	
	
	public void onExit() {
		Santase.terminate(context);
		context.finish();
	}

	public void onConfigurationChanged(Configuration newConfig) {
		//DN
	}
}