/*
 * Copyright (c) Dimitar Karamanov 2008-2010. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the source code must retain
 * the above copyright notice and the following disclaimer.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 */
package com.karamanov.santase.text;

import game.beans.pack.card.rank.Rank;
import game.beans.pack.card.suit.Suit;

import java.util.Hashtable;

import android.content.Context;

import com.karamanov.santase.R;

/**
 * TextDecorator class.
 * @author Dimitar Karamanov
 */
public final class TextDecorator {

    private static final String COUPLE = "COUPLE";

    private static final String SUIT = "SUIT";

    /**
     * Rank signs container.
     */
    private final Hashtable<Rank, String> rankSigns = new Hashtable<Rank, String>();

    /**
     * Rank signs container.
     */
    private final Hashtable<Rank, String> ranks = new Hashtable<Rank, String>();

    /**
     * Suit signs container.
     */
    private final Hashtable<Suit, String> suits = new Hashtable<Suit, String>();

    private final Context context;

    /**
     * Constructor.
     */
    public TextDecorator(Context context) {
        this.context = context;

        // Rank signs initialization
        rankSigns.put(Rank.Ace, context.getString(R.string.AceSign));
        rankSigns.put(Rank.King, context.getString(R.string.KingSign));
        rankSigns.put(Rank.Queen, context.getString(R.string.QueenSign));
        rankSigns.put(Rank.Jack, context.getString(R.string.JackSign));
        rankSigns.put(Rank.Ten, context.getString(R.string.TenSign));
        rankSigns.put(Rank.Nine, context.getString(R.string.NineSign));

        // Rank signs initialization
        ranks.put(Rank.Ace, context.getString(R.string.Ace));
        ranks.put(Rank.King, context.getString(R.string.King));
        ranks.put(Rank.Queen, context.getString(R.string.Queen));
        ranks.put(Rank.Jack, context.getString(R.string.Jack));
        ranks.put(Rank.Ten, context.getString(R.string.Ten));
        ranks.put(Rank.Nine, context.getString(R.string.Nine));

        // Suits
        suits.put(Suit.Club, context.getString(R.string.Clubs));
        suits.put(Suit.Diamond, context.getString(R.string.Diamonds));
        suits.put(Suit.Heart, context.getString(R.string.Hearts));
        suits.put(Suit.Spade, context.getString(R.string.Spades));
    }

    /**
     * Returns rank sign text.
     * @param rank Rank instance.
     * @return Text presentation of the provided argument object.
     */
    public String getRankSign(final Rank rank) {
        return getHasTableKeyString(rankSigns, rank);
    }

    /**
     * Returns rank sign text.
     * @param rank Rank instance.
     * @return Text presentation of the provided argument object.
     */
    public String getRank(final Rank rank) {
        return getHasTableKeyString(ranks, rank);
    }

    /**
     * Returns rank sign text.
     * @param rank Rank instance.
     * @return Text presentation of the provided argument object.
     */
    public String getSuit(final Suit suit) {
        return getHasTableKeyString(suits, suit);
    }

    /**
     * Returns rank first letter sign.
     * @param rank Rank instance.
     * @return Text presentation of the provided argument object.
     */
    public String getRankLetter(final Rank rank) {
        final String text = getHasTableKeyString(ranks, rank);
        if (text != null && text.length() > 0) {
            return text.substring(0, 1);
        }
        return "";
    }

    /**
     * Returns associated key object value text presentation.
     * @param hash container.
     * @param key of the object.
     * @return Text presentation of the key object.
     */
    private String getHasTableKeyString(final Hashtable<?, String> hash, final Object key) {
        if (hash.containsKey(key)) {
            return hash.get(key).toString();
        }
        return "";
    }

    public String translateCouple(Suit suit, Suit trump, String message) {
        String couple;
        if (suit.equals(trump)) {
            couple = context.getString(R.string.CoupleForty);
        } else {
            couple = context.getString(R.string.CoupleTwenty);
        }

        return message.replace(COUPLE, couple);
    }

    public String replaceSuit(Suit suit, String message) {
        return message.replace(SUIT, getSuit(suit));
    }
}
