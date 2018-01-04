package com.muchpolitik.lejeu.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.muchpolitik.lejeu.LeJeu;

/**
 * Game class for Desktop apps.
 */

public class LeJeuDesktop extends LeJeu {
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
