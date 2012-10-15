/*
 * Copyright (c) Dimitar Karamanov 2008-2010. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the source code must retain
 * the above copyright notice and the following disclaimer.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 */
package com.karamanov.santase.graphics;

/**
 * ImagePosition class.
 * @author Dimitar Karamanov
 */
public class ImagePosition {

    /**
     * Internal constant for left.
     */
    private final static int LEFT = 0;

    /**
     * Internal constant for right.
     */
    private final static int RIGHT = 1;

    /**
     * Position left constant object.
     */
    public final static ImagePosition Left = new ImagePosition(LEFT);

    /**
     * Position right constant object.
     */
    public final static ImagePosition Right = new ImagePosition(RIGHT);

    /**
     * Internal value.
     */
    private final int position;

    /** 
     * Constructor.
     * @param position
     */
    public ImagePosition(final int position) {
        this.position = position;
    }

    /**
     * Returns object hash code.
     * @return int object hash code.
     */
    public int hashCode() {
        return position;
    }

    /**
     * The method checks if this ImagePosition and specified object (ImagePosition) are equal.
     * @param obj specified object.
     * @return boolean true if this ImagePosition is equal to specified object and false otherwise.
     */
    public boolean equals(final Object obj) {
        if (obj instanceof ImagePosition) {
            return position == ((ImagePosition) obj).position;
        }
        return false;
    }
}
