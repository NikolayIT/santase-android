package com.karamanov.santase.screen.main.tip;


import game.beans.Player;

import java.util.ArrayList;

import com.karamanov.santase.R;
import com.karamanov.santase.screen.main.message.MessageData;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class TipScreen extends Dialog {
	
	public TipScreen(Context context, Player player, ArrayList<MessageData> messages) {
		super(context);		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setBackgroundDrawableResource(R.drawable.message_shape);
        
        TipPanel tipPanel = new TipPanel(context, player, messages);
		setContentView(tipPanel);
	}
	
	protected void onStop () {
		super.onStop();
	}
	
    public boolean onKeyDown (int keyCode, KeyEvent event) {
    	dismiss();
		return true;
    }
		
	 /**
     * Invoked when the navigational action is selected.
     * @param status - Bitfield of values defined by KeypadListener.
     * @param time - Number of milliseconds since the device was turned on.
     */
    public boolean onTouchEvent (MotionEvent event) {
    	dismiss();
        return true;
    }
}
