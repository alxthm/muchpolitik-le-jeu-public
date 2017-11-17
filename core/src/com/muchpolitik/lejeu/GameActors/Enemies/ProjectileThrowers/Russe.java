package com.muchpolitik.lejeu.GameActors.Enemies.ProjectileThrowers;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.muchpolitik.lejeu.GameActors.Enemies.Projectiles.Faucille;
import com.muchpolitik.lejeu.GameActors.Enemies.Projectiles.Marteau;
import com.muchpolitik.lejeu.GameActors.Enemies.Projectiles.Projectile;
import com.muchpolitik.lejeu.Stages.GameStage;

public class Russe extends ProjectileThrower {

    public Russe(float startX, float startY, float range, GameStage gameStage) {
        super("russe", startX, startY, range, gameStage);

        // set custom variables
        defenseType = DefenseType.Tank;
        speed = 1;
        deathTime = 4;
        timeBetweenAttacks = 2f;
        attackDelay = 0.5f;
        setSize(1, 1.08f);

        // set hitbox
        hitboxWidth = getWidth();
        bounds = new Rectangle(getX(), getY(), hitboxWidth, 0.92f * getHeight());

        loadStunnedAnimations(gameStage.getLevel().getGameObjectsAtlas(), "russe");
        createStateMachines();
    }

    @Override
    public void throwProjectile() {
        super.throwProjectile();

        // create a random projectile and add it to the stage
        float x = getX();
        float y = getY() + 0.4f;
        Projectile projectile;
        if (MathUtils.randomBoolean())
            projectile = new Marteau(x, y, facingRight, gameStage);
        else
            projectile = new Faucille(x, y, facingRight, gameStage);
        gameStage.addActor(projectile);
    }
}
