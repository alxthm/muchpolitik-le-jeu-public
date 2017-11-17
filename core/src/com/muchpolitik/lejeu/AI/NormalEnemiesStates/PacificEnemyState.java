package com.muchpolitik.lejeu.AI.NormalEnemiesStates;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.muchpolitik.lejeu.GameActors.Enemies.Pacific.PacificEnemy;

/**
 * Contains states for all non agressive enemies.
 */
public enum PacificEnemyState implements State<PacificEnemy> {

    WANDER() {
        @Override
        public void enter(PacificEnemy enemy) {
            enemy.currentAnimation = enemy.facingRight? enemy.walkRightAnimation : enemy.walkLeftAnimation;
        }

        @Override
        public void update(PacificEnemy enemy) {
            enemy.moveWithinRangeOfAction();
            enemy.checkPlayerCollision();
        }
    };

    @Override
    public void exit(PacificEnemy enemy) {

    }

    @Override
    public boolean onMessage(PacificEnemy enemy, Telegram telegram) {
        return false;
    }
}
