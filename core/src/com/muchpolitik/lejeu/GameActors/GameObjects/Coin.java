package com.muchpolitik.lejeu.GameActors.GameObjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.muchpolitik.lejeu.Screens.Level;

public class Coin extends Actor {

    private int value = 1;
    private Sprite sprite;
    private Rectangle bounds;
    private Level level;

    public Coin(float startX, float startY, Level lvl) {
        level = lvl;

        sprite = level.getGameObjectsAtlas().createSprite("coin");

        //set position and size
        setBounds(startX, startY, 1, 1);
        bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        if (bounds.overlaps(level.getPlayer().getBounds())) {
            level.addMoney(value);
            this.remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(sprite, getX(), getY(), getWidth(), getHeight());
    }
}
