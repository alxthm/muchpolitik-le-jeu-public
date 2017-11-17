package com.muchpolitik.lejeu.GameActors.Enemies.Pacific;

import com.badlogic.gdx.math.Rectangle;
import com.muchpolitik.lejeu.Stages.GameStage;

public class Moustachu extends PacificEnemy {

    public Moustachu(float startX, float startY, float range, GameStage gameStage) {
        super("moustachu", startX, startY, range, gameStage);

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
