package com.muchpolitik.lejeu.GameActors.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.muchpolitik.lejeu.Screens.Level;

/**
 * A label containing text to help the player.
 */

public class Hint extends Actor {

    private Sprite sprite;


    public Hint(float startX, float startY, Level level, String imageName) {
        sprite = level.getGameObjectsAtlas().createSprite(imageName);

        //set position and size
        setBounds(startX, startY, 3, 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(sprite, getX(), getY(), getWidth(), getHeight());
    }
}
