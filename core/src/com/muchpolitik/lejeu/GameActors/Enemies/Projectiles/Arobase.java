package com.muchpolitik.lejeu.GameActors.Enemies.Projectiles;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.muchpolitik.lejeu.Stages.GameStage;

public class Arobase extends Projectile {

    public Arobase(float startX, float startY, boolean facingRight, GameStage gameStage) {
        super("arobase" + MathUtils.random(6), startX, startY, facingRight, gameStage);

        // set custom variables
        setSize(0.5f, 0.5f);
        speed = 5;
        deathTime = 0.5f;

        // set hitbox
        hitboxWidth = getWidth();
        bounds = new Rectangle(getX(), getY(), hitboxWidth, getHeight());

        createStateMachines();
    }

}
