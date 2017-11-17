package com.muchpolitik.lejeu.GameActors.Enemies.ProjectileThrowers;

import com.badlogic.gdx.math.Rectangle;
import com.muchpolitik.lejeu.GameActors.Enemies.Projectiles.BouleDeFeu;
import com.muchpolitik.lejeu.Stages.GameStage;

public class Draideux extends ProjectileThrower {

    public Draideux(float startX, float startY, float range, GameStage gameStage) {
        super("draideux", startX, startY, range, gameStage);

        // set custom variables
        defenseType = DefenseType.Basic;
        speed = 1;
        deathTime = 4;
        timeBetweenAttacks = 1.5f;
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

        // create projectile and add it to the stage
        float x = getX();
        float y = getY() + 0.4f;
        BouleDeFeu projectile = new BouleDeFeu(x, y, facingRight, gameStage);
        gameStage.addActor(projectile);
    }
}
