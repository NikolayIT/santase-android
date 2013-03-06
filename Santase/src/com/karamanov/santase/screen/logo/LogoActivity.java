/*
 * Copyright (c) Dimitar Karamanov 2008-2010. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the source code must retain
 * the above copyright notice and the following disclaimer.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 */
package com.karamanov.santase.screen.logo;

import com.karamanov.santase.Santase;
import com.karamanov.santase.screen.main.SantaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * LogoFrame class.
 * @author Dimitar Karamanov
 */
public class LogoActivity extends Activity {

    private boolean send = false;

    /**
     * Constructor.
     * @param canvas parent container game canvas
     * @param rootComponent original root component
     */
    public LogoActivity() {
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogoView view = new LogoView(this);

        setContentView(view);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        startSantaseActivity();
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        startSantaseActivity();
        return true;
    }

    private void startSantaseActivity() {
        if (!send) {
            send = true;
            Santase.initSantaseFacade(this);
            Intent i = new Intent(this, SantaseActivity.class);
            startActivity(i);
        }
        finish();
    }
}
