package com.muchpolitik.lejeu.AI.BossStates;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.muchpolitik.lejeu.AI.MessageType;
import com.muchpolitik.lejeu.GameActors.Enemies.Bosses.MarieNougatine;

/**
 * Phases for KKK boss.
 */

public enum MarieNougatinePhases implements State<MarieNougatine> {

    /**
     * Throw projectiles, change platform when hit (twice before going to PHASE_2).
     */
    PHASE_1() {
        @Override
        public void enter(MarieNougatine marieNougatine) {
            // spawns the boss on a random platform
            marieNougatine.changePlatform(marieNougatine.getRandomFreePlatform());

            marieNougatine.livingStateMachine.changeState(BossState.WALK);
        }

        @Override
        public boolean onMessage(MarieNougatine marieNougatine, Telegram telegram) {
            switch (telegram.message) {
                // attack when ready (once timeBetweenAttacks elapsed)
                case MessageType.READY_TO_ATTACK:
                    marieNougatine.livingStateMachine.changeState(BossState.ATTACK);
                    return true;

                // send the projectile after attackDelay
                case MessageType.THROW_PROJECTILE:
                    marieNougatine.throwProjectile();
                    return true;

                // go back to WALK state after the attack
                case MessageType.ATTACK_FINISHED:
                    marieNougatine.livingStateMachine.changeState(BossState.WALK);
                    return true;

                // end level once the boss is killed
                case MessageType.DEAD:
                    marieNougatine.getGameStage().getLevel().loadGameWinMenu();
                    return true;
            }
            return false;
        }
    },

    /**
     * Stays in HIDING state.
     */
    PHASE_2() {
        @Override
        public void enter(MarieNougatine marieNougatine) {
            // hide the boss
            marieNougatine.livingStateMachine.changeState(BossState.HIDING);

            marieNougatine.spawnMinions();
        }
    };

    @Override
    public void enter(MarieNougatine entity) {

    }

    @Override
    public void update(MarieNougatine entity) {

    }

    @Override
    public void exit(MarieNougatine entity) {

    }

    @Override
    public boolean onMessage(MarieNougatine entity, Telegram telegram) {
        return false;
    }
}
