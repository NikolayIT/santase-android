package com.karamanov.santase.screen.base;

import java.io.Serializable;

public class BooleanFlag implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4218960544131511719L;

    private Boolean value;

    public BooleanFlag() {
        this(Boolean.TRUE);
    }

    public BooleanFlag(Boolean value) {
        this.value = value;
    }

    public void setTrue() {
        value = Boolean.TRUE;
    }

    public void setFalse() {
        value = Boolean.FALSE;
    }

    public boolean getValue() {
        return value.booleanValue();
    }
}