package com.karamanov.santase.screen.main;

import game.beans.Game;
import game.beans.Player;
import game.beans.pack.card.Card;
import game.beans.pack.card.rank.Rank;
import game.beans.pack.card.suit.Suit;
import game.logic.SantaseFacade;
import game.logic.strategy.validator.ValidateCode;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;

import com.karamanov.framework.MessageActivity;
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

    public Dealer(MessageActivity context, SantaseView belotPanel) {
        this.context = context;
        this.santaseFacade = Santase.getSantaseFacade(context);
        this.santasePanel = belotPanel;

        handler = new Handler();
        santasePainter = new SantasePainter(context);
        textDecorator = new TextDecorator(context);
    }

    public void checkClick(float x, float y) {
        if (santaseFacade.getGame().isBothPlayed()) {
            performClickForNextTrickOrEndGame();
        } else {
            checkHumanTurnClick(x, y);
        }
    }
    
    private void checkHumanTurnClick(float x, float y) {
        if (santaseFacade.isHumanTurn()) {
            if (isClickedSelectedCard(x, y)) {
                if (canHumanPlaySelectedCard()) {
                    playSelectedHumanCard();
                }
            } else if (santaseFacade.getGame().canClose() && isMouseOverTrumpCard(x, y)) {
                changeTrumpCard();
            } else if (santaseFacade.getGame().canClose() && isMouseOverCloseCard(x, y)) {
                showCloseGameDialog();
            } else {
                checkCardToSelect(x, y);
            }
        }
    }
    
    private void checkCardToSelect(float x, float y) {
        Card card = getHumanCardUnderPointer(x, y);
        Player computer = santaseFacade.getGame().getComputer();
        Player human = santaseFacade.getGame().getHuman();
        
        if (card != null && santaseFacade.validatePlayerCard(human, computer, card).equals(ValidateCode.PLAYER_CAN_PLAY)) {
            human.setSelectedCard(card);
            invalidateGame();
        }        
    }

    private void showCloseGameDialog() {
        handler.post(new Runnable() {
            public void run() {
                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
                myAlertDialog.setTitle(context.getString(R.string.Confirm));
                myAlertDialog.setMessage(context.getString(R.string.CloseGameQuestion));
                myAlertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        turnToPlayerClosedMode();
                        Santase santase = (Santase) context.getApplication();
                        santase.getMessageProcessor().runMessaging();
                    }
                });
                myAlertDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Santase santase = (Santase) context.getApplication();
                        santase.getMessageProcessor().runMessaging();
                    }
                });
                myAlertDialog.show();
            }
        });

        Santase santase = (Santase) context.getApplication();
        santase.getMessageProcessor().stopMessaging();
    }

    private boolean isMouseOverTrumpCard(float x, float y) {
        return santasePainter.getTrumpCardRectangle(santasePanel).include((int) x, (int) y);
    }

    private boolean isMouseOverCloseCard(float x, float y) {
        return santasePainter.getCloseCardRectangle(santasePanel).include((int) x, (int) y);
    }

    private boolean isClickedSelectedCard(float x, float y) {
        return santaseFacade.getGame().getHuman().getSelectedCard() != null && santaseFacade.getGame().getHuman().getSelectedCard().equals(getHumanCardUnderPointer(x, y));
    }

    public void turnToPlayerClosedMode() {
        santaseFacade.getGame().setClosedGame(santaseFacade.getGame().getHuman());
        invalidateGame();
    }

    private void newGame(final Player lastHandPlayer, final String endGameMessage) {
        Player computer = santaseFacade.getGame().getComputer();
        Player human = santaseFacade.getGame().getHuman();
        int humanPoints = santaseFacade.getGame().calculateCurrentGamePlayerPoints(lastHandPlayer, human);
        int computerPoints = santaseFacade.getGame().calculateCurrentGamePlayerPoints(lastHandPlayer, computer);
        
        Player seriesWinner = null;
        if (humanPoints + human.getLittleGames() >= Game.MAX_LITTLE_GAMES) {
            seriesWinner = human;
        }
        
        if (computerPoints + computer.getLittleGames() >= Game.MAX_LITTLE_GAMES) {
            seriesWinner = computer;
        }
        
        if (seriesWinner != null) {
            String message;
            Bitmap bitmap;
            
            if (santaseFacade.getGame().getHuman().equals(seriesWinner)) {
                message = context.getString(R.string.PlayerWinSeries);
                bitmap = santasePainter.getHappy();
            } else {
                message = context.getString(R.string.AndroidWinSeries);
                bitmap = santasePainter.getUnhappy();
            }
            
            ArrayList<MessageData> list = new ArrayList<MessageData>();
            //list.add(new MessageData(endGameMessage));
            list.add(new MessageData(bitmap, endGameMessage + "\n" + message));
            displayMessage(list);
        } else {
            showInfo(endGameMessage);
        }
                
        human.setSelectedCard(null);
        santaseFacade.getGame().newGame(lastHandPlayer);

        invalidateGame(STANDARD_CARD_DELAY);
        if (computer.equals(santaseFacade.getGame().getTrickAttackPlayer())) {
            performComputerCard();
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
            displayMessage(result);
        }
    }

    /**
     * Displays a message.
     * 
     * @param player which call the message function.
     * @param card played by player.
     */
    private void displayMessage(final ArrayList<MessageData> messages) {
        handler.post(new Runnable() {
            public void run() {
                messageScreen = new MessageScreen(context, messages);
                messageScreen.show();
            }
        });

        Santase santase = (Santase) context.getApplication();
        santase.getMessageProcessor().stopMessaging();
    }

    private void playSelectedHumanCard() {
        Player computer = santaseFacade.getGame().getComputer();
        Player human = santaseFacade.getGame().getHuman();
            
        if (computer.getPlayedCard() == null && human.getSelectedCard() != null && human.getCards().hasCouple(human.getSelectedCard())) {
        	human.getCouples().setCouple(human.getSelectedCard().getSuit());
            boolean result = human.getPoints(santaseFacade.getGame().getTrumpSuit()) >= SantaseFacade.END_GAME_POINTS;

            if (result) {
                Card card = human.getSelectedCard();
                removeSelectedHumanCard();
                invalidateGame();
                newGame(human, getPlayerCoupleMessageExit(card.getSuit()));
                return;
            } else {
                invalidateGame();
                displayCoupleMessagePlayer(human.getSelectedCard().getSuit());
            }
        }
                            
        removeSelectedHumanCard();
        santaseFacade.getGame().setTrickAttackPlayer(computer);
        invalidateGame();
        
        sleep(PLAY_DELAY);
        
        if (computer.getPlayedCard() == null) {
            performComputerCard();
        }
                    
        invalidateGame();
    }

    private void removeSelectedHumanCard() {
        Player human = santaseFacade.getGame().getHuman();
        human.getCards().remove(human.getSelectedCard());
        human.setPlayedCard(human.getSelectedCard());
        human.setSelectedCard(null);
    }

    private boolean canHumanPlaySelectedCard() {
        Player human = santaseFacade.getGame().getHuman();
        Player computer = santaseFacade.getGame().getComputer();
        return santaseFacade.validatePlayerCard(human, computer, human.getSelectedCard()).equals(ValidateCode.PLAYER_CAN_PLAY);
    }

    private void performClickForNextTrickOrEndGame() {
        santaseFacade.getGame().processTrick();
        invalidateGame();
            
        if (santaseFacade.getGame().hasMoreTricks()) {
            performClickForNextTrick();
        } else {
            performClickForEndGame();
        }
    }

    private void performClickForNextTrick() {
        Player human = santaseFacade.getGame().getHuman();
        Player computer = santaseFacade.getGame().getComputer();

        if (human.equals(santaseFacade.getGame().getTrickAttackPlayer())) {
            if (santaseFacade.getGame().canEndGame(human)) {
                newGame(human, getEndGameMessage(human));
            }
        } else if (computer.equals(santaseFacade.getGame().getTrickAttackPlayer())) {
            if (santaseFacade.getGame().canEndGame(computer)) {
                newGame(computer, getEndGameMessage(computer));
            } else {
                performComputerCard();
            }
        }
    }

    private String getEndGameMessage(final Player player) {
        if (player.equals(santaseFacade.getGame().getComputer())) {
            return context.getString(R.string.AndroidMore66);
        } else {
            return context.getString(R.string.HumanMore66);
        }
    }

    private void showInfo(String infoString) {
        showInfo(infoString, null);
    }

    private void showInfo(String infoString, Bitmap image) {
        ArrayList<MessageData> list = new ArrayList<MessageData>();
        list.add(new MessageData(image, infoString));
        displayMessage(list);
    }

    private void performComputerCard() {
        santaseFacade.getAICard(santaseFacade.getGame().getComputer());
        checkGameActionStatus(santaseFacade.getGame().getComputer());
        santaseFacade.getGame().setTrickAttackPlayer(santaseFacade.getGame().getHuman());
        invalidateGame();
    }

    private void checkGameActionStatus(final Player player) {
        ArrayList<MessageData> list = new ArrayList<MessageData>();

        if (santaseFacade.getGame().containActionStatus(Game.GA_CHANGE)) {
            list.add(new MessageData(context.getString(R.string.AnnounceChangeTrumpCard)));    
        }

        if (santaseFacade.getGame().containActionStatus(Game.GA_CLOSE)) {
            list.add(new MessageData(context.getString(R.string.AnnounceCloseGame)));
        }

        if (santaseFacade.getGame().containActionStatus(Game.GA_COUPLE)) {
            if (player.getPoints(santaseFacade.getGame().getTrumpSuit()) >= SantaseFacade.END_GAME_POINTS) {
                list.add(new MessageData(getComputerCoupleMessageExit(player.getPlayedCard().getSuit())));
                
                StringBuffer message = new StringBuffer();
                for (Iterator<MessageData> iterator = list.iterator(); iterator.hasNext();) {
                    if (message.length() != 0) {
                        message.append("\n");
                    }
                    MessageData data = iterator.next();
                    message.append(data.getMessage());
                }
                
                list.clear();
                
                newGame(player, message.toString());
            } else {
                String message = context.getString(R.string.AndroidHasCoupleOf);
                message = textDecorator.replaceSuit(player.getPlayedCard().getSuit(), message);
                message = textDecorator.translateCouple(player.getPlayedCard().getSuit(), santaseFacade.getGame().getTrumpSuit(), message);
                list.add(new MessageData(santasePainter.getSuitImage(player.getPlayedCard().getSuit()), message));
            }
        }
        
        if (list.size() > 0) {
            displayMessage(list);
        }
    }

    /*
    public void displayChangeTrumpCard() {
        showInfo(context.getString(R.string.AnnounceChangeTrumpCard));
    }
    */

    /*
    private void displayCloseGame() {
        showInfo(context.getString(R.string.AnnounceCloseGame));
    }
    */

    private String getComputerCoupleMessageExit(Suit suit) {
        String message = context.getString(R.string.AndroidHasACoupleExit);
        message = textDecorator.replaceSuit(suit, message);
        message = textDecorator.translateCouple(suit, santaseFacade.getGame().getTrumpSuit(), message);
        return message;
    }

    private String getPlayerCoupleMessageExit(Suit suit) {
        String message = context.getString(R.string.HumanHasACoupleExit);
        message = textDecorator.replaceSuit(suit, message);
        message = textDecorator.translateCouple(suit, santaseFacade.getGame().getTrumpSuit(), message);
        return message;
    }

    private void displayCoupleMessagePlayer(Suit suit) {
        String message = context.getString(R.string.HumanHasCoupleOf);
        message = textDecorator.replaceSuit(suit, message);
        message = textDecorator.translateCouple(suit, santaseFacade.getGame().getTrumpSuit(), message);
        showInfo(message, santasePainter.getSuitImage(suit));
    }

    /*
    private void displayCoupleMessage(Suit suit) {
        String message = context.getString(R.string.AndroidHasCoupleOf);
        message = textDecorator.replaceSuit(suit, message);
        message = textDecorator.translateCouple(suit, santaseFacade.getGame().getTrumpSuit(), message);
        showInfo(message, santasePainter.getSuitImage(suit));
    }
    */

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
                if (canHumanPlaySelectedCard()) {
                    playSelectedHumanCard();
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
        if (santaseFacade.getGame().isBothPlayed()) {
            performClickForNextTrickOrEndGame();
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
            if (index == -1) {
                return null;
            }
            card = santaseFacade.getGame().getHuman().getCards().getCard(index);
        } while (!santaseFacade.validatePlayerCard(santaseFacade.getGame().getHuman(), santaseFacade.getGame().getComputer(), card).equals(ValidateCode.PLAYER_CAN_PLAY));

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
            if (index == -1) {
                return null;
            }
            card = santaseFacade.getGame().getHuman().getCards().getCard(index);
        } while (!santaseFacade.validatePlayerCard(santaseFacade.getGame().getHuman(), santaseFacade.getGame().getComputer(), card).equals(ValidateCode.PLAYER_CAN_PLAY));

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

    private void performClickForEndGame() {
        String message;
        if (santaseFacade.getGame().isObligatoryMode() && (santaseFacade.getGame().getComputer().getCards().getSize() != 0)) {
            message = getEndGameMessage(santaseFacade.getGame().getTrickAttackPlayer());
        } else {
            message = getEndGameMessageOnLastHand(santaseFacade.getGame().getTrickAttackPlayer());
        }

        newGame(santaseFacade.getGame().getTrickAttackPlayer(), message);
    }

    private String getEndGameMessageOnLastHand(final Player lastHandPlayer) {
        Player computer = santaseFacade.getGame().getComputer();
        Player closer = santaseFacade.getGame().getPlayerClosedGame();
        if (santaseFacade.getGame().isClosedGame() && closer.getPoints(santaseFacade.getGame().getTrumpSuit()) < SantaseFacade.END_GAME_POINTS) {
            return computer.equals(closer) ? context.getString(R.string.AndroidClosedAndLostTheGame) : context.getString(R.string.HumanClosedAndLostTheGame);
        } else {
            return lastHandPlayer.equals(computer) ? context.getString(R.string.AndroidGotLastHand) : context.getString(R.string.HumanGotLastHand);
        }
    }

    public void onExit() {
        context.finish();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        // DN
    }
}