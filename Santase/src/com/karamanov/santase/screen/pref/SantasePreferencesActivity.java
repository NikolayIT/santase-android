/*
 * Copyright (c) i:FAO AG. All Rights Reserved.
 * CytricSettingsActivity.java
 * cytric mobile application.
 *
 * Created by mobile team Feb 20, 2012
 */
package com.karamanov.santase.screen.pref;

import com.karamanov.santase.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SantasePreferencesActivity extends PreferenceActivity {

	public SantasePreferencesActivity() {
        super();
    }

    /**
     * Called when the activity is first created.
     * @param saveInstanceState - Bundle contains the data it most recently supplied in
     *            onSaveInstanceState(Bundle).
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setTitle(getString(R.string.PREFERENCES));
    }
}
