package com.muchpolitik.lejeu.GameActors.Enemies.FallingObstacles;

import com.badlogic.gdx.math.Rectangle;
import com.muchpolitik.lejeu.Stages.GameStage;

public class MarteauObstacle extends FallingObstacle {

    public MarteauObstacle(float startX, float startY, float range, GameStage gameStage) {
        super("marteau-obstacle", startX, startY, range, gameStage);

        // set custom variables
        setSize(1, 1);
        speed = 11;
        deathTime = 0.5f;

        // set hitbox
        hitboxWidth = getWidth();
        bounds = new Rectangle(getX(), getY(), hitboxWidth, getHeight());

        createStateMachines();
    }

}
