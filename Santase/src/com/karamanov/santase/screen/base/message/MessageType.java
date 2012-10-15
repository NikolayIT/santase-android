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
 * MessageType class.
 * @author Dimitar Karamanov
 */
public abstract class MessageType {

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
     * Returns hash code generated on message type ID value.
     * @param multiplicator of the result;
     * @return int hash code.
     */
    protected final int hashCode(int multiplicator) {
        int hash = 5;
        hash = 37 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = hash * multiplicator;
        return hash;
    }

    /**
     * Returns hash code generated on message type ID value.
     * @return int hash code.
     */
    public abstract int hashCode();

    /**
     * The method checks if this MessageType and specified object (MessageType) are equal.
     * @param obj specified object.
     * @return true if this MessageType is equal to specified object and false otherwise.
     */
    public abstract boolean equals(final Object obj);
}