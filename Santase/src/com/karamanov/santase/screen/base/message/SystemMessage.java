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
 * SystemMessage class used for system defined messages.
 * @author Dimitar Karamanov
 */
public final class SystemMessage extends Message {

    /**
     * Constructor.
     * @param messageType message type.
     */
    public SystemMessage(final SystemMessageType messageType) {
        super(messageType);
    }

    /**
     * Constructor.
     * @param messageType message type.
     * @param data message data.
     */
    public SystemMessage(final SystemMessageType messageType, final Object data) {
        super(messageType, data);
    }
}
