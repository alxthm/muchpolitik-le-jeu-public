package com.muchpolitik.lejeu;


import android.content.Intent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.android.AndroidApplication;

/**
 * Game class for Android apps.
 */

public class LeJeuAndroid extends LeJeu {

    // useful for starting an external activity
    private AndroidApplication androidApplication;

    public LeJeuAndroid(AndroidApplication androidApplication) {
        super();
        this.androidApplication = androidApplication;
    }


    @Override
    public void render() {
        super.render();

        // register the call to back key
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK))
            onBackPressed(true);
    }

    @Override
    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "L'avanture incroyabl et mirobolante du jeune " +
                "filibère, disponible sur android !\n" +
                "https://play.google.com/store/apps/details?id=com.muchpolitik.lejeu");
        sendIntent.setType("text/plain");

        androidApplication.startActivity(sendIntent);
    }

}
