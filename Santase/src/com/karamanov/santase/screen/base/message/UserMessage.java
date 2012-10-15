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

/**
 * UserMessage class used for user defined messages.
 * @author Dimitar Karamanov
 */
public final class UserMessage extends Message {

    /**
     * Constructor.
     * @param messageType message type.
     */
    public UserMessage(final UserMessageType messageType) {
        super(messageType);
    }

    /**
     * Constructor.
     * @param messageType message type.
     * @param data message data.
     */
    public UserMessage(final UserMessageType messageType, final Object data) {
        super(messageType, data);
    }
}
