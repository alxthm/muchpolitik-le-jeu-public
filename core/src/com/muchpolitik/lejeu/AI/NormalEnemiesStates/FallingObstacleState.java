package com.muchpolitik.lejeu.AI.NormalEnemiesStates;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.muchpolitik.lejeu.GameActors.Enemies.FallingObstacles.FallingObstacle;
import com.muchpolitik.lejeu.GameActors.Enemies.Projectiles.Projectile;

/**
 * Contains states for falling obstacles.
 */
public enum FallingObstacleState implements State<FallingObstacle> {

    WAITING() {
        @Override
        public void update(FallingObstacle obstacle) {
            obstacle.checkPlayerCollision();

            if (obstacle.isPlayerInRange()) {
                obstacle.livingStateMachine.changeState(FALLING);
            }
        }
    },

    FALLING() {
        @Override
        public void update(FallingObstacle obstacle) {
            obstacle.moveDown();
            obstacle.checkPlayerCollision();
            obstacle.checkTilesCollision();
        }
    };

    @Override
    public void enter(FallingObstacle obstacle) {

    }

    @Override
    public void exit(FallingObstacle obstacle) {

    }

    @Override
    public boolean onMessage(FallingObstacle obstacle, Telegram telegram) {
        return false;
    }
}
