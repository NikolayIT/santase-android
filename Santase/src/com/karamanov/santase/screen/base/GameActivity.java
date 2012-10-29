/*

 * 
 * 
 * 
 * 
 *
 * 
 * 
 * 
 * aS
 * Co'
 * 
 * '
 * [P-
 * -[-
 [* \          ba./sat (c) Dimitar Karamanov 2008-2010. All Rights Reserved.[ 
 KKKKKKKK
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the source code must retain
 * the above copyright notice and the following disclaimer.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 */
package com.karamanov.santase.screen.base;

import android.app.Activity;
import android.os.Bundle;

import com.karamanov.santase.Santase;
import com.karamanov.santase.message.Message;
import com.karamanov.santase.message.MessageType;
import com.karamanov.santase.message.Messageable;

/**
 * FrameGameCanvas class.
 * @author Dimitar Karamanov
 */
public class GameActivity extends Activity {

    /**
     * Constructor.
     * @param suppressKeyEvents indicates if the key events are suppressed.
     */
    public GameActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inits listener for a system message type.
     * @param messageType for which will be init listener.
     * @param messageTypeKey message type key ID.
     * @param listener for the concrete system message type.
     */
    /*
     * private SystemMessageType initSystemMessageTypeListener(final String messageTypeKey, final Messageable listener) { SystemMessageType result; result =
     * MessageTypeRegister.getRegister().getSystemMessageType(messageTypeKey); if (result == null) { result =
     * MessageTypeRegister.getRegister().registerSystemMessageType(messageTypeKey); } messageQueue.addMessageListener(result, listener);
     * 
     * return result; }
     */
    
    /**
     * The canvas is being displayed. Stop the event handling and animation thread.
     */
    protected void onResume() {
        super.onResume();
    }

    /**
     * The canvas is being removed from the screen. Stop the event handling and animation thread.
     */
    protected void onPause() {
        super.onPause();
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
     * Adds user message to the end of the queue.
     * @param message new message.
     */
    public final void triggerMessage(final Message message) {
        if (getApplication() instanceof Santase) {
            Santase santase = (Santase) getApplication();
            santase.getMessageProcessor().sendMessage(message);
        }
    }

    /**
     * Adds message listener for the concrete message type.
     * @param messageType concrete user message type.
     * @param messageable message listener.
     */
    public final void addMessageListener(final MessageType messageType, final Messageable messageable) {
        if (getApplication() instanceof Santase) {
            Santase santase = (Santase) getApplication();
            santase.getMessageProcessor().addMessageListener(messageType, messageable);
        }
    }

    /**
     * Removes message listener for the concrete message type.
     * @param messageType concrete user message type.
     */
    public final void removeMessageListener(final MessageType messageType) {
        if (getApplication() instanceof Santase) {
            Santase santase = (Santase) getApplication();
            santase.getMessageProcessor().removeMessageListener(messageType);
        }
    }
}
