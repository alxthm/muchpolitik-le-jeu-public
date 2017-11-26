package com.muchpolitik.lejeu.AI;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.muchpolitik.lejeu.GameActors.Enemies.Enemy;
import com.muchpolitik.lejeu.GameActors.Enemies.ProjectileThrowers.ProjectileThrower;

/**
 * Contains generic states for all enemies. Handles behavior according to the Defense Type.
 */
public enum EnemyState implements State<Enemy> {

    LIVING() {
        @Override
        public void update(Enemy enemy) {
            // delegate action to the enemy specific stateMachine
            enemy.updateLivingStateMachine();
        }

        @Override
        public boolean onMessage(Enemy enemy, Telegram telegram) {
            return enemy.handleLivingStateMachineMessage(telegram);
        }
    },

    STUNNED() {
        @Override
        public void enter(Enemy enemy) {
            // play stunned anim from beginning
            enemy.currentAnimation = enemy.facingRight? enemy.stunnedRightAnimation : enemy.stunnedLeftAnimation;
            enemy.stateTime = 0;

            // tell the enemy when the stun is finished
            MessageManager.getInstance().dispatchMessage(enemy.stunnedTime, enemy, enemy, MessageType.STUN_FINISHED);
        }

        @Override
        public boolean onMessage(Enemy enemy, Telegram telegram) {
            switch (telegram.message) {
                // for tank enemies
                case MessageType.STUN_FINISHED:
                    enemy.globalStateMachine.changeState(LIVING);
                    enemy.returnToDefaultState();       // usually WALK or WANDER state
                    return true;

                // for projectile throwers
                case MessageType.THROW_PROJECTILE:
                    if (enemy instanceof ProjectileThrower)
                        // the attack is aborted and considered done
                        ((ProjectileThrower) enemy).throwingProjectile = false;
                    return true;
            }
            return false;
        }
    },

    DYING() {
        @Override
        public void enter(Enemy enemy) {
            // start dying animation with fade out
            enemy.stateTime = 0;
            enemy.currentAnimation = enemy.dyingAnimation;
            enemy.addAction(Actions.fadeOut(enemy.deathTime));

            // send a delayed message to inform enemy when animation is finished
            MessageManager.getInstance().dispatchMessage(enemy.deathTime, enemy, enemy, MessageType.DEAD);
        }

        @Override
        public boolean onMessage(Enemy enemy, Telegram telegram) {
            if (telegram.message == MessageType.DEAD) {
                // eventually let the enemy end the level if it's a boss
                enemy.handleLivingStateMachineMessage(telegram);

                // dying animation with fade out is complete, so we remove the enemy
                enemy.remove();
                return true;
            } else {
                return false;
            }
        }
    };

    @Override
    public void enter(Enemy enemy) {

    }

    @Override
    public void update(Enemy enemy) {

    }

    @Override
    public void exit(Enemy enemy) {

    }
}
