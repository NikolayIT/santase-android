/*
 * Copyright (c) Dimitar Karamanov 2008-2009. All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by DK as part of a product of Dimitar Karamanov
 * for use ONLY by licensed users of the product, includes CONFIDENTIAL and PROPRIETARY information.
 */
package game.logic.strategy.automat.executors.base;

import game.logic.strategy.automat.base.PlayCardMethod;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * PlayCardMethodList class. Wrapper class of system collection used to hold and
 * access PlayCardMethod instances.
 * 
 * @author Dimitar Karamanov
 */
public final class PlayCardMethodList {
	/**
	 * Internal container collection.
	 */
	private ArrayList<PlayCardMethod> collection = new ArrayList<PlayCardMethod>();

	/**
	 * Constructor.
	 */
	public PlayCardMethodList() {
		super();
	}

	/**
	 * Clears all elements from the collection.
	 */
	public void clear() {
		collection.clear();
	}

	/**
	 * Adds a method to the collection.
	 * 
	 * @param method to be added
	 */
	public void add(PlayCardMethod method) {
		collection.add(method);
	}

	/**
	 * Returns collection size.
	 * 
	 * @return int size of the method's collection
	 */
	public int size() {
		return collection.size();
	}

	/**
	 * Returns iterator for the collection.
	 * 
	 * @return PlayCardMethodIterator iterator.
	 */
	public PlayCardMethodIterator iterator() {
		return new PlayCardMethodIteratorImpl(collection.iterator());
	}

	/**
	 * PlayableIteratorImpl class. Implements PlayCardMethodIterator interface.
	 */
	private class PlayCardMethodIteratorImpl implements PlayCardMethodIterator {
		/**
		 * The internal collection enumerator.
		 */
		private final Iterator<PlayCardMethod> enumeration;

		/**
		 * Constructor.
		 * 
		 * @param enumerator the internal collection enumerator.
		 */
		public PlayCardMethodIteratorImpl(Iterator<PlayCardMethod> enumeration) {
			this.enumeration = enumeration;
		}

		/**
		 * Returns true if the iteration has more elements.
		 * 
		 * @return boolean true if the iteration has more elements false
		 *         otherwise.
		 */
		public boolean hasNext() {
			return enumeration.hasNext();
		}

		/**
		 * Returns the next element in the iteration.
		 * 
		 * @return Playable the next element in the iteration.
		 */
		public PlayCardMethod next() {
			return enumeration.next();
		}
	}
}
