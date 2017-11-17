package com.muchpolitik.lejeu.GameActors.GameObjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.muchpolitik.lejeu.Screens.Level;

public class InvincibilityBonus extends Actor {

    private static float duration = 10;

    private Sprite sprite;
    private Rectangle bounds;
    private Level level;

    public InvincibilityBonus(float startX, float startY, Level lvl) {
        level = lvl;

        sprite = level.getGameObjectsAtlas().createSprite("bonus-invincibilite");

        //set position and size
        setBounds(startX, startY, 1, 1);
        bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        if (bounds.overlaps(level.getPlayer().getBounds())) {
            level.getPlayer().makeInvicible(duration);
            this.remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(sprite, getX(), getY(), getWidth(), getHeight());
    }

    public static float getDuration() {
        return duration;
    }

}
