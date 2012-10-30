package com.karamanov.framework;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.karamanov.framework.message.MessageProcessor;

public class MessageApplication extends Application {

    private final MessageProcessor messageProcessor;

    public MessageApplication() {
        super();
        messageProcessor = new MessageProcessor();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        messageProcessor.start();
    }

    public final MessageProcessor getMessageProcessor() {
        return messageProcessor;
    }

    public static int fromPixelToDip(Context context, int pixels) {
        Resources resources = context.getResources();
        if (pixels == 1) {
            return Math.max(1, Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, resources.getDisplayMetrics())));
        } else {
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, resources.getDisplayMetrics()));
        }
    }

    public static float fromPixelToDipF(Context context, float pixels) {
        Resources resources = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, resources.getDisplayMetrics());
    }
}
