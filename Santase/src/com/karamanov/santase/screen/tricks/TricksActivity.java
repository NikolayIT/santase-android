package com.karamanov.santase.screen.tricks;

import game.beans.pack.Pack;
import game.beans.pack.PackIterator;
import game.beans.pack.card.Card;
import game.logic.SantaseFacade;

import com.karamanov.santase.R;
import com.karamanov.santase.Santase;
import com.karamanov.santase.graphics.PictureDecorator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

public class TricksActivity extends Activity {

    public TricksActivity() {
        super();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);

        LinearLayout vertical = new LinearLayout(this);
        vertical.setOrientation(LinearLayout.VERTICAL);

        SantaseFacade game = Santase.getSantaseFacade(this);
        Pack hands = game.getGame().getHuman().getHands();

        for (PackIterator i = hands.iterator(); i.hasNext();) {
            Card pCard = i.next();
            if (i.hasNext()) {
                Card cCard = i.next();
                HandView tv = new HandView(this, pCard, cCard);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.CENTER_HORIZONTAL;
                tv.setLayoutParams(lp);
                vertical.addView(tv);
            }
        }

        scroll.addView(vertical);
        scroll.setBackgroundResource(R.drawable.score_bkg);
        setContentView(scroll);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            finish();
        }
        return super.onTouchEvent(event);
    }
}

class HandView extends LinearLayout {
    public HandView(Context context, Card pCard, Card cCard) {
        super(context);

        int dip1 = Santase.fromPixelToDip(context, 2);
        setOrientation(LinearLayout.HORIZONTAL);

        PictureDecorator pd = new PictureDecorator(context);

        Card[] cards = new Card[] { pCard, cCard };
        for (Card card : cards) {
            ImageView iv = new ImageView(context);
            Bitmap picture = pd.getCardImage(card);
            iv.setImageBitmap(picture);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.setMargins(dip1, dip1, dip1, dip1);
            iv.setLayoutParams(lp);
            addView(iv);
        }
    }
}
