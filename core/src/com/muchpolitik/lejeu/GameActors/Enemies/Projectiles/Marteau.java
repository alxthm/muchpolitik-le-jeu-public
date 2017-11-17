package com.muchpolitik.lejeu.GameActors.Enemies.Projectiles;

import com.badlogic.gdx.math.Rectangle;
import com.muchpolitik.lejeu.Stages.GameStage;

public class Marteau extends Projectile {

    public Marteau(float startX, float startY, boolean facingRight, GameStage gameStage) {
        super("marteau", startX, startY, facingRight, gameStage);

        // set custom variables
        setSize(0.5f, 0.5f);
        speed = 6;
        deathTime = 0.5f;

        // set hitbox
        hitboxWidth = getWidth();
        bounds = new Rectangle(getX() + (getWidth() - hitboxWidth)/2f, getY(), hitboxWidth, getHeight());

        createStateMachines();
    }

}
