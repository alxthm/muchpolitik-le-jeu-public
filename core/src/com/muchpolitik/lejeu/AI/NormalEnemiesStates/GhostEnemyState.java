package com.muchpolitik.lejeu.AI.NormalEnemiesStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.muchpolitik.lejeu.AI.MessageType;
import com.muchpolitik.lejeu.GameActors.Enemies.Ghosts.GhostEnemy;

/**
 * Contains states for all ghost enemies.
 */
public enum GhostEnemyState implements State<GhostEnemy> {

    WANDER() {
        @Override
        public void enter(GhostEnemy enemy) {
            // set a timer for the enemy to attack
            MessageManager.getInstance().dispatchMessage(enemy.timeBetweenAttacks, enemy, enemy, MessageType.READY_TO_SHADOW);
        }

        @Override
        public void update(GhostEnemy enemy) {
            enemy.moveWithinRangeOfAction();
            enemy.checkPlayerCollision();
        }

        @Override
        public boolean onMessage(GhostEnemy enemy, Telegram telegram) {
            if (telegram.message == MessageType.READY_TO_SHADOW) {
                enemy.livingStateMachine.changeState(SHADOW);
                return true;
            }
            return false;
        }
    },

    SHADOW() {
        @Override
        public void enter(GhostEnemy enemy) {
            // set a timer for the enemy to go back to WALK state
            MessageManager.getInstance().dispatchMessage(enemy.attackDuration, enemy, enemy, MessageType.SHADOW_FINISHED);

            enemy.fadeInAndOutAction = Actions.sequence(
                    Actions.fadeOut(enemy.attackDuration / 4f, Interpolation.pow4Out),
                    Actions.alpha(0.1f, enemy.attackDuration / 4f, Interpolation.pow3),
                    Actions.fadeOut(enemy.attackDuration / 4f, Interpolation.pow3),
                    Actions.fadeIn(enemy.attackDuration / 4f, Interpolation.pow4In));
            enemy.addAction(enemy.fadeInAndOutAction);
        }

        @Override
        public void update(GhostEnemy enemy) {
            enemy.moveWithinRangeOfAction();
            enemy.checkPlayerCollision();
        }

        @Override
        public boolean onMessage(GhostEnemy enemy, Telegram telegram) {
            switch (telegram.message) {
                case MessageType.SHADOW_FINISHED:
                    enemy.livingStateMachine.changeState(WANDER);
                    return true;
                case MessageType.START_DYING:
                    // stop being transparent
                    enemy.removeAction(enemy.fadeInAndOutAction);
                    enemy.setColor(1, 1, 1, 1);
                    return true;
                default:
                    return false;
            }
        }
    };

    @Override
    public void enter(GhostEnemy enemy) {

    }

    @Override
    public void update(GhostEnemy enemy) {

    }

    @Override
    public void exit(GhostEnemy enemy) {

    }

    @Override
    public boolean onMessage(GhostEnemy enemy, Telegram telegram) {
        return false;
    }
}
