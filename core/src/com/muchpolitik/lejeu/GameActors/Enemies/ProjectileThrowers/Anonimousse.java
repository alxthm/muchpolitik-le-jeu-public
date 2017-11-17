package com.muchpolitik.lejeu.GameActors.Enemies.ProjectileThrowers;

import com.badlogic.gdx.math.Rectangle;
import com.muchpolitik.lejeu.GameActors.Enemies.Projectiles.Arobase;
import com.muchpolitik.lejeu.Stages.GameStage;

public class Anonimousse extends ProjectileThrower {

    public Anonimousse(float startX, float startY, float range, GameStage gameStage) {
        super("anonimousse", startX, startY, range, gameStage);

        // set custom variables
        defenseType = DefenseType.Basic;
        speed = 1.5f;
        deathTime = 4;
        timeBetweenAttacks = 2f;
        attackDelay = 0.5f;
        setSize(1, 1);

        // set hitbox
        hitboxWidth = getWidth();
        bounds = new Rectangle(getX(), getY(), hitboxWidth, getHeight());

        createStateMachines();
    }

    @Override
    public void throwProjectile() {
        super.throwProjectile();

        // create a projectile and add it to the stage
        float x = getX();
        float y = getY() + 0.4f;
        Arobase projectile = new Arobase(x, y, facingRight, gameStage);
        gameStage.addActor(projectile);
    }
}
