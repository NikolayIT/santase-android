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

/**
 * MessageTypeRegister class.
 * 
 * @author Dimitar Karamanov
 */
public final class MessageTypeRegister {

	/**
	 * Messages types list.
	 */
	private final ArrayList<MessageType> types = new ArrayList<MessageType>();

	/**
	 * The only one class instance.
	 */
	private final static MessageTypeRegister register = new MessageTypeRegister();

	/**
	 * Constructor. Singleton object.
	 */
	private MessageTypeRegister() {
		super();
	}

	/**
	 * Returns the singleton instance of the register.
	 * 
	 * @return the singleton instance of the register.
	 */
	public static MessageTypeRegister getRegister() {
		return register;
	}

	/**
	 * Registers new user message type.
	 * 
	 * @param type
	 *            message type.
	 * @return MessageType instance.
	 */
	public UserMessageType registerUserMessageType(final String type) {
		UserMessageType result = null;

		if (!hasMessageType(type, UserMessageType.class)) {
			result = new UserMessageType(type);
			types.add(result);
		}

		return result;
	}

	/**
	 * Registers new user message type.
	 * 
	 * @param type
	 *            message type.
	 * @return MessageType instance.
	 */
	public SystemMessageType registerSystemMessageType(final String type) {
		SystemMessageType result = null;

		if (!hasMessageType(type, SystemMessageType.class)) {
			result = new SystemMessageType(type);
			types.add(result);
		}

		return result;
	}

	/**
	 * Returns true if the provided type has been registered before, false
	 * otherwise.
	 * 
	 * @param type
	 *            ID.
	 * @return boolean true if the provided type has been registered before,
	 *         false otherwise.
	 */
	private boolean hasMessageType(final String type, final Class<? extends MessageType> messageClass) {
		if (getMessageType(type, messageClass) == null) {
			return false;
		}
		return true;
	}

	/**
	 * Returns system message type by ID.
	 * 
	 * @param type
	 *            ID.
	 * @return MessageType system message type.
	 */
	public SystemMessageType getSystemMessageType(final String type) {
		return (SystemMessageType) getMessageType(type, SystemMessageType.class);
	}

	/**
	 * Returns message type by ID.
	 * 
	 * @param type
	 *            message type.
	 * @return MessageType message type.
	 */
	public UserMessageType getUserMessageType(final String type) {
		return (UserMessageType) getMessageType(type, UserMessageType.class);
	}

	/**
	 * Returns message type by ID ans class type.
	 * 
	 * @param type
	 *            message type.
	 * @param messageClass
	 *            class type.
	 * @return MessageType message type.
	 */
	private MessageType getMessageType(final String type, final Class<? extends MessageType> messageClass) {
		for (MessageType messageType : types) {
			if (messageType.type.equals(type) && messageType.getClass().equals(messageClass)) {
				return messageType;
			}
		}
		return null;
	}
}
