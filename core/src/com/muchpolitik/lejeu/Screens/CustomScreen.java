package com.muchpolitik.lejeu.Screens;

import com.badlogic.gdx.Screen;

/**
 * Extends Screen, with a load() method added.
 */
public interface CustomScreen extends Screen {
    /**
     * Load all necessary resources. To be called during the transition between screens,
     * to hide the freezing.
     */
    public void load();
}
