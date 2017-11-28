package com.muchpolitik.lejeu.GameActors.GameObjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.muchpolitik.lejeu.Screens.Level;

/**
 * Load next Level / Cutscene when touched by the player.
 */
public class ExitDoor extends Actor {

    private boolean disabled;

    private Sprite currentSprite, enabledSprite, disabledSprite;
    private Rectangle bounds;
    private Level level;

    public ExitDoor(float startX, float startY, Level lvl) {
        level = lvl;

        // load sprites (a mailbox for all boss levels except Mairie, and a door otherwise)
        boolean levelWithMailbox = level.getName().contains("timer") && !level.getName().contains("Mairie");
        String doorType = levelWithMailbox? "exit-mailbox" : "exit-door";
        enabledSprite = lvl.getGameObjectsAtlas().createSprite(doorType + "-enabled");
        disabledSprite = lvl.getGameObjectsAtlas().createSprite(doorType + "-disabled");

        // currentSprite is set once all keys are loaded, in the setDisabled method,
        // /!\ to call it just after winTrigger instantiation

        //set position and size
        setBounds(startX, startY, 1, 1);
        bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());

    }

    @Override
    public void act(float delta) {

        if (!disabled && bounds.overlaps(level.getPlayer().getBounds())) {
            level.loadGameWinMenu();

            // to prevent loading more gameWinMenu
            disabled = true;
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentSprite, getX(), getY(), getWidth(), getHeight());
    }

    /**
     * Important : call it just after winTrigger instantiation to set currentSprite.
     * @param disabled
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;

        currentSprite = disabled ? disabledSprite : enabledSprite;
    }
}
