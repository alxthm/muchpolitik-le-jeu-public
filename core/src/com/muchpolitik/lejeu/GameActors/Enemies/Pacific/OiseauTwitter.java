package com.muchpolitik.lejeu.GameActors.Enemies.Pacific;

import com.badlogic.gdx.math.Rectangle;
import com.muchpolitik.lejeu.Stages.GameStage;

public class OiseauTwitter extends PacificEnemy {

    public OiseauTwitter(float startX, float startY, float range, GameStage gameStage) {
        super("oiseau-twitter", startX, startY, range, gameStage);

        // set custom variables
        defenseType = DefenseType.Basic;
        speed = 2.5f;
        deathTime = 4;
        setSize(0.5f, 0.5f);

        // set hitbox
        hitboxWidth = getWidth();
        bounds = new Rectangle(getX(), getY(), hitboxWidth, getHeight());

        createStateMachines();
    }

}
