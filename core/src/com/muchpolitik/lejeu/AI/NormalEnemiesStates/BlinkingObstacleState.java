package com.muchpolitik.lejeu.AI.NormalEnemiesStates;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.muchpolitik.lejeu.AI.MessageType;
import com.muchpolitik.lejeu.GameActors.Enemies.DangerousObstacle.BlinkingObstacle;

/**
 * Contains States for Blinking Obstacles.
 */

public enum BlinkingObstacleState implements State<BlinkingObstacle>{

    OFF() {
        @Override
        public void enter(BlinkingObstacle entity) {
            // send a message to go blinking after offDuration
            MessageManager.getInstance().dispatchMessage(entity.offDuration, entity, entity, MessageType.TIME_TO_BLINK);

            // set alpha to 0 (invisible)
            entity.setColor(1, 1, 1, 0);
        }

        @Override
        public boolean onMessage(BlinkingObstacle entity, Telegram telegram) {
            if (telegram.message == MessageType.TIME_TO_BLINK) {
                // once the enemy has been off for long enough, go blinking
                entity.livingStateMachine.changeState(BLINKING);
            }
            return true;
        }

    },

    BLINKING() {
        @Override
        public void enter(BlinkingObstacle entity) {
            // send a message to go on after blinkDuration
            MessageManager.getInstance().dispatchMessage(entity.blinkDuration, entity, entity, MessageType.TIME_TO_GO_ON);

            SequenceAction blink = Actions.sequence(
                    Actions.fadeIn(entity.blinkDuration / 5f),
                    Actions.fadeOut(entity.blinkDuration / 5f),
                    Actions.fadeIn(entity.blinkDuration / 5f),
                    Actions.fadeOut(entity.blinkDuration / 5f),
                    Actions.fadeIn(entity.blinkDuration / 5f));
            entity.addAction(blink);
        }

        @Override
        public boolean onMessage(BlinkingObstacle entity, Telegram telegram) {
            if (telegram.message == MessageType.TIME_TO_GO_ON) {
                // once the enemy has blinked for long enough, go on
                entity.livingStateMachine.changeState(ON);
            }
            return true;
        }
    },

    ON() {
        @Override
        public void enter(BlinkingObstacle entity) {
            // send a message to go off after onDuration
            MessageManager.getInstance().dispatchMessage(entity.onDuration, entity, entity, MessageType.TIME_TO_GO_OFF);

            // set alpha to 1 (fully visible)
            entity.setColor(1, 1, 1, 1);
        }

        @Override
        public void update(BlinkingObstacle entity) {
            entity.checkPlayerCollision();
        }

        @Override
        public boolean onMessage(BlinkingObstacle entity, Telegram telegram) {
            if (telegram.message == MessageType.TIME_TO_GO_OFF) {
                // once the enemy is on long enough, go off
                entity.livingStateMachine.changeState(OFF);
            }
            return true;
        }
    },

    /**
     * State of the enemy at beginning, before he goes BLINKING and begins its cycle. Allows enemies to appear
     * only after a specified delay.
     */
    WAITING_BEFORE_APPARITION() {
        @Override
        public void enter(BlinkingObstacle entity) {
            // send a message to go blinking after apparition delay
            MessageManager.getInstance().dispatchMessage(entity.apparitionDelay, entity, entity, MessageType.TIME_TO_BLINK);

            // set alpha to 0 (invisible)
            entity.setColor(1, 1, 1, 0);
        }

        @Override
        public boolean onMessage(BlinkingObstacle entity, Telegram telegram) {
            if (telegram.message == MessageType.TIME_TO_BLINK) {
                // go blinking (once the apparition delay is over)
                entity.livingStateMachine.changeState(BLINKING);
            }
            return true;
        }
    };

    @Override
    public void enter(BlinkingObstacle entity) {

    }

    @Override
    public void update(BlinkingObstacle entity) {

    }

    @Override
    public void exit(BlinkingObstacle entity) {

    }

    @Override
    public boolean onMessage(BlinkingObstacle entity, Telegram telegram) {
        return false;
    }


}
