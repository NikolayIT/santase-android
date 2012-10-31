package com.karamanov.santase.screen.main;

import game.beans.Game;
import game.beans.Player;
import game.beans.pack.card.Card;
import game.beans.pack.card.rank.Rank;
import game.beans.pack.card.suit.Suit;
import game.logic.SantaseFacade;
import game.logic.strategy.validator.ValidateCode;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;

import com.karamanov.framework.BooleanFlag;
import com.karamanov.framework.MessageActivity;
import com.karamanov.framework.graphics.ImageUtil;
import com.karamanov.framework.graphics.Rectangle;
import com.karamanov.santase.R;
import com.karamanov.santase.Santase;
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
     * Santase painter. (All drawing functionality is in it).
     */
    public final SantasePainter santasePainter;

    private MessageScreen messageScreen;

    public static final int NAV_PRESS = -1;
    public static final int NAV_LEFT = -2;
    public static final int NAV_RIGHT = -3;

    private static final long PLAY_DELAY = 200;

    private final MessageActivity context;

    private final SantaseFacade santaseFacade;

    private final SantaseView santasePanel;

    private final TextDecorator textDecorator;

    public boolean endGameActivity = true;

    public Dealer(MessageActivity context, SantaseFacade santaseFacade, SantaseView belotPanel) {
        this.context = context;
        this.santaseFacade = santaseFacade;
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

        if (santaseFacade.isHumanTurn() && isMouseOverTrumpCard(x, y)) {
            changeTrumpCard();
            return;
        }

        if (santaseFacade.isHumanTurn() && santaseFacade.getGame().canClose() && isMouseOverCloseCard(x, y)) {
            showCloseGameDialog();
            return;
        }

        if (santaseFacade.isHumanTurn()) {
            Card card = getHumanCardUnderPointer(x, y);
            if (card != null && santaseFacade.validatePlayerMove(santaseFacade.getGame().getHuman(), santaseFacade.getGame().getComputer(), card).equals(ValidateCode.PLAYER_CAN_PLAY)) {
                santaseFacade.getGame().getHuman().setSelectedCard(card);
                invalidateGame();
                return;
            }
        }
    }

    private void showCloseGameDialog() {
        final BooleanFlag wait = new BooleanFlag();
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
        if (santaseFacade.getGame().getGameCards().getSize() == 0 || santaseFacade.getGame().isClosedGame()) {
            return false;
        }
        return santasePainter.getTrumpCardRectangle(santasePanel).include((int) x, (int) y);
    }

    private boolean isMouseOverCloseCard(float x, float y) {
        // Case when there is no Trump Card (Not Visible)
        if (santaseFacade.getGame().getGameCards().getSize() == 0 || santaseFacade.getGame().isClosedGame()) {
            return false;
        }
        return santasePainter.getCloseCardRectangle(santasePanel).include((int) x, (int) y);
    }

    private boolean isClickedSelectedCard(float x, float y) {
        return santaseFacade.getGame().getHuman().getSelectedCard() != null && santaseFacade.getGame().getHuman().getSelectedCard().equals(getHumanCardUnderPointer(x, y));
    }

    public void turnToPlayerClosedMode() {
        santaseFacade.getGame().setClosedGame(santaseFacade.getGame().getHuman());
        invalidateGame();
    }

    private void newGame(final Player player) {
        santaseFacade.getGame().getHuman().setSelectedCard(null);
        santaseFacade.getGame().newGame(player);

        invalidateGame(STANDARD_CARD_DELAY);
        // ako e v newGame ste e malko shibano s prechertavaneto
        if (santaseFacade.getGame().getComputer().equals(santaseFacade.getGame().getTrickAttackPlayer())) {
            getAICard();
        }

        if (santaseFacade.getGame().isBigNewGame()) {
            String message;
            Bitmap bitmap;
            
            if (santaseFacade.getGame().getComputer().equals(santaseFacade.getGame().getTrickAttackPlayer())) {
                message = context.getString(R.string.PlayerWinSeries);
                bitmap = santasePainter.getHappy();
            } else {
                message = context.getString(R.string.AndroidWinSeries);
                bitmap = santasePainter.getUnhappy();
            }
            
            ArrayList<MessageData> list = new ArrayList<MessageData>();
            list.add(new MessageData(bitmap, message));
            displayMessage(list);
        }

        invalidateGame();
    }

    public boolean playerCanChangeTrumpCard(ArrayList<MessageData> result, Card PlayerCheckedCard) {
        if (PlayerCheckedCard == null || !PlayerCheckedCard.getSuit().equals(santaseFacade.getGame().getTrumpSuit()) || !PlayerCheckedCard.getRank().equals(Rank.Nine)) {
            String errorStr = context.getString(R.string.YouCanChangeWith9Trump);
            result.add(new MessageData(null, errorStr.replace(TRUMP, textDecorator.getSuit(santaseFacade.getGame().getTrumpSuit()))));

            return false;
        }

        if (santaseFacade.getGame().getGameCards().getSize() == 12 || santaseFacade.getGame().getHuman().getHands().getSize() == 0) {
            result.add(new MessageData(null, context.getString(R.string.YouCanChangeHands)));
            return false;
        }

        if (santaseFacade.getGame().getGameCards().getSize() <= 2) {
            result.add(new MessageData(null, context.getString(R.string.TwoCardsLeft)));
            return false;
        }

        if (santaseFacade.getGame().getComputer().getPlayedCard() != null) {
            result.add(new MessageData(null, context.getString(R.string.YouCannotChange)));
            return false;
        }

        return true;
    }

    private void changeTrumpCard() {
        ArrayList<MessageData> result = new ArrayList<MessageData>();
        if (playerCanChangeTrumpCard(result, santaseFacade.getGame().getHuman().getSelectedCard())) {
            santaseFacade.getGame().changeTrumpCard(santaseFacade.getGame().getHuman().getSelectedCard(), santaseFacade.getGame().getHuman());
            santaseFacade.getGame().getHuman().setSelectedCard(null);
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
        final BooleanFlag wait = new BooleanFlag();
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
            Player human = santaseFacade.getGame().getHuman();
            newGame(human);
        } else {
            Player computer = santaseFacade.getGame().getComputer();
            moveSelectedPlayerCard();
            santaseFacade.getGame().setTrickAttackPlayer(computer);

            if (computer.getPlayedCard() == null) {
                getAICard();
            }

            invalidateGame();
        }
    }

    private void moveSelectedPlayerCard() {
        santaseFacade.getGame().getHuman().getCards().remove(santaseFacade.getGame().getHuman().getSelectedCard());
        santaseFacade.getGame().getHuman().setPlayedCard(santaseFacade.getGame().getHuman().getSelectedCard());
        santaseFacade.getGame().getHuman().setSelectedCard(null);

        invalidateGame();
    }

    private boolean endGameWithCouple() {
        return santaseFacade.getGame().getComputer().getPlayedCard() == null && santaseFacade.getGame().getHuman().getCards().hasCouple(santaseFacade.getGame().getHuman().getSelectedCard())
                && playCoupleAndStop(santaseFacade.getGame().getHuman().getSelectedCard());
    }

    private boolean playCoupleAndStop(Card pc) {
        Player human = santaseFacade.getGame().getHuman();
        
        human.getCouples().setCouple(pc.getSuit());
        boolean result = human.getPoints(santaseFacade.getGame().getTrumpSuit()) >= SantaseFacade.END_GAME_POINTS;

        if (result) {
            moveSelectedPlayerCard(); // Refactor
            invalidateGame();
            displayPlayerCoupleMessageExit(pc.getSuit());
        } else {
            invalidateGame();
            displayCoupleMessagePlayer(pc.getSuit());
        }

        return result;
    }

    private boolean validatePlayerMove() {
        Player human = santaseFacade.getGame().getHuman();
        Player computer = santaseFacade.getGame().getComputer();
        return santaseFacade.validatePlayerMove(human, computer, human.getSelectedCard()).equals(ValidateCode.PLAYER_CAN_PLAY);
    }

    private boolean checkClickForPlay() {
        return santaseFacade.getGame().getHuman().getSelectedCard() != null && santaseFacade.isHumanTurn();
    }

    private boolean checkClickForNextTourOrEndGame() {
        if (santaseFacade.getGame().isBothPlayed()) {
            invalidateGame();

            if (santaseFacade.getGame().nextTour()) {
                return checkClickForNextTour();
            } else {
                return checkClickForEndGame();
            }
        }
        return false;
    }

    private boolean checkClickForNextTour() {
        invalidateGame();
        
        Player human = santaseFacade.getGame().getHuman();
        Player computer = santaseFacade.getGame().getComputer();

        if (human.equals(santaseFacade.getGame().getTrickAttackPlayer())) {
            if (santaseFacade.getGame().canEndGame(human)) {
                displayEndGameMessage(human);
                newGame(human);
                return true;
            }
        }

        if (computer.equals(santaseFacade.getGame().getTrickAttackPlayer())) {
            if (santaseFacade.getGame().canEndGame(computer)) {
                displayEndGameMessage(computer);
                newGame(computer);
                return true;
            }

            getAICard();
            // Check for end with couple
            if (santaseFacade.getGame().canEndGame(computer)) {
                newGame(computer);
                return true;
            }

            invalidateGame();
        }
        return true;
    }

    private void displayEndGameMessage(final Player gPlayer) {
        santaseFacade.getGame().calculateFuturePoints(gPlayer);

        if (gPlayer.equals(santaseFacade.getGame().getComputer())) {
            showInfo(context.getString(R.string.AndroidMore66));
        } else {
            showInfo(context.getString(R.string.HumanMore66));
        }
    }

    private void showInfo(String infoString) {
        showInfo(infoString, null);
    }

    private void showInfo(String infoString, Bitmap image) {
        ArrayList<MessageData> list = new ArrayList<MessageData>();
        list.add(new MessageData(null, infoString));
        displayMessage(list);
    }

    private void getAICard() {
        santaseFacade.getGame().setTrickAttackPlayer(santaseFacade.getGame().getHuman());
        santaseFacade.getAICard(santaseFacade.getGame().getComputer());
        checkGameActionStatus(santaseFacade.getGame().getComputer());
    }

    private void checkGameActionStatus(final Player aiPlayer) {

        if (santaseFacade.getGame().containActionStatus(Game.GA_CHANGE)) {
            displayChangeTrumpCard();
        }

        if (santaseFacade.getGame().containActionStatus(Game.GA_CLOSE)) {
            displayCloseGame();
        }

        if (santaseFacade.getGame().containActionStatus(Game.GA_COUPLE)) {

            if (santaseFacade.getGame().getComputer().getPoints(santaseFacade.getGame().getTrumpSuit()) >= SantaseFacade.END_GAME_POINTS) {
                santaseFacade.getGame().calculateFuturePoints(santaseFacade.getGame().getComputer());
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
        message = textDecorator.translateCouple(suit, santaseFacade.getGame().getTrumpSuit(), message);
        showInfo(message, ImageUtil.transformToDisabledImage(santasePainter.getSuitImage(suit)));
    }

    private void displayPlayerCoupleMessageExit(Suit suit) {
        String message = context.getString(R.string.HumanHasACoupleExit);
        message = textDecorator.replaceSuit(suit, message);
        message = textDecorator.translateCouple(suit, santaseFacade.getGame().getTrumpSuit(), message);
        showInfo(message, ImageUtil.transformToDisabledImage(santasePainter.getSuitImage(suit)));
    }

    private void displayCoupleMessagePlayer(Suit suit) {
        String message = context.getString(R.string.HumanHasCoupleOf);
        message = textDecorator.replaceSuit(suit, message);
        message = textDecorator.translateCouple(suit, santaseFacade.getGame().getTrumpSuit(), message);
        showInfo(message, ImageUtil.transformToDisabledImage(santasePainter.getSuitImage(suit)));
    }

    private void displayCoupleMessage(Suit suit) {
        String message = context.getString(R.string.AndroidHasCoupleOf);
        message = textDecorator.replaceSuit(suit, message);
        message = textDecorator.translateCouple(suit, santaseFacade.getGame().getTrumpSuit(), message);
        showInfo(message, ImageUtil.transformToDisabledImage(santasePainter.getSuitImage(suit)));
    }

    private Card getHumanCardUnderPointer(final float x, final float y) {
        Player player = santaseFacade.getGame().getHuman();

        for (int i = 0; i < Rank.getRankCount(); i++) {
            if (i < player.getCards().getSize()) {
                Rectangle rec = santasePainter.getPlayerCardRectangle(santaseFacade.getGame(), i, santasePanel);
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
        } else if (keyCode == NAV_LEFT && santaseFacade.isHumanTurn()) {
            if (canSelectPlayersCard()) {
                Card card = selectNextLeftCard();
                processSelectCard(card);
                return;
            }
        } else if (keyCode == NAV_RIGHT && santaseFacade.isHumanTurn()) {
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
            if (!card.equals(santaseFacade.getGame().getHuman().getSelectedCard())) {
                santaseFacade.getGame().getHuman().setSelectedCard(card);
                invalidateGame();
            }
        }
    }

    /**
     * Returns the position of the first left card.
     * @return int the position of the first left card.
     */
    private int getFirstLeftCardIndex() {
        if (santaseFacade.getGame().getHuman().getCards().getSize() > 0) {
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
            if (current == santaseFacade.getGame().getHuman().getCards().getSize() - 1) {
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
            card = santaseFacade.getGame().getHuman().getCards().getCard(index);
        } while (!santaseFacade.validatePlayerMove(santaseFacade.getGame().getHuman(), santaseFacade.getGame().getComputer(), card).equals(ValidateCode.PLAYER_CAN_PLAY));

        return card;
    }

    /**
     * Returns human selected card index.
     * @return human selected card index.
     */
    private int getHumanSelectedCardIndex() {
        if (santaseFacade.getGame().getHuman().getSelectedCard() != null) {
            for (int i = 0; i < santaseFacade.getGame().getHuman().getCards().getSize(); i++) {
                final Card card = santaseFacade.getGame().getHuman().getCards().getCard(i);

                if (santaseFacade.getGame().getHuman().getSelectedCard().equals(card)) {
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
        if (santaseFacade.getGame().getHuman().getCards().getSize() > 0) {
            return santaseFacade.getGame().getHuman().getCards().getSize() - 1;
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
            card = santaseFacade.getGame().getHuman().getCards().getCard(index);
        } while (!santaseFacade.validatePlayerMove(santaseFacade.getGame().getHuman(), santaseFacade.getGame().getComputer(), card).equals(ValidateCode.PLAYER_CAN_PLAY));

        return card;
    }

    public void invalidateGame() {
        invalidateGame(0);
    }

    public void invalidateGame(int delay) {
        Canvas canvas = santasePanel.getBufferedCanvas();
        if (canvas != null) {
            santasePainter.drawGame(santasePanel.getBufferedCanvas(), santaseFacade.getGame(), santasePanel, delay);
            santasePanel.refresh();
        }
    }

    private boolean canSelectPlayersCard() {
        return santaseFacade.isHumanTurn();
    }

    private boolean canPlayerPlaySelectedCard() {
        return santaseFacade.getGame().getHuman().getSelectedCard() != null && santaseFacade.isHumanTurn();
    }

    private boolean checkClickForEndGame() {
        if (santaseFacade.getGame().isObligatoryMode() && (santaseFacade.getGame().getComputer().getCards().getSize() != 0)) {
            displayEndGameMessage(santaseFacade.getGame().getTrickAttackPlayer());
        } else {
            displayEndGameMessageOnLastHand(santaseFacade.getGame().getTrickAttackPlayer());
        }

        newGame(santaseFacade.getGame().getTrickAttackPlayer());

        return true;
    }

    private void displayEndGameMessageOnLastHand(final Player gPlayer) {
        String text;

        if (santaseFacade.getGame().isClosedGame()
                && (gPlayer.equals(santaseFacade.getGame().getPlayerClosedGame()) && gPlayer.getPoints(santaseFacade.getGame().getTrumpSuit()) < SantaseFacade.END_GAME_POINTS)) {
            text = gPlayer.equals(santaseFacade.getGame().getComputer()) ? context.getString(R.string.AndroidClosedAndLostTheGame) : context
                    .getString(R.string.HumanClosedAndLostTheGame);
        } else {
            text = gPlayer.equals(santaseFacade.getGame().getComputer()) ? context.getString(R.string.AndroidGotLastHand) : context.getString(R.string.HumanGotLastHand);
        }

        showInfo(text);
    }

    public void onExit() {
        Santase.terminate(context);
        context.finish();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        // DN
    }
}