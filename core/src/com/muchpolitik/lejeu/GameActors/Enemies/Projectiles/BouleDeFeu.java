package com.muchpolitik.lejeu.GameActors.Enemies.Projectiles;

import com.badlogic.gdx.math.Rectangle;
import com.muchpolitik.lejeu.Screens.Level;
import com.muchpolitik.lejeu.Stages.GameStage;

public class BouleDeFeu extends Projectile {

    public BouleDeFeu(float startX, float startY, boolean facingRight, GameStage gameStage) {
        super("boule-de-feu", startX, startY, facingRight, gameStage);

        // set custom variables
        setSize(0.5f, 0.5f);
        speed = 6;
        deathTime = 0.5f;

        // set hitbox
        hitboxWidth = getWidth();
        bounds = new Rectangle(getX(), getY(), hitboxWidth, getHeight());

        createStateMachines();
    }

}
