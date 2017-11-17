package com.muchpolitik.lejeu.AI.NormalEnemiesStates;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.muchpolitik.lejeu.GameActors.Enemies.Sprinters.SprinterEnemy;

/**
 * Contains states for all sprinter enemies.
 */
public enum SprinterEnemyState implements State<SprinterEnemy> {

    WANDER() {
        @Override
        public void enter(SprinterEnemy enemy) {
            enemy.currentAnimation = enemy.facingRight? enemy.walkRightAnimation : enemy.walkLeftAnimation;
        }

        @Override
        public void update(SprinterEnemy enemy) {
            enemy.moveWithinRangeOfAction();

            if (enemy.isPlayerVisible()) {
                enemy.livingStateMachine.changeState(ATTACKING);
            }

            enemy.checkPlayerCollision();
        }
    },

    ATTACKING() {
        @Override
        public void enter(SprinterEnemy enemy) {
            enemy.currentAnimation = enemy.facingRight? enemy.attackingRightAnimation : enemy.attackingLeftAnimation;
        }

        @Override
        public void update(SprinterEnemy enemy) {
            if (enemy.isPlayerBehind())
                enemy.livingStateMachine.changeState(WANDER);
            else
                enemy.sprintWithinRangeOfAction();

            enemy.checkPlayerCollision();
        }
    };

    @Override
    public void exit(SprinterEnemy enemy) {

    }

    @Override
    public boolean onMessage(SprinterEnemy enemy, Telegram telegram) {
        return false;
    }
}
