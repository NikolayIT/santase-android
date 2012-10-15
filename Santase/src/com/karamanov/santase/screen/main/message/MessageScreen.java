package com.karamanov.santase.screen.main.message;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.karamanov.santase.R;
import com.karamanov.santase.screen.base.BooleanFlag;

public class MessageScreen extends Dialog {

    private final BooleanFlag flag;

    public MessageScreen(Context context, ArrayList<MessageData> messages, BooleanFlag flag) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setBackgroundDrawableResource(R.drawable.message_dialog_shape);

        this.flag = flag;
        MessagePanel messagePanel = new MessagePanel(context, messages);
        setContentView(messagePanel);
    }

    protected void onStop() {
        flag.setFalse();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        dismiss();
        return true;
    }

    /**
     * Invoked when the navigational action is selected.
     * @param status - Bitfield of values defined by KeypadListener.
     * @param time - Number of milliseconds since the device was turned on.
     */
    public boolean onTouchEvent(MotionEvent event) {
        dismiss();
        return true;
    }
}
