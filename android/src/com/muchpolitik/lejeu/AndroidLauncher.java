package com.muchpolitik.lejeu;

import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		// use immersive mode : hide navigation and status bars
		config.useImmersiveMode = true;

        initialize(new LeJeuAndroid(this), config);

        // catch back key to avoid quitting immediately (has to be called after initialization)
        Gdx.input.setCatchBackKey(true);
    }
}
