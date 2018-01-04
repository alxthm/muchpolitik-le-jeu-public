package com.muchpolitik.lejeu.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.muchpolitik.lejeu.LeJeu;

/**
 * Game class for html apps.
 */

public class LeJeuHtml extends LeJeu {

    @Override
    public void render() {
        super.render();

        // catch escape key
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            onBackPressed(false);
    }

    @Override
    public void shareApp() {

    }

}
