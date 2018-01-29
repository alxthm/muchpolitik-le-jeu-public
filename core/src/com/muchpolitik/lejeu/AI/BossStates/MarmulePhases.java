package com.muchpolitik.lejeu.AI.BossStates;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.muchpolitik.lejeu.AI.MessageType;
import com.muchpolitik.lejeu.GameActors.Enemies.Bosses.Marmule;

/**
 * Phases for Ville boss.
 */

public enum MarmulePhases implements State<Marmule> {

    PHASE_1() {
        @Override
        public void enter(Marmule marmule) {
            marmule.speed = marmule.SPEED_PHASE_1;
            marmule.timeBetweenAttacks = marmule.TIME_BETWEEN_ATTACKS_PHASE_1;

            marmule.livingStateMachine.changeState(BossState.WALK);
        }

        @Override
        public boolean onMessage(Marmule marmule, Telegram telegram) {
            switch (telegram.message) {
                // attack when ready (once timeBetweenAttacks elapsed)
                case MessageType.READY_TO_ATTACK:
                    marmule.livingStateMachine.changeState(BossState.ATTACK);
                    return true;

                // send the projectile after attackDelay
                case MessageType.THROW_PROJECTILE:
                    marmule.throwProjectile();
                    return true;

                // go back to WALK state after the attack
                case MessageType.ATTACK_FINISHED:
                    marmule.livingStateMachine.changeState(BossState.WALK);
                    return true;
            }
            return false;

        }
    },

    PHASE_2() {
        @Override
        public void enter(Marmule marmule) {
            marmule.speed = marmule.SPEED_PHASE_2;
            marmule.timeBetweenAttacks = marmule.TIME_BETWEEN_CHARGES_PHASE_2;
                // during phase 2, attacks are just charges, where the marmule walks fast

            marmule.livingStateMachine.changeState(BossState.WALK);
        }

        @Override
        public boolean onMessage(Marmule marmule, Telegram telegram) {
            switch (telegram.message) {
                // charge when ready (once timeBetweenAttacks elapsed), and send a msg to go back to normal after
                case MessageType.READY_TO_ATTACK:
                    marmule.speed = marmule.CHARGE_SPEED;
                    MessageManager.getInstance().dispatchMessage(marmule.CHARGE_TIME, marmule, marmule,
                            MessageType.ATTACK_FINISHED);
                    return true;

                // go back to WALK state after the attack
                case MessageType.ATTACK_FINISHED:
                    marmule.speed = marmule.SPEED_PHASE_2;
                    marmule.livingStateMachine.changeState(BossState.WALK);
                    return true;
            }
            return false;
        }


    },

    PHASE_3() {
        @Override
        public void enter(Marmule marmule) {
            marmule.speed = marmule.SPEED_PHASE_3;
            marmule.timeBetweenAttacks = marmule.TIME_BETWEEN_ATTACKS_PHASE_3;

            marmule.livingStateMachine.changeState(BossState.WALK);
        }

        @Override
        public boolean onMessage(Marmule marmule, Telegram telegram) {
            switch (telegram.message) {
                // attack when ready (once timeBetweenAttacks elapsed)
                case MessageType.READY_TO_ATTACK:
                    marmule.livingStateMachine.changeState(BossState.ATTACK);
                    return true;

                // send the projectile after attackDelay
                case MessageType.THROW_PROJECTILE:
                    marmule.throwProjectile();
                    return true;

                // go back to WALK state after the attack
                case MessageType.ATTACK_FINISHED:
                    marmule.livingStateMachine.changeState(BossState.WALK);
                    return true;

                // end level once the boss is killed
                case MessageType.DEAD:
                    marmule.getGameStage().getLevel().loadGameWinMenu();
                    return true;
            }
            return false;

        }

    };

    @Override
    public void update(Marmule entity) {

    }

    @Override
    public void exit(Marmule entity) {

    }
}
