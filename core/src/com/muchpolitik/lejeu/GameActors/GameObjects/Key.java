package com.muchpolitik.lejeu.GameActors.GameObjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.muchpolitik.lejeu.Screens.Level;

/**
 * Has to be found by the player to unlock winTrigger.
 */

public class Key extends Actor {

    private Sprite sprite;
    private Rectangle bounds;
    private Level level;

    public Key(float startX, float startY, Level lvl) {
        level = lvl;

        String keyType;

        if (level.getName().equals("Ville/timer"))
            // for boss level in Ville world
            keyType = "key-dossier-signatures";
        else if (level.getName().contains("timer"))
            // for other bosses level
            keyType = "key-dossier-secret";
        else if (level.getName().contains("Ville"))
            // for regular Ville levels
            keyType = "key-signature";
        else
            // for regular levels in other worlds
            keyType = "key-default";


        // create sprite
        sprite = level.getGameObjectsAtlas().createSprite(keyType);

        //set position and size
        setBounds(startX, startY, 1, 1);
        bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        if (bounds.overlaps(level.getPlayer().getBounds())) {
            // register that a new key was found
            level.findKey();

            this.remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(sprite, getX(), getY(), getWidth(), getHeight());
    }

}
