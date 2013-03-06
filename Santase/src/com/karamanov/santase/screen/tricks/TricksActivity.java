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
            Card winnerCard = i.next();
            if (i.hasNext()) {
                Card loserCard = i.next();
                HandView handView = new HandView(this, winnerCard, loserCard);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER_HORIZONTAL;
                handView.setLayoutParams(params);
                vertical.addView(handView);
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

    public HandView(Context context, Card winnerCard, Card loserCard) {
        super(context);

        int dip1 = Santase.fromPixelToDip(context, 2);
        setOrientation(LinearLayout.HORIZONTAL);

        PictureDecorator pictureDecorator = new PictureDecorator(context);

        Card[] cards = new Card[] { winnerCard, loserCard };
        for (Card card : cards) {
            ImageView imageView = new ImageView(context);
            Bitmap picture = pictureDecorator.getCardImage(card);
            imageView.setImageBitmap(picture);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(dip1, dip1, dip1, dip1);
            imageView.setLayoutParams(params);
            addView(imageView);
        }
    }
}
