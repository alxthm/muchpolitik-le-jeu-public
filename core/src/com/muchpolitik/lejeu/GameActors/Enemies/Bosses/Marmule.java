package com.muchpolitik.lejeu.GameActors.Enemies.Bosses;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.muchpolitik.lejeu.AI.BossStates.BossState;
import com.muchpolitik.lejeu.AI.BossStates.MarmulePhases;
import com.muchpolitik.lejeu.GameActors.Enemies.ProjectileThrowers.Russe;
import com.muchpolitik.lejeu.Stages.GameStage;

/**
 * The boss of 'Ville' world.
 */

public class Marmule extends Boss {

    public final float TIME_BETWEEN_ATTACKS_PHASE_1 = 2, TIME_BETWEEN_CHARGES_PHASE_2 = 2, TIME_BETWEEN_ATTACKS_PHASE_3 = 2,
            SPEED_PHASE_1 = 2, SPEED_PHASE_2 = 1.5f, SPEED_PHASE_3 = 1.5f,
            CHARGE_SPEED = 6, CHARGE_TIME = 0.75f;

    public StateMachine<Marmule, MarmulePhases> phasesStateMachine;

    public Marmule(GameStage gameStage){
        super(7, 1, 7, gameStage);          // the boss is first located in the middle of the map and can reach both sides

        // load graphics and state machines
        loadAnimations(gameStage.getLevel().getGameObjectsAtlas(), "marmule", true);
        loadStunnedAnimations(gameStage.getLevel().getGameObjectsAtlas(), "marmule");
        facingRight = MathUtils.randomBoolean();
        createStateMachines();

        // set custom variables
        defenseType = DefenseType.Tank;
        lives = 15; // the player has to hit 15 times the boss in order to win
        deathTime = 4;
        stunnedTime = 1.5f;

        // set hitbox
        setSize(1.5f, 1.5f);
        hitboxWidth = getWidth();
        bounds = new Rectangle(getX(), getY(), hitboxWidth, getHeight());

        // add a health bar (does not work atm)
        //healthBar = new HealthBar(this);
        //gameStage.addActor(healthBar);
    }

    @Override
    public void updateLivingStateMachine() {
        livingStateMachine.update();
    }

    @Override
    public boolean handleLivingStateMachineMessage(Telegram telegram) {
        return livingStateMachine.handleMessage(telegram);
    }

    @Override
    public void createStateMachines() {
        super.createStateMachines();

        // create livingStateMachine specific to projectile throwers
        livingStateMachine = new DefaultStateMachine<Boss, BossState>(this);

        // create phasesStateMachine
        phasesStateMachine = new DefaultStateMachine<>(this);
        phasesStateMachine.changeState(MarmulePhases.PHASE_1);
    }

    @Override
    public void turnAround() {
        switch (livingStateMachine.getCurrentState()) {
            case WALK:
                float newX = facingRight? (startX + rangeOfAction) : (startX - rangeOfAction);
                setX(newX);

                facingRight = !facingRight;
                currentAnimation = facingRight? walkRightAnimation : walkLeftAnimation;
                break;

            case ATTACK:
                facingRight = !facingRight;
                currentAnimation = facingRight? attackingRightAnimation : attackingLeftAnimation;
                break;
        }
    }

    /**
     * Lose a life, get stunned and check if the phase is finished (if the boss has lost enough lives)
     */
    @Override
    public void loseOneLife() {
        // change phase if needed
        switch (lives) {
            case 10:
                phasesStateMachine.changeState(MarmulePhases.PHASE_2);
                break;
            // encore 4 coups
            case 4:
                phasesStateMachine.changeState(MarmulePhases.PHASE_3);
                break;
        }

        // call after changing phase otherwise the boss isn't stunned
        super.loseOneLife();


        // spawn enemies to support the boss in the last phase
        if (lives <= 3) {
            float startX = MathUtils.random(5, 9);
            Russe russe = new Russe(startX, 1, 4, gameStage);
            // enemies helping the boss only need one life (too hard otherwise)
            russe.lives -= 1;
            gameStage.addActor(russe);
        }
    }

    @Override
    public boolean handlePhasesStateMachine(Telegram telegram) {
        return phasesStateMachine.handleMessage(telegram);
    }
}
