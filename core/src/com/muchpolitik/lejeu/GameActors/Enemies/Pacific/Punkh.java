package com.muchpolitik.lejeu.GameActors.Enemies.Pacific;

import com.badlogic.gdx.math.Rectangle;
import com.muchpolitik.lejeu.Stages.GameStage;

public class Punkh extends PacificEnemy {

    public Punkh(float startX, float startY, float range, GameStage gameStage) {
        super("punkh", startX, startY, range, gameStage);

        // set custom variables
        defenseType = DefenseType.Immortal;
        speed = 1.5f;
        deathTime = 4;
        setSize(1, 1.14f);

        // set hitbox
        hitboxWidth = getWidth();
        bounds = new Rectangle(getX() + (getWidth() - hitboxWidth)/2f, getY(), hitboxWidth, 0.87f * getHeight());

        createStateMachines();
    }

}
