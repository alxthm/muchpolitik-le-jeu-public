package com.muchpolitik.lejeu.AI.BossStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.TimeUtils;
import com.muchpolitik.lejeu.AI.MessageType;
import com.muchpolitik.lejeu.GameActors.Enemies.Bosses.Boss;

/**
 * Contains the states for all bosses' livingStateMachines (WALK, ATTACK). Messages are handled by the phaseStateMachine (specific to each boss),
 * although it only gets messages if enemy is in LIVING state.
 */

public enum BossStates implements State<Boss> {

    WALK() {
        @Override
        public void enter(Boss boss) {
            boss.currentAnimation = boss.facingRight? boss.walkRightAnimation : boss.walkLeftAnimation;

            // reset the tracker
            boss.timeOfWalkBeginning = TimeUtils.millis();
        }

        @Override
        public void update(Boss boss) {
            boss.moveWithinRangeOfAction();
            boss.checkPlayerCollision();

            // once enough time has elapsed, the boss is ready to attack
            if (TimeUtils.timeSinceMillis(boss.timeOfWalkBeginning) > boss.timeBetweenAttacks * 1000)
                MessageManager.getInstance().dispatchMessage(boss, boss, MessageType.READY_TO_ATTACK);
        }
    },

    ATTACK() {
        @Override
        public void enter(Boss boss) {
            boss.startAttack();
        }

        @Override
        public void update(Boss boss) {
            if (boss.isPlayerBehind()) {
                boss.turnAround();
            }

            boss.checkPlayerCollision();
        }
    },

    /**
     * The boss is invisible and cannot hurt or be hurt by the player.
     */
    HIDING {
        @Override
        public void enter(Boss boss) {
            // make the boss invisible
            boss.setColor(1, 1, 1, 0.5f);
        }

        @Override
        public void exit(Boss boss) {
            // make the boss visible again
            boss.setColor(1, 1, 1, 1);
        }
    };


    @Override
    public void enter(Boss entity) {

    }

    @Override
    public void update(Boss entity) {

    }

    @Override
    public void exit(Boss entity) {

    }

    @Override
    public boolean onMessage(Boss boss, Telegram telegram) {
        // let the phase machine handle the message
        return boss.handlePhasesStateMachine(telegram);
    }
}
