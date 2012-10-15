package com.karamanov.santase.screen.main.message;

import android.graphics.Bitmap;

/**
 * MessageData class.
 */
public class MessageData {

    /**
     * Image.
     */
    private final Bitmap image;

    /**
     * Text.
     */
    private final String text;
    
    /**
     * Constructor.
     * @param image of the data.
     * @param text of the data.
     */
    public MessageData(final String text) {
    	this(null, text);
    }

    /**
     * Constructor.
     * @param image of the data.
     * @param text of the data.
     */
    public MessageData(final Bitmap image, final String text) {
        this.image = image;
        this.text = text;
    }
    
    public Bitmap getImage() {
    	return image;
    }
    
    public String getMessage() {
    	return text;
    }
}