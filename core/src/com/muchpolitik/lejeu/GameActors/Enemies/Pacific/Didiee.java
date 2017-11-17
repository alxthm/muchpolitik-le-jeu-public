package com.muchpolitik.lejeu.GameActors.Enemies.Pacific;

import com.badlogic.gdx.math.Rectangle;
import com.muchpolitik.lejeu.Stages.GameStage;

public class Didiee extends PacificEnemy {

    public Didiee(float startX, float startY, float range, GameStage gameStage) {
        super("didiee", startX, startY, range, gameStage);

        // set custom variables
        defenseType = DefenseType.Basic;
        speed = 1;
        deathTime = 4;
        setSize(1, 1);

        // set hitbox
        hitboxWidth = getWidth();
        bounds = new Rectangle(getX(), getY(), hitboxWidth, getHeight());

        createStateMachines();
    }

}
