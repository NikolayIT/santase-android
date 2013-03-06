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
package com.karamanov.framework;

import android.app.Activity;
import android.os.Bundle;

import com.karamanov.framework.message.Message;
import com.karamanov.framework.message.MessageType;
import com.karamanov.framework.message.Messageable;

/**
 * FrameGameCanvas class.
 * @author Dimitar Karamanov
 */
public class MessageActivity extends Activity {

    /**
     * Constructor.
     * @param suppressKeyEvents indicates if the key events are suppressed.
     */
    public MessageActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getApplication() instanceof MessageApplication) {
            MessageApplication messageApplication = (MessageApplication) getApplication();
            messageApplication.getMessageProcessor().runMessaging();
        }
    }

    /**
     * The canvas is being displayed. Stop the event handling and animation thread.
     */
    protected void onResume() {
        super.onResume();
        if (getApplication() instanceof MessageApplication) {
            MessageApplication messageApplication = (MessageApplication) getApplication();
            messageApplication.getMessageProcessor().runMessaging();
        }
    }

    /**
     * The canvas is being removed from the screen. Stop the event handling and animation thread.
     */
    protected void onPause() {
        if (getApplication() instanceof MessageApplication) {
            MessageApplication messageApplication = (MessageApplication) getApplication();
            messageApplication.getMessageProcessor().stopMessaging();
        }
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
            }
        }
    }

    /**
     * Adds user message to the end of the queue.
     * @param message new message.
     */
    public final void triggerMessage(final Message message) {
        triggerMessage(message, false);
    }

    /**
     * Adds user message to the end of the queue.
     * @param message new message.
     */
    public final void triggerMessage(final Message message, boolean always) {
        if (getApplication() instanceof MessageApplication) {
            MessageApplication messageApplication = (MessageApplication) getApplication();
            messageApplication.getMessageProcessor().sendMessage(message, always);
        }
    }

    /**
     * Adds message listener for the concrete message type.
     * @param messageType concrete user message type.
     * @param messageable message listener.
     */
    public final void addMessageListener(final MessageType messageType, final Messageable messageable) {
        if (getApplication() instanceof MessageApplication) {
            MessageApplication messageApplication = (MessageApplication) getApplication();
            messageApplication.getMessageProcessor().addMessageListener(messageType, messageable);
        }
    }

    /**
     * Removes message listener for the concrete message type.
     * @param messageType concrete user message type.
     */
    public final void removeMessageListener(final MessageType messageType) {
        if (getApplication() instanceof MessageApplication) {
            MessageApplication messageApplication = (MessageApplication) getApplication();
            messageApplication.getMessageProcessor().removeMessageListener(messageType);
        }
    }
}
