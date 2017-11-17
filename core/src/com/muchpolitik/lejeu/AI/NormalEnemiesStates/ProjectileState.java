package com.muchpolitik.lejeu.AI.NormalEnemiesStates;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.muchpolitik.lejeu.GameActors.Enemies.Projectiles.Projectile;

/**
 * Contains states for projectiles.
 */
public enum ProjectileState implements State<Projectile> {

    FLYING() {
        @Override
        public void update(Projectile projectile) {
            // move projectile
            float speedX = projectile.facingRight? projectile.speed : -projectile.speed;
            projectile.moveBy(speedX * projectile.deltaTime, 0);

            projectile.checkPlayerCollision();
            projectile.checkTilesCollision();
        }
    };

    @Override
    public void enter(Projectile entity) {

    }

    @Override
    public void exit(Projectile projectile) {

    }

    @Override
    public boolean onMessage(Projectile projectile, Telegram telegram) {
        return false;
    }
}
