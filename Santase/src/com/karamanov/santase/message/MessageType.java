/*
 * Copyright (c) Dimitar Karamanov 2008-2010. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the source code must retain
 * the above copyright notice and the following disclaimer.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 */
package com.karamanov.santase.message;

/**
 * UserMessageType class used in SystemMessage class as type.
 * @author Dimitar Karamanov
 */
public final class MessageType {
    
    public final static MessageType MT_KEY_PRESSED = new MessageType("MT_KEY_PRESSED");

    public final static MessageType MT_TOUCH_EVENT = new MessageType("MT_TOUCH_EVENT");

    public final static MessageType MT_EXIT_EVENT = new MessageType("MT_EXIT_EVENT");

    public final static MessageType MT_PAINT_EVENT = new MessageType("MT_PAINT_EVENT");
    
    public final static MessageType MT_CLOSE_END_GAME = new MessageType("MT_CLOSE_END_GAME");
    
    /**
     * Type ID.
     */
    protected final String type;

    /**
     * Constructor.
     * @param type ID.
     */
    protected MessageType(final String type) {
        this.type = type;
    }

    /**
     * The method checks if this MessageType and specified object (MessageType) are equal.
     * @param obj specified object.
     * @return true if this MessageType is equal to specified object and false otherwise.
     */
    public boolean equals(final Object obj) {
        if (obj instanceof MessageType) {
            final MessageType messageType = (MessageType) obj;
            return type.equals(messageType.type);
        }
        return false;
    }

    /**
     * Returns hash code generated on message type ID value.
     * @return int hash code.
     */
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.type != null ? this.type.hashCode() : 0);
        return hash;
    }
}
