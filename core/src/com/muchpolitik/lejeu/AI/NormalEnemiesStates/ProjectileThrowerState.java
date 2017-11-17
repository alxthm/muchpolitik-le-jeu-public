package com.muchpolitik.lejeu.AI.NormalEnemiesStates;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.muchpolitik.lejeu.AI.MessageType;
import com.muchpolitik.lejeu.GameActors.Enemies.ProjectileThrowers.ProjectileThrower;

/**
 * Contains states for all enemies throwing projectiles.
 */
public enum ProjectileThrowerState implements State<ProjectileThrower> {

    WANDER() {
        @Override
        public void enter(ProjectileThrower enemy) {
            enemy.currentAnimation = enemy.facingRight? enemy.walkRightAnimation : enemy.walkLeftAnimation;
        }

        @Override
        public void update(ProjectileThrower enemy) {
            enemy.moveWithinRangeOfAction();

            if (enemy.isPlayerVisible())
                enemy.livingStateMachine.changeState(ATTACK);

            enemy.checkPlayerCollision();
        }
    },

    ATTACK() {
        @Override
        public void enter(ProjectileThrower enemy) {
            enemy.startAttack();
        }

        @Override
        public void update(ProjectileThrower enemy) {
            // leave ATTACK state if player is out of scope but enemy has not started his attack (to avoid missing THROW_PROJECTILE messages).
            // As such, once the enemy has entered attack mode, it has to throw its projectile before eventually going back to WANDER.
            if (enemy.isPlayerOutOfScope() && !enemy.throwingProjectile) {
                enemy.livingStateMachine.changeState(WANDER);
            }
            else if (enemy.isPlayerBehind()) {
                enemy.turnAround();
            }

            enemy.checkPlayerCollision();
        }

        @Override
        public boolean onMessage(ProjectileThrower enemy, Telegram telegram) {
            switch (telegram.message) {
                case MessageType.READY_TO_ATTACK:
                    enemy.startAttack();
                    return true;

                case MessageType.THROW_PROJECTILE:
                    enemy.throwProjectile();
                    return true;
            }
            return false;
        }

    };

    @Override
    public void enter(ProjectileThrower enemy) {

    }

    @Override
    public void exit(ProjectileThrower enemy) {

    }

    @Override
    public boolean onMessage(ProjectileThrower enemy, Telegram telegram) {
        return false;
    }
}
