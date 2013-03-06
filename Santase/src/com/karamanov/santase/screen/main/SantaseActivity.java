package com.karamanov.santase.screen.main;

import game.beans.Game;
import game.beans.pack.card.Card;
import game.logic.SantaseFacade;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.karamanov.framework.MessageActivity;
import com.karamanov.framework.message.Message;
import com.karamanov.framework.message.Messageable;
import com.karamanov.santase.R;
import com.karamanov.santase.Santase;
import com.karamanov.santase.graphics.PictureDecorator;
import com.karamanov.santase.screen.main.message.MessageData;
import com.karamanov.santase.screen.main.tip.TipScreen;
import com.karamanov.santase.screen.pref.SantasePreferencesActivity;
import com.karamanov.santase.screen.tricks.TricksActivity;
import com.karamanov.santase.text.TextDecorator;

public class SantaseActivity extends MessageActivity implements OnSharedPreferenceChangeListener {

    private Dealer dealer;

    private RelativeLayout buttons;

    private SantaseView santaseView;

    private RelativeLayout relative;

    public static final int NAV_PRESS = -1;

    public static final int NAV_LEFT = -2;

    public static final int NAV_RIGHT = -3;

    private final static int CLOSE_GAME_INDEX = 1;

    private static final int GAME_NEW_INDEX = 2;

    private static final int PLAYED_CARDS_INDEX = 3;

    private static final int TIP_CARD_INDEX = 4;

    private static final int PREF_INDEX = 5;

    /**
     * Constructor.
     */
    public SantaseActivity() {
        super();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addMessageListener(Santase.MT_KEY_PRESSED, new KeyPressedListener());
        addMessageListener(Santase.MT_TOUCH_EVENT, new TouchListener());
        addMessageListener(Santase.MT_EXIT_EVENT, new ExitListener());
        addMessageListener(Santase.MT_PAINT_EVENT, new PaintListener());

        buttons = new RelativeLayout(this);
        buttons.setId(1);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        buttons.setLayoutParams(rlp);

        ImageButton left = new ImageButton(this);
        left.setBackgroundResource(R.drawable.btn_left);
        rlp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        rlp.addRule(RelativeLayout.CENTER_VERTICAL);
        left.setLayoutParams(rlp);
        left.setOnClickListener(new ButtonPressListener(Integer.valueOf(NAV_LEFT)));
        left.setSoundEffectsEnabled(false);
        buttons.addView(left);

        ImageButton play = new ImageButton(this);
        play.setBackgroundResource(R.drawable.btn_play);
        rlp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
        play.setLayoutParams(rlp);
        play.setOnClickListener(new ButtonPressListener(Integer.valueOf(NAV_PRESS)));
        play.setSoundEffectsEnabled(false);
        buttons.addView(play);

        ImageButton right = new ImageButton(this);
        right.setBackgroundResource(R.drawable.btn_right);
        rlp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rlp.addRule(RelativeLayout.CENTER_VERTICAL);
        right.setLayoutParams(rlp);
        right.setOnClickListener(new ButtonPressListener(Integer.valueOf(NAV_RIGHT)));
        right.setSoundEffectsEnabled(false);
        buttons.addView(right);

        relative = new RelativeLayout(this);
        relative.addView(buttons);

        santaseView = new SantaseView(this);

        dealer = new Dealer(this, santaseView);
        rlp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        rlp.addRule(RelativeLayout.ABOVE, buttons.getId());
        santaseView.setLayoutParams(rlp);
        relative.addView(santaseView);

        setContentView(relative);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean showBtns = preferences.getBoolean(getString(R.string.prefShowBtns), Boolean.TRUE);
        buttons.setVisibility(showBtns ? View.VISIBLE : View.GONE);

        repaint();
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.unregisterOnSharedPreferenceChangeListener(this);

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        int base = Menu.CATEGORY_SECONDARY;

        MenuItem newMenu = menu.add(base, base + GAME_NEW_INDEX, base + GAME_NEW_INDEX, getString(R.string.New));
        newMenu.setIcon(android.R.drawable.ic_menu_rotate);

        MenuItem prefMenu = menu.add(base, base + PREF_INDEX, base + PREF_INDEX, getString(R.string.PREFERENCES));
        prefMenu.setIcon(android.R.drawable.ic_menu_manage);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        int base = Menu.CATEGORY_SECONDARY;

        SantaseFacade santaseFacade = Santase.getSantaseFacade(this);
        boolean showClose = santaseFacade.getGame().canClose();

        MenuItem showMenu = menu.findItem(base + CLOSE_GAME_INDEX);
        if (showClose) {
            if (showMenu == null) {
                showMenu = menu.add(base, base + CLOSE_GAME_INDEX, base + CLOSE_GAME_INDEX, getString(R.string.CloseGame));
                showMenu.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
            }
        } else {
            if (showMenu != null) {
                menu.removeItem(base + CLOSE_GAME_INDEX);
            }
        }

        MenuItem historyMenu = menu.findItem(base + PLAYED_CARDS_INDEX);
        if (santaseFacade.getGame().getHuman().getHands().getSize() > 0) {
            if (historyMenu == null) {
                historyMenu = menu.add(base, base + PLAYED_CARDS_INDEX, base + PLAYED_CARDS_INDEX, getString(R.string.PastTricks));
                historyMenu.setIcon(R.drawable.ic_menu_tricks);
            }
        } else {
            if (historyMenu != null) {
                menu.removeItem(base + PLAYED_CARDS_INDEX);
            }
        }

        MenuItem tipsMenu = menu.findItem(base + TIP_CARD_INDEX);
        boolean showTips = santaseFacade.isHumanTurn() && santaseFacade.getGame().getHuman().getPlayedCard() == null;
        if (showTips) {
            if (tipsMenu == null) {
                tipsMenu = menu.add(base, base + TIP_CARD_INDEX, base + TIP_CARD_INDEX, getString(R.string.Tip));
                tipsMenu.setIcon(android.R.drawable.ic_menu_info_details);
            }
        } else {
            if (tipsMenu != null) {
                menu.removeItem(base + TIP_CARD_INDEX);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = super.onOptionsItemSelected(item);

        int base = Menu.CATEGORY_SECONDARY;

        if (item.getItemId() == base + CLOSE_GAME_INDEX) {
            dealer.turnToPlayerClosedMode();
            return true;
        }

        if (item.getItemId() == base + GAME_NEW_INDEX) {
            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
            myAlertDialog.setTitle(getString(R.string.Confirm));
            myAlertDialog.setMessage(getString(R.string.NewEraseQuestion));

            myAlertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    Santase.resetGame(SantaseActivity.this);
                    repaint();
                }
            });

            myAlertDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    //
                }
            });

            myAlertDialog.setCancelable(false);
            myAlertDialog.show();
            return true;
        }

        if (item.getItemId() == base + PLAYED_CARDS_INDEX) {
            Intent intent = new Intent(this, TricksActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }

        if (item.getItemId() == base + PREF_INDEX) {
            Intent intent = new Intent(this, SantasePreferencesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }

        if (item.getItemId() == base + TIP_CARD_INDEX) {
            createTipDialog();
            return true;
        }

        return result;
    }

    private void createTipDialog() {
        SantaseFacade santaseFacade = Santase.getSantaseFacade(this);

        ArrayList<MessageData> messages = new ArrayList<MessageData>();
        Card card = santaseFacade.getTipMessageCard(santaseFacade.getGame().getHuman());

        if (card != null) {
            PictureDecorator decorator = new PictureDecorator(this);

            int i = 1;
            if (santaseFacade.getGame().containActionStatus(Game.GA_CHANGE)) {
                messages.add(new MessageData(addNumberToTip(i, getString(R.string.ChangeTrumpCard))));
                i++;
            }
            if (santaseFacade.getGame().containActionStatus(Game.GA_CLOSE)) {
                messages.add(new MessageData(addNumberToTip(i, getString(R.string.CloseTheGame))));
                i++;
            }
            if (santaseFacade.getGame().containActionStatus(Game.GA_COUPLE)) {
                TextDecorator textDecorator = new TextDecorator(this);
                String message = getString(R.string.HumanHasCoupleOf);
                message = textDecorator.replaceSuit(card.getSuit(), message);
                message = textDecorator.translateCouple(card.getSuit(), santaseFacade.getGame().getTrumpSuit(), message);
                messages.add(new MessageData(decorator.getSuitImage(card.getSuit()), addNumberToTip(i, message)));
                i++;
            }
            messages.add(new MessageData(decorator.getCardImage(card), addNumberToTip(i, getString(R.string.PlayCard))));

            TipScreen tipScreen = new TipScreen(this, santaseFacade.getGame().getHuman(), messages);
            tipScreen.show();
        }
    }

    private String addNumberToTip(int i, String tip) {
        return i + ". " + tip;
    }

    @Override
    public void onBackPressed() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String key = getString(R.string.prefAlertOnQuit);
        boolean alertOnQuit = preferences.getBoolean(key, Boolean.TRUE);

        if (alertOnQuit) {
            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
            myAlertDialog.setTitle(getString(R.string.Confirm));
            myAlertDialog.setMessage(getString(R.string.ExitQuestion));
            myAlertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    Message tMessage = new Message(Santase.MT_EXIT_EVENT);
                    triggerMessage(tMessage);
                }
            });
            myAlertDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    //
                }
            });

            myAlertDialog.setCancelable(false);
            myAlertDialog.show();
        } else {
            Message tMessage = new Message(Santase.MT_EXIT_EVENT);
            triggerMessage(tMessage);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.prefShowBtns))) {
            boolean showBtns = sharedPreferences.getBoolean(key, Boolean.TRUE);
            if (buttons != null) {
                buttons.setVisibility(showBtns ? View.VISIBLE : View.GONE);
                repaint();
                if (santaseView != null) {
                    santaseView.invalidate();
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dealer.onConfigurationChanged(newConfig);
    }

    public void repaint() {
        Message tMessage = new Message(Santase.MT_PAINT_EVENT);
        triggerMessage(tMessage, true);
    }

    private class KeyPressedListener implements Messageable {

        public void performMessage(Message message) {
            if (message.getData() instanceof Integer) {
                Integer integer = (Integer) message.getData();
                dealer.checkKeyPressed(integer.intValue());
            }
        }
    }

    private class TouchListener implements Messageable {

        public void performMessage(Message message) {
            if (message.getData() instanceof PointF) {
                PointF xy = (PointF) message.getData();
                dealer.checkClick(xy.x, xy.y);
            }
        }
    }

    private class ExitListener implements Messageable {

        @Override
        public void performMessage(Message message) {
            Santase.terminate(SantaseActivity.this);
            dealer.onExit();
        }
    }

    private class PaintListener implements Messageable {

        @Override
        public void performMessage(Message message) {
            dealer.invalidateGame();
        }
    }

    private class ButtonPressListener implements OnClickListener {

        private final Integer i;

        public ButtonPressListener(Integer i) {
            this.i = i;
        }

        @Override
        public void onClick(View view) {
            Message tMessage = new Message(Santase.MT_KEY_PRESSED, i);
            triggerMessage(tMessage);
        }
    }
}