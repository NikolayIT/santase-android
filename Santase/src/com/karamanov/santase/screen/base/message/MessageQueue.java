/*
 * Copyright (c) Dimitar Karamanov 2008-2010. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the source code must retain
 * the above copyright notice and the following disclaimer.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 */
package com.karamanov.santase.screen.base.message;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * MessageQueue class.
 * @author Dimitar Karamanov
 */
public final class MessageQueue {

    /**
     * Internal container object.
     */
    private final ArrayList<Message> messageList = new ArrayList<Message>();

    /**
     * Hash table which maps messages with handlers.
     */
    private final Hashtable<MessageType, Messageable> listenersHash = new Hashtable<MessageType, Messageable>();

    /**
     * Constructor.
     */
    public MessageQueue() {
        super();
    }

    /**
     * Adds message to the list.
     * @param message new message.
     */
    public final void addMessage(final Message message) {
    	synchronized (messageList) {
			if (message != null) {
				messageList.add(message);
				messageList.notify();
			}
		}
    }

    /**
     * Returns one message from the queue.
     * @return Message extracted from queue.
     */
    private Message getMessage() {
    	synchronized (messageList) {
			while (messageList.size() == 0) {
				try {
					messageList.wait();
				} catch (Exception e) {
				}
			}
			if (messageList.size() > 0) {
				final Message message = (Message) messageList.get(0);
				messageList.remove(message);
				return message;
			}
		}
        return null;
    }

    /**
     * Adds message listener for the concrete message type.
     * @param messageType concrete message type.
     * @param messageable message listener.
     */
    public final void addMessageListener(final MessageType messageType, final Messageable messageable) {
        if (listenersHash.containsKey(messageType)) {
            listenersHash.remove(messageType);
        }
        listenersHash.put(messageType, messageable);
    }

    /**
     * Removes message listener for the concrete message type.
     * @param messageType concrete message type.
     */
    public final void removeMessageListener(final MessageType messageType) {
        listenersHash.remove(messageType);
    }

    /**
     * Returns true if there are messages int the queue false otherwise.
     * @return boolean true if there are messages int the queue false otherwise.
     */
    public final boolean hasMessage() {
        return messageList.size() > 0;
    }

    /**
     * Process one message.
     */
    public final void processMessage() {
        final Message message = getMessage();
        if (message != null) {
            final Messageable messageable = (Messageable) listenersHash.get(message.getMessageType());
            if (messageable != null) {
                messageable.performMessage(message);
            }
        }
    }
}
