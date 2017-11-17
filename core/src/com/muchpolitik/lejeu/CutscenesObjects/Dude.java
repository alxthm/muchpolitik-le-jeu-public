package com.muchpolitik.lejeu.CutscenesObjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Characters displayed in cut-scenes.
 */
public class Dude extends Actor {
    Sprite sprite;

    public Dude(Sprite sprite) {
        this.sprite = sprite;
        setSize(432, 432);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(sprite, getX(), getY(), getWidth(), getHeight());
    }
}
