/*
 * Copyright (c) Dimitar Karamanov 2008-2010. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the source code must retain
 * the above copyright notice and the following disclaimer.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 */
package com.karamanov.santase.screen.base;

import com.karamanov.santase.graphics.PictureDecorator;
import com.karamanov.santase.text.TextDecorator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * BasePainter class.
 * @author Dimitar Karamanov
 */
public abstract class BasePainter {

    /**
     * Cashed card width.
     */
    protected final int cardWidth;

    /**
     * Cashed card height.
     */
    protected final int cardHeight;

    /**
     * Cashed card width.
     */
    protected final int cardBackWidth;

    /**
     * Cashed card height.
     */
    protected final int cardBackHeight;

    /**
     * Text decorator of game beans object (Suit, Rank, Announce ...)
     */
    protected final TextDecorator textDecorator;

    /**
     * Text decorator of game beans object (Suit, Rank, Announce ...)
     */
    protected final PictureDecorator pictureDecorator;

    private final Context context;

    /**
     * Constructor.
     * @param width canvas width.
     * @param height canvas height.
     */
    protected BasePainter(Context context) {
        this.context = context;
        pictureDecorator = new PictureDecorator(context);
        cardWidth = pictureDecorator.getCardBackImage().getWidth();
        cardHeight = pictureDecorator.getCardBackImage().getHeight();
        cardBackWidth = pictureDecorator.getCardBackSmallImage().getWidth();
        cardBackHeight = pictureDecorator.getCardBackSmallImage().getHeight();
        textDecorator = new TextDecorator(context);
    }

    protected Context getContext() {
        return context;
    }

    /**
     * Draws number using images to be the same on different platforms.
     * @param g Graphics object.
     * @param number to be drawn.
     * @param x position.
     * @param y position.
     */
    protected final void drawNumber(final Canvas canvas, int number, final int x, final int y) {
        canvas.drawText(String.valueOf(number), x, y, new Paint());
    }

    /**
     * Draws desk image.
     * @param g Graphics object.
     * @param x position.
     * @param y position.
     */
    protected void drawDeskImage(final Canvas canvas, final int x, final int y) {
        Bitmap b = pictureDecorator.getCardBackSmallImage();
        canvas.drawBitmap(b, x, y, new Paint());
    }

    /**
     * Draws desk image.
     * @param g Graphics object.
     * @param x position.
     * @param y position.
     */
    protected void drawDeskImageLarge(final Canvas canvas, final int x, final int y) {
        Bitmap b = pictureDecorator.getCardBackImage();
        canvas.drawBitmap(b, x, y, new Paint());
    }

    protected void drawRotatedDeskImage(final Canvas canvas, final int x, final int y) {
        Bitmap b = pictureDecorator.getCardBackSmallImage();
        canvas.save();
        canvas.rotate(90);
        canvas.drawBitmap(b, y, -x, new Paint());
        canvas.restore();
    }
}