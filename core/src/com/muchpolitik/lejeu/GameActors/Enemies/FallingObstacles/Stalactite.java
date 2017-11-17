package com.muchpolitik.lejeu.GameActors.Enemies.FallingObstacles;

import com.badlogic.gdx.math.Rectangle;
import com.muchpolitik.lejeu.Stages.GameStage;

/**
 * A stalactite falling on the player.
 */

public class Stalactite extends FallingObstacle {

    public Stalactite(float startX, float startY, float range, GameStage gameStage) {
        super("stalactite", startX, startY, range, gameStage);

        // set custom variables
        setSize(1, 1);
        speed = 12;
        deathTime = 0.5f;

        // set hitbox
        hitboxWidth = getWidth();
        bounds = new Rectangle(getX(), getY(), hitboxWidth, getHeight());

        createStateMachines();
    }

}
